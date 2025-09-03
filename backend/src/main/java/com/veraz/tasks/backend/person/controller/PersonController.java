package com.veraz.tasks.backend.person.controller;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.veraz.tasks.backend.shared.controller.ControllerInterface;
import com.veraz.tasks.backend.shared.dto.ApiResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;
import com.veraz.tasks.backend.shared.util.PaginationUtils;
import com.veraz.tasks.backend.person.service.PersonService;
import com.veraz.tasks.backend.person.repository.PersonRepository;
import com.veraz.tasks.backend.person.model.Person;
import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.util.MessageUtils;
import com.veraz.tasks.backend.person.dto.PersonUserAssociationDTO;

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
public class PersonController
        implements ControllerInterface<UUID, PersonCreateRequestDTO, PersonUpdateRequestDTO, PersonResponseDTO> {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private final PersonService personService;
    private final PersonRepository personRepository;

    public PersonController(PersonService personService, PersonRepository personRepository) {
        this.personService = personService;
        this.personRepository = personRepository;
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
                MessageUtils.getCrudSuccessMessage(MessageKeys.CRUD_RETRIEVED_SUCCESS, "Persons"),
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
                    MessageUtils.getCrudSuccessMessage(MessageKeys.CRUD_RETRIEVED_SUCCESS, "Person"),
                    response.get(), null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, HttpStatus.NOT_FOUND,
                            MessageUtils.getEntityNotFoundMessage("Person"), null, null));
        }
    }

    @PostMapping
    @Operation(summary = "Create person", description = "Admin/Manager access OR create for own user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("@permissionService.canCreatePerson(#createRequest.userId)")
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> create(
            @Valid @RequestBody PersonCreateRequestDTO createRequest) {
        PersonResponseDTO response = personService.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, HttpStatus.CREATED,
                        MessageUtils.getCrudSuccessMessage(MessageKeys.CRUD_CREATED_SUCCESS, "Person"),
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
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> update(@PathVariable UUID id,
            @Valid @RequestBody PersonUpdateRequestDTO updateRequest) {
        PersonResponseDTO response = personService.update(id, updateRequest);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK,
                MessageUtils.getCrudSuccessMessage(MessageKeys.CRUD_UPDATED_SUCCESS, "Person"),
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
                MessageUtils.getCrudSuccessMessage(MessageKeys.CRUD_DELETED_SUCCESS, "Person"),
                null, null));
    }

    @GetMapping("/by-user/{userId}")
    @Operation(summary = "Get person by user ID", description = "Admin/Manager/Supervisor access OR user ownership")
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
                    MessageUtils.getCrudSuccessMessage(MessageKeys.CRUD_RETRIEVED_SUCCESS, "Person"),
                    response.get(), null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, HttpStatus.NOT_FOUND,
                            MessageUtils.getEntityNotFoundMessage("Person"), null, null));
        }
    }

    @PatchMapping("/associate-user/{id}")
    @Operation(summary = "Associate user with person", description = "Admin/Manager access only")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - User already associated")
    })
    @PreAuthorize("@permissionService.canWriteResources()")
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> associateUser(
            @PathVariable UUID id,
            @RequestBody PersonUserAssociationDTO associationRequest) {

        PersonResponseDTO response = personService.associateUser(id, associationRequest.getUserId());
        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK,
                "User associated successfully with person", response, null));
    }

    @PatchMapping("/remove-user/{id}")
    @Operation(summary = "Remove user association from person", description = "Admin/Manager access only")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - No user association found")
    })
    @PreAuthorize("@permissionService.canWriteResources()")
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> removeUserAssociation(@PathVariable UUID id) {
        PersonResponseDTO response = personService.removeUserAssociation(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK,
                "User association removed successfully from person", response, null));
    }

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Simple test to verify controller is working")
    public ResponseEntity<String> test() {
        // logger.info("Test endpoint called successfully"); // Original code had this line commented out
        return ResponseEntity.ok("PersonController is working! Current time: " + java.time.LocalDateTime.now());
    }

    @GetMapping("/exists/{id}")
    @Operation(summary = "Check if person exists", description = "Test endpoint to verify person existence")
    public ResponseEntity<String> checkPersonExists(@PathVariable UUID id) {
        logger.info("Checking if person exists with ID: {}", id);
        
        boolean exists = personRepository.existsById(id);
        logger.info("Person with ID {} exists: {}", id, exists);
        
        if (exists) {
            try {
                Person person = personRepository.findByIdWithUser(id).orElse(null);
                if (person != null) {
                    return ResponseEntity.ok(String.format("Person exists: %s %s (ID: %s)", 
                        person.getFirstName(), person.getLastName(), person.getId()));
                } else {
                    return ResponseEntity.ok("Person exists but findByIdWithUser failed");
                }
            } catch (Exception e) {
                logger.error("Error finding person with ID {}: {}", id, e.getMessage());
                return ResponseEntity.ok("Person exists but error occurred: " + e.getMessage());
            }
        } else {
            return ResponseEntity.ok("Person does not exist with ID: " + id);
        }
    }
}