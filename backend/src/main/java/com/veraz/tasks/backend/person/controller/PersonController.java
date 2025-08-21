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
import org.springframework.web.bind.annotation.RequestBody;
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
    @Operation(summary = "Get all persons", description = "Get all persons with pagination and search")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Persons retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR')")
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<PersonResponseDTO>>> findAll(
            @ModelAttribute PaginationRequestDTO paginationRequest) {

        paginationRequest.validateAndNormalize();
        Pageable pageable = PaginationUtils.createPageable(paginationRequest);

        PaginatedResponseDTO<PersonResponseDTO> response;

        if (paginationRequest.hasSearch()) {
            response = personService.findBySearch(paginationRequest.getSearch(), pageable);
        } else {
            response = personService.findAll(pageable);
        }

        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, MessageUtils.getCrudSuccess(MessageKeys.CRUD_RETRIEVED_SUCCESS, "Persons"),
                response, null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get person by ID", description = "Retrieves a specific person by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person found successfully"),
            @ApiResponse(responseCode = "404", description = "Person not found")
    })
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> findById(@PathVariable UUID id) {
        try {
            Optional<PersonResponseDTO> response = personService.findById(id);
            if (response.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, MessageUtils.getCrudSuccess(MessageKeys.CRUD_RETRIEVED_SUCCESS, "Person"), response.get(), null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, HttpStatus.NOT_FOUND, MessageUtils.getEntityNotFound("Person"), null, null));
            }
        } catch (Exception e) {
            // GlobalExceptionHandler will handle specific exceptions
            throw e;
        }
    }

    @PostMapping
    @Operation(summary = "Create person", description = "Create a new person with required fields")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Person created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions."),
            @ApiResponse(responseCode = "409", description = "Conflict - Person already exists")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> create(@Valid @RequestBody PersonCreateRequestDTO personRequestDTO) {
        try {
            PersonResponseDTO response = personService.create(personRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO<>(true, HttpStatus.CREATED, MessageUtils.getCrudSuccess(MessageKeys.CRUD_CREATED_SUCCESS, "Person"), response, null));
        } catch (Exception e) {
            // GlobalExceptionHandler will handle specific exceptions
            throw e;
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update person", description = "Updates an existing person with partial data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "409", description = "Person already exists")
    })
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> update(@PathVariable UUID id, @Valid @RequestBody PersonUpdateRequestDTO personRequest) {
        try {
            PersonResponseDTO response = personService.update(id, personRequest);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, MessageUtils.getCrudSuccess(MessageKeys.CRUD_UPDATED_SUCCESS, "Person"), response, null));
        } catch (Exception e) {
            // GlobalExceptionHandler will handle specific exceptions
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete person", description = "Delete person with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions."),
            @ApiResponse(responseCode = "409", description = "Conflict - Person has associated user")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponseDTO<Void>> deleteById(@PathVariable UUID id) {
        try {
            personService.deleteById(id);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, MessageUtils.getCrudSuccess(MessageKeys.CRUD_DELETED_SUCCESS, "Person"), null, null));
        } catch (Exception e) {
            // GlobalExceptionHandler will handle specific exceptions
            throw e;
        }
    }

    @PatchMapping("/remove-user/{id}")
    @Operation(summary = "Remove user association", description = "Remove user association from person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User association removed successfully"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> removeUserAssociation(@PathVariable UUID id) {
        try {
            PersonResponseDTO response = personService.removeUserAssociation(id);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, "User association removed successfully", response, null));
        } catch (Exception e) {
            // GlobalExceptionHandler will handle specific exceptions
            throw e;
        }
    }

    @PatchMapping("/associate-user/{id}")
    @Operation(summary = "Associate user with person", description = "Associate a user with a person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User associated successfully"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> associateUser(@PathVariable UUID id, @Valid @RequestBody PersonUserAssociationDTO associationDTO) {
        try {
            PersonResponseDTO response = personService.associateUser(id, associationDTO.getUserId());
            return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, "User associated successfully", response, null));
        } catch (Exception e) {
            // GlobalExceptionHandler will handle specific exceptions
            throw e;
        }
    }
}