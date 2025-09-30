package com.veraz.tasks.backend.infrastructure.identity.controllers;

import com.veraz.tasks.backend.application.business.dto.UserResponse;
import com.veraz.tasks.backend.application.identity.commands.CreateUserUseCase;

import com.veraz.tasks.backend.application.identity.commands.UpdateUserUseCase;
import com.veraz.tasks.backend.application.identity.queries.FindAllUsersUseCase;
import com.veraz.tasks.backend.application.identity.queries.FindAvailableUsersUseCase;
import com.veraz.tasks.backend.application.identity.queries.FindUserByIdUseCase;
import com.veraz.tasks.backend.application.identity.commands.DeleteUserUseCase;
import com.veraz.tasks.backend.infrastructure.identity.dto.CreateUserRequest;
import com.veraz.tasks.backend.infrastructure.identity.dto.UpdateUserRequest;
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
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "User", description = "User management endpoints using Clean Architecture")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final FindAllUsersUseCase findAllUsersUseCase;
    private final FindAvailableUsersUseCase findAvailableUsersUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase,
            FindUserByIdUseCase findUserByIdUseCase,
            FindAllUsersUseCase findAllUsersUseCase,
            FindAvailableUsersUseCase findAvailableUsersUseCase,
            UpdateUserUseCase updateUserUseCase,
            DeleteUserUseCase deleteUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.findUserByIdUseCase = findUserByIdUseCase;
        this.findAllUsersUseCase = findAllUsersUseCase;
        this.findAvailableUsersUseCase = findAvailableUsersUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves all users with pagination using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) String search) {

        try {
            PaginationRequestDTO paginationRequest = new PaginationRequestDTO(page, size, sortBy, sortDirection,
                    search);
            PaginatedResponseDTO<UserResponse> response = findAllUsersUseCase.execute(paginationRequest);

            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    HttpStatus.OK,
                    MessageUtils.getMessage(MessageKeys.CONTROLLER_RETRIEVED_SUCCESS,
                            MessageUtils.getLocalizedEntityName("user")),
                    response,
                    null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_INVALID_PAGINATION, e.getMessage()),
                            null,
                            null));
        }
    }

    @GetMapping("/available")
    @Operation(summary = "Get available users", description = "Retrieves users not associated with any person with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Available users retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<UserResponse>>> getAvailableUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) String search) {

        try {
            PaginationRequestDTO paginationRequest = new PaginationRequestDTO(page, size, sortBy, sortDirection,
                    search);
            PaginatedResponseDTO<UserResponse> response = findAvailableUsersUseCase.execute(paginationRequest);

            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    HttpStatus.OK,
                    MessageUtils.getMessage(MessageKeys.CONTROLLER_RETRIEVED_SUCCESS,
                            MessageUtils.getLocalizedEntityName("user")),
                    response,
                    null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_INVALID_PAGINATION, e.getMessage()),
                            null,
                            null));
        }
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<ApiResponseDTO<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        try {
            UserResponse appResponse = createUserUseCase.execute(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO<>(
                            true,
                            HttpStatus.CREATED,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_CREATED_SUCCESS,
                                    MessageUtils.getLocalizedEntityName("user")),
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
    @Operation(summary = "Get user by ID", description = "Retrieves a user by ID using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format")
    })
    public ResponseEntity<ApiResponseDTO<UserResponse>> getUserById(@PathVariable String id) {

        try {
            Optional<UserResponse> appResponse = findUserByIdUseCase.execute(id);

            if (appResponse.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO<>(
                        true,
                        HttpStatus.OK,
                        MessageUtils.getMessage(MessageKeys.CONTROLLER_FOUND_SUCCESS,
                                MessageUtils.getLocalizedEntityName("user")),
                        appResponse.get(),
                        null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(
                                false,
                                HttpStatus.NOT_FOUND,
                                MessageUtils.getMessage(MessageKeys.CONTROLLER_NOT_FOUND_WITH_ID,
                                        MessageUtils.getLocalizedEntityName("user"), id),
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

    @PatchMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates a user using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ApiResponseDTO<UserResponse>> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UpdateUserRequest request) {

        try {
            UserResponse appResponse = updateUserUseCase.execute(id, request);

            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    HttpStatus.OK,
                    MessageUtils.getMessage(MessageKeys.CONTROLLER_UPDATED_SUCCESS,
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

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ApiResponseDTO<Void>> deleteUser(@PathVariable String id) {

        try {
            deleteUserUseCase.execute(id);

            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    HttpStatus.OK,
                    MessageUtils.getMessage(MessageKeys.CONTROLLER_DELETED_SUCCESS,
                            MessageUtils.getLocalizedEntityName("user")),
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
