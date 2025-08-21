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

import com.veraz.tasks.backend.person.dto.PersonCreateRequestDTO;
import com.veraz.tasks.backend.person.dto.PersonUpdateRequestDTO;
import com.veraz.tasks.backend.person.dto.PersonResponseDTO;
import com.veraz.tasks.backend.person.dto.PersonUserAssociationDTO;
import com.veraz.tasks.backend.shared.controller.ControllerInterface;
import com.veraz.tasks.backend.shared.dto.ApiResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;
import com.veraz.tasks.backend.shared.util.PaginationUtils;
import com.veraz.tasks.backend.person.service.PersonService;
import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.util.MessageUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST controller for Person entity management
 * Provides CRUD operations and user association management
 * Implements role-based access control for all operations
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/persons")
@Tag(name = "Person", description = "Person management endpoints")
public class PersonController implements ControllerInterface<UUID, PersonCreateRequestDTO, PersonUpdateRequestDTO, PersonResponseDTO> {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    @Operation(summary = "Get all persons", description = "Admin/Manager/Supervisor access")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("@permissionService.canReadResources()")
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<PersonResponseDTO>>> findAll(
            @ModelAttribute PaginationRequestDTO paginationRequest) {

        paginationRequest.validateAndNormalize();
        Pageable pageable = PaginationUtils.createPageable(paginationRequest);

        PaginatedResponseDTO<PersonResponseDTO> response = paginationRequest.hasSearch() 
            ? personService.findBySearch(paginationRequest.getSearch(), pageable)
            : personService.findAll(pageable);

        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, 
                MessageUtils.getCrudSuccess(MessageKeys.CRUD_RETRIEVED_SUCCESS, "Persons"),
                response, null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get person by ID", description = "Admin/Manager/Supervisor access OR resource ownership")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PreAuthorize("@permissionService.canAccessResource(#id, 'PERSON') or @permissionService.hasAdminAccess()")
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> findById(@PathVariable UUID id) {
        Optional<PersonResponseDTO> response = personService.findById(id);
        
        if (response.isPresent()) {
            return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK,
                    MessageUtils.getCrudSuccess(MessageKeys.CRUD_RETRIEVED_SUCCESS, "Person"), 
                    response.get(), null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, HttpStatus.NOT_FOUND,
                            MessageUtils.getEntityNotFound("Person"), null, null));
        }
    }

    @PostMapping
    @Operation(summary = "Create person", description = "Admin/Manager access OR create for own user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("@permissionService.canCreatePerson(#personRequestDTO.userId)")
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> create(@Valid @RequestBody PersonCreateRequestDTO personRequestDTO) {
        PersonResponseDTO response = personService.create(personRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, HttpStatus.CREATED, 
                        MessageUtils.getCrudSuccess(MessageKeys.CRUD_CREATED_SUCCESS, "Person"), 
                        response, null));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update person", description = "Admin/Manager access OR person ownership")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PreAuthorize("@permissionService.canWriteResources() or @permissionService.isPersonOwner(#id)")
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> update(@PathVariable UUID id, @Valid @RequestBody PersonUpdateRequestDTO personRequest) {
        PersonResponseDTO response = personService.update(id, personRequest);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK,
                MessageUtils.getCrudSuccess(MessageKeys.CRUD_UPDATED_SUCCESS, "Person"), 
                response, null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete person", description = "Admin/Manager access only")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("@permissionService.canWriteResources()")
    public ResponseEntity<ApiResponseDTO<Void>> deleteById(@PathVariable UUID id) {
        personService.deleteById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK,
                MessageUtils.getCrudSuccess(MessageKeys.CRUD_DELETED_SUCCESS, "Person"), 
                null, null));
    }

    @PatchMapping("/remove-user/{id}")
    @Operation(summary = "Remove user association", description = "Admin/Manager access only")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("@permissionService.canWriteResources()")
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> removeUserAssociation(@PathVariable UUID id) {
        PersonResponseDTO response = personService.removeUserAssociation(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, 
                "User association removed successfully", response, null));
    }

    @PatchMapping("/associate-user/{id}")
    @Operation(summary = "Associate user with person", description = "Admin/Manager access only")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("@permissionService.canWriteResources()")
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> associateUser(@PathVariable UUID id, @Valid @RequestBody PersonUserAssociationDTO associationDTO) {
        PersonResponseDTO response = personService.associateUser(id, associationDTO.getUserId());
        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, 
                "User associated successfully", response, null));
    }

    @GetMapping("/by-user/{userId}")
    @Operation(summary = "Get person by user ID", description = "Admin/Manager/Supervisor access OR resource ownership")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PreAuthorize("@permissionService.canAccessResource(#userId, 'USER') or @permissionService.hasAdminAccess()")
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> findByUserId(@PathVariable UUID userId) {
        Optional<PersonResponseDTO> response = personService.findByUserId(userId);
        
        if (response.isPresent()) {
            return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK,
                    MessageUtils.getCrudSuccess(MessageKeys.CRUD_RETRIEVED_SUCCESS, "Person"), 
                    response.get(), null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, HttpStatus.NOT_FOUND,
                            MessageUtils.getEntityNotFound("Person"), null, null));
        }
    }

}