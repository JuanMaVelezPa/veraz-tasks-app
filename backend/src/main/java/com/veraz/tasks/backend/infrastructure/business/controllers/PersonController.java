package com.veraz.tasks.backend.infrastructure.business.controllers;

import com.veraz.tasks.backend.application.business.commands.AssociateUserToPersonUseCase;
import com.veraz.tasks.backend.application.business.commands.CreatePersonUseCase;
import com.veraz.tasks.backend.application.business.commands.DeletePersonUseCase;
import com.veraz.tasks.backend.application.business.commands.RemoveUserAssociationUseCase;
import com.veraz.tasks.backend.application.business.commands.UpdatePersonUseCase;
import com.veraz.tasks.backend.application.business.dto.PersonResponse;
import com.veraz.tasks.backend.application.business.queries.FindAllPersonsUseCase;
import com.veraz.tasks.backend.application.business.queries.FindPersonByIdUseCase;
import com.veraz.tasks.backend.application.business.queries.FindPersonByUserIdUseCase;
import com.veraz.tasks.backend.infrastructure.business.dto.CreatePersonRequest;
import com.veraz.tasks.backend.infrastructure.business.dto.UpdatePersonRequest;
import com.veraz.tasks.backend.shared.dto.ApiResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;
import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.util.MessageUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/persons")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Person", description = "Person management endpoints")
public class PersonController {

    private final CreatePersonUseCase createPersonUseCase;
    private final FindPersonByIdUseCase findPersonByIdUseCase;
    private final FindPersonByUserIdUseCase findPersonByUserIdUseCase;
    private final FindAllPersonsUseCase findAllPersonsUseCase;
    private final AssociateUserToPersonUseCase associateUserToPersonUseCase;
    private final UpdatePersonUseCase updatePersonUseCase;
    private final RemoveUserAssociationUseCase removeUserAssociationUseCase;
    private final DeletePersonUseCase deletePersonUseCase;

    public PersonController(CreatePersonUseCase createPersonUseCase,
            FindPersonByIdUseCase findPersonByIdUseCase,
            FindPersonByUserIdUseCase findPersonByUserIdUseCase,
            FindAllPersonsUseCase findAllPersonsUseCase,
            AssociateUserToPersonUseCase associateUserToPersonUseCase,
            UpdatePersonUseCase updatePersonUseCase,
            RemoveUserAssociationUseCase removeUserAssociationUseCase,
            DeletePersonUseCase deletePersonUseCase) {
        this.createPersonUseCase = createPersonUseCase;
        this.findPersonByIdUseCase = findPersonByIdUseCase;
        this.findPersonByUserIdUseCase = findPersonByUserIdUseCase;
        this.findAllPersonsUseCase = findAllPersonsUseCase;
        this.associateUserToPersonUseCase = associateUserToPersonUseCase;
        this.updatePersonUseCase = updatePersonUseCase;
        this.removeUserAssociationUseCase = removeUserAssociationUseCase;
        this.deletePersonUseCase = deletePersonUseCase;
    }

    @GetMapping
    @Operation(summary = "Get all persons", description = "Retrieves all persons with pagination using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Persons retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<PersonResponse>>> getAllPersons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) String search) {

        try {
            PaginationRequestDTO paginationRequest = new PaginationRequestDTO(page, size, sortBy, sortDirection,
                    search);
            PaginatedResponseDTO<PersonResponse> response = findAllPersonsUseCase.execute(paginationRequest);

            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    HttpStatus.OK,
                    MessageUtils.getMessage(MessageKeys.CONTROLLER_RETRIEVED_SUCCESS,
                            MessageUtils.getLocalizedEntityName("person")),
                    response,
                    null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_INVALID_DATA, e.getMessage()),
                            null,
                            null));
        }
    }

    @PostMapping
    @Operation(summary = "Create a new person", description = "Creates a new person using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Person created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Person already exists")
    })
    public ResponseEntity<ApiResponseDTO<PersonResponse>> createPerson(
            @Valid @RequestBody CreatePersonRequest request) {

        try {
            PersonResponse appResponse = createPersonUseCase.execute(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO<>(
                            true,
                            HttpStatus.CREATED,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_CREATED_SUCCESS,
                                    MessageUtils.getLocalizedEntityName("person")),
                            appResponse,
                            null));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.CONFLICT,
                            e.getMessage(),
                            null,
                            null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_INVALID_DATA, e.getMessage()),
                            null,
                            null));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get person by ID", description = "Retrieves a person by ID using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person found successfully"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format")
    })
    public ResponseEntity<ApiResponseDTO<PersonResponse>> getPersonById(@PathVariable String id) {

        try {
            Optional<PersonResponse> appResponse = findPersonByIdUseCase.execute(id);

            if (appResponse.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO<>(
                        true,
                        HttpStatus.OK,
                        MessageUtils.getMessage(MessageKeys.CONTROLLER_FOUND_SUCCESS,
                                MessageUtils.getLocalizedEntityName("person")),
                        appResponse.get(),
                        null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(
                                false,
                                HttpStatus.NOT_FOUND,
                                MessageUtils.getMessage(MessageKeys.CONTROLLER_NOT_FOUND_WITH_ID,
                                        MessageUtils.getLocalizedEntityName("person"), id),
                                null,
                                null));
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_INVALID_ID_FORMAT, e.getMessage()),
                            null,
                            null));
        }
    }

    @GetMapping("/by-user/{userId}")
    @Operation(summary = "Get person by user ID", description = "Retrieves a person by user ID using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person found successfully"),
            @ApiResponse(responseCode = "404", description = "Person not found for user"),
            @ApiResponse(responseCode = "400", description = "Invalid user ID format")
    })
    public ResponseEntity<ApiResponseDTO<PersonResponse>> getPersonByUserId(@PathVariable String userId) {

        try {
            PersonResponse appResponse = findPersonByUserIdUseCase.execute(userId);

            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    HttpStatus.OK,
                    MessageUtils.getMessage(MessageKeys.CONTROLLER_FOUND_SUCCESS,
                            MessageUtils.getLocalizedEntityName("person")),
                    appResponse,
                    null));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.NOT_FOUND,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_NOT_FOUND_FOR_USER,
                                    MessageUtils.getLocalizedEntityName("person"), userId),
                            null,
                            null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_INVALID_USER_ID_FORMAT, e.getMessage()),
                            null,
                            null));
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update person", description = "Updates a person using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Person not found")
    })
    public ResponseEntity<ApiResponseDTO<PersonResponse>> updatePerson(
            @PathVariable String id,
            @Valid @RequestBody UpdatePersonRequest request) {

        try {
            PersonResponse appResponse = updatePersonUseCase.execute(id, request);

            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    HttpStatus.OK,
                    MessageUtils.getMessage(MessageKeys.CONTROLLER_UPDATED_SUCCESS,
                            MessageUtils.getLocalizedEntityName("person")),
                    appResponse,
                    null));

        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(
                                false,
                                HttpStatus.NOT_FOUND,
                                e.getMessage(),
                                null,
                                null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponseDTO<>(
                                false,
                                HttpStatus.BAD_REQUEST,
                                e.getMessage(),
                                null,
                                null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_INVALID_DATA, e.getMessage()),
                            null,
                            null));
        }
    }

    @PatchMapping("/{id}/associate-user")
    @Operation(summary = "Associate user to person", description = "Associates a user to a person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User associated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Person or user not found"),
            @ApiResponse(responseCode = "409", description = "User or person already associated")
    })
    public ResponseEntity<ApiResponseDTO<PersonResponse>> associateUser(
            @PathVariable String id,
            @RequestParam String userId) {

        try {
            PersonResponse appResponse = associateUserToPersonUseCase.execute(id, userId);

            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    HttpStatus.OK,
                    MessageUtils.getMessage(MessageKeys.CONTROLLER_ASSOCIATED_SUCCESS,
                            MessageUtils.getLocalizedEntityName("user")),
                    appResponse,
                    null));

        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(
                                false,
                                HttpStatus.NOT_FOUND,
                                e.getMessage(),
                                null,
                                null));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponseDTO<>(
                                false,
                                HttpStatus.CONFLICT,
                                e.getMessage(),
                                null,
                                null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_INVALID_DATA, e.getMessage()),
                            null,
                            null));
        }
    }

    @PatchMapping("/remove-user/{id}")
    @Operation(summary = "Remove user association from person", description = "Removes user association from a person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User association removed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "409", description = "Person does not have user associated")
    })
    public ResponseEntity<ApiResponseDTO<PersonResponse>> removeUserAssociation(@PathVariable String id) {

        try {
            PersonResponse appResponse = removeUserAssociationUseCase.execute(id);

            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    HttpStatus.OK,
                    MessageUtils.getMessage(MessageKeys.CONTROLLER_ASSOCIATION_REMOVED_SUCCESS,
                            MessageUtils.getLocalizedEntityName("user")),
                    appResponse,
                    null));

        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(
                                false,
                                HttpStatus.NOT_FOUND,
                                e.getMessage(),
                                null,
                                null));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponseDTO<>(
                                false,
                                HttpStatus.CONFLICT,
                                e.getMessage(),
                                null,
                                null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_INVALID_DATA, e.getMessage()),
                            null,
                            null));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete person", description = "Deletes a person using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Person not found")
    })
    public ResponseEntity<ApiResponseDTO<Void>> deletePerson(@PathVariable String id) {

        try {
            deletePersonUseCase.execute(id);

            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    HttpStatus.OK,
                    MessageUtils.getMessage(MessageKeys.CONTROLLER_DELETED_SUCCESS,
                            MessageUtils.getLocalizedEntityName("person")),
                    null,
                    null));

        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(
                                false,
                                HttpStatus.NOT_FOUND,
                                e.getMessage(),
                                null,
                                null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponseDTO<>(
                                false,
                                HttpStatus.BAD_REQUEST,
                                e.getMessage(),
                                null,
                                null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_INVALID_DATA, e.getMessage()),
                            null,
                            null));
        }
    }

}
