package com.veraz.tasks.backend.person.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veraz.tasks.backend.person.dto.ClientCreateRequestDTO;
import com.veraz.tasks.backend.person.dto.ClientUpdateRequestDTO;
import com.veraz.tasks.backend.person.dto.ClientResponseDTO;
import com.veraz.tasks.backend.shared.controller.ControllerInterface;
import com.veraz.tasks.backend.shared.dto.ApiResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;
import com.veraz.tasks.backend.shared.util.PaginationUtils;
import com.veraz.tasks.backend.person.service.ClientService;
import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.util.MessageUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/clients")
@Tag(name = "Client", description = "Client management endpoints")
public class ClientController
        implements ControllerInterface<UUID, ClientCreateRequestDTO, ClientUpdateRequestDTO, ClientResponseDTO> {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    @Operation(summary = "Get all clients", description = "Admin/Manager/Supervisor access")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("@permissionService.canReadResources()")
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<ClientResponseDTO>>> findAll(
            @ModelAttribute PaginationRequestDTO paginationRequest) {

        paginationRequest.validateAndNormalize();
        Pageable pageable = PaginationUtils.createPageable(paginationRequest);

        PaginatedResponseDTO<ClientResponseDTO> response = clientService.findAll(pageable);

        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK,
                MessageUtils.getCrudSuccess(MessageKeys.CRUD_RETRIEVED_SUCCESS, "Clients"),
                response, null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get client by ID", description = "Admin/Manager/Supervisor access OR resource ownership")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PreAuthorize("@permissionService.canAccessResource(#id, 'CLIENT') or @permissionService.hasAdminAccess()")
    public ResponseEntity<ApiResponseDTO<ClientResponseDTO>> findById(@PathVariable UUID id) {
        Optional<ClientResponseDTO> response = clientService.findById(id);

        if (response.isPresent()) {
            return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK,
                    MessageUtils.getCrudSuccess(MessageKeys.CRUD_RETRIEVED_SUCCESS, "Client"),
                    response.get(), null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, HttpStatus.NOT_FOUND,
                            MessageUtils.getEntityNotFound("Client"), null, null));
        }
    }

    @PostMapping
    @Operation(summary = "Create client", description = "Admin/Manager access OR create for own person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("@permissionService.canCreateClient(#clientRequestDTO.personId)")
    public ResponseEntity<ApiResponseDTO<ClientResponseDTO>> create(
            @Valid @RequestBody ClientCreateRequestDTO clientRequestDTO) {
        ClientResponseDTO response = clientService.create(clientRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, HttpStatus.CREATED,
                        MessageUtils.getCrudSuccess(MessageKeys.CRUD_CREATED_SUCCESS, "Client"),
                        response, null));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update client", description = "Admin/Manager access OR resource ownership")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PreAuthorize("@permissionService.canWriteResources() or @permissionService.isResourceOwner(#id)")
    public ResponseEntity<ApiResponseDTO<ClientResponseDTO>> update(@PathVariable UUID id,
            @Valid @RequestBody ClientUpdateRequestDTO clientRequest) {
        ClientResponseDTO response = clientService.update(id, clientRequest);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK,
                MessageUtils.getCrudSuccess(MessageKeys.CRUD_UPDATED_SUCCESS, "Client"),
                response, null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete client", description = "Admin/Manager access only")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("@permissionService.canWriteResources()")
    public ResponseEntity<ApiResponseDTO<Void>> deleteById(@PathVariable UUID id) {
        clientService.deleteById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK,
                MessageUtils.getCrudSuccess(MessageKeys.CRUD_DELETED_SUCCESS, "Client"),
                null, null));
    }
}