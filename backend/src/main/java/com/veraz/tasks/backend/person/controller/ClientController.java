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
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/clients")
@Tag(name = "Client", description = "Client endpoints")
public class ClientController implements ControllerInterface<UUID, ClientCreateRequestDTO, ClientUpdateRequestDTO, ClientResponseDTO> {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    @Operation(summary = "Get all clients", description = "Get all clients with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clients retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR')")
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<ClientResponseDTO>>> findAll(
            @ModelAttribute PaginationRequestDTO paginationRequest) {

        paginationRequest.validateAndNormalize();
        Pageable pageable = PaginationUtils.createPageable(paginationRequest);

        PaginatedResponseDTO<ClientResponseDTO> response = clientService.findAll(pageable);

        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, MessageUtils.getCrudSuccess(MessageKeys.CRUD_RETRIEVED_SUCCESS, "Clients"),
                response, null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get client by ID", description = "Retrieves a specific client by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client found successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<ApiResponseDTO<ClientResponseDTO>> findById(@PathVariable UUID id) {
        try {
            Optional<ClientResponseDTO> response = clientService.findById(id);
            if (response.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, MessageUtils.getCrudSuccess(MessageKeys.CRUD_RETRIEVED_SUCCESS, "Client"), response.get(), null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, HttpStatus.NOT_FOUND, MessageUtils.getEntityNotFound("Client"), null, null));
            }
        } catch (Exception e) {
            // GlobalExceptionHandler will handle specific exceptions
            throw e;
        }
    }

    @PostMapping
    @Operation(summary = "Create client", description = "Create client with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions."),
            @ApiResponse(responseCode = "409", description = "Conflict - Client already exists")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponseDTO<ClientResponseDTO>> create(@Valid @RequestBody ClientCreateRequestDTO clientRequestDTO) {
        try {
            ClientResponseDTO response = clientService.create(clientRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO<>(true, HttpStatus.CREATED, MessageUtils.getCrudSuccess(MessageKeys.CRUD_CREATED_SUCCESS, "Client"), response, null));
        } catch (Exception e) {
            // GlobalExceptionHandler will handle specific exceptions
            throw e;
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update client", description = "Updates an existing client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "409", description = "Client already exists")
    })
    public ResponseEntity<ApiResponseDTO<ClientResponseDTO>> update(@PathVariable UUID id, @Valid @RequestBody ClientUpdateRequestDTO clientRequest) {
        try {
            ClientResponseDTO response = clientService.update(id, clientRequest);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, MessageUtils.getCrudSuccess(MessageKeys.CRUD_UPDATED_SUCCESS, "Client"), response, null));
        } catch (Exception e) {
            // GlobalExceptionHandler will handle specific exceptions
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete client", description = "Delete client with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponseDTO<Void>> deleteById(@PathVariable UUID id) {
        try {
            clientService.deleteById(id);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, MessageUtils.getCrudSuccess(MessageKeys.CRUD_DELETED_SUCCESS, "Client"), null, null));
        } catch (Exception e) {
            // GlobalExceptionHandler will handle specific exceptions
            throw e;
        }
    }
}