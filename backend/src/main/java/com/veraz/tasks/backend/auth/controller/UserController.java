package com.veraz.tasks.backend.auth.controller;

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

import com.veraz.tasks.backend.auth.dto.UserCreateRequestDTO;
import com.veraz.tasks.backend.auth.dto.UserUpdateRequestDTO;
import com.veraz.tasks.backend.auth.dto.UserResponseDTO;
import com.veraz.tasks.backend.shared.controller.ControllerInterface;
import com.veraz.tasks.backend.shared.dto.ApiResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;
import com.veraz.tasks.backend.shared.util.PaginationUtils;
import com.veraz.tasks.backend.auth.service.UserService;
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
@RequestMapping("/users")
@Tag(name = "User", description = "User endpoints")
public class UserController implements ControllerInterface<UUID, UserCreateRequestDTO, UserUpdateRequestDTO, UserResponseDTO> {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Get all users with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR')")
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<UserResponseDTO>>> findAll(
            @ModelAttribute PaginationRequestDTO paginationRequest) {

        paginationRequest.validateAndNormalize();
        Pageable pageable = PaginationUtils.createPageable(paginationRequest);

        PaginatedResponseDTO<UserResponseDTO> response = userService.findAll(pageable);

        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, MessageUtils.getCrudSuccess(MessageKeys.CRUD_RETRIEVED_SUCCESS, "Users"),
                response, null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a specific user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> findById(@PathVariable UUID id) {
        try {
            Optional<UserResponseDTO> response = userService.findById(id);
            if (response.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, MessageUtils.getCrudSuccess(MessageKeys.CRUD_RETRIEVED_SUCCESS, "User"), response.get(), null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, HttpStatus.NOT_FOUND, MessageUtils.getEntityNotFound("User"), null, null));
            }
        } catch (Exception e) {
            // GlobalExceptionHandler will handle specific exceptions
            throw e;
        }
    }

    @PostMapping
    @Operation(summary = "Create user", description = "Create a new user with required fields")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions."),
            @ApiResponse(responseCode = "409", description = "Conflict - Username or email already exists")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> create(@Valid @RequestBody UserCreateRequestDTO userRequestDTO) {
        try {
            UserResponseDTO response = userService.create(userRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO<>(true, HttpStatus.CREATED, MessageUtils.getCrudSuccess(MessageKeys.CRUD_CREATED_SUCCESS, "User"), response, null));
        } catch (Exception e) {
            // GlobalExceptionHandler will handle specific exceptions
            throw e;
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates an existing user with partial data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> update(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequestDTO userRequest) {
        try {
            UserResponseDTO response = userService.update(id, userRequest);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, MessageUtils.getCrudSuccess(MessageKeys.CRUD_UPDATED_SUCCESS, "User"), response, null));
        } catch (Exception e) {
            // GlobalExceptionHandler will handle specific exceptions
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete user with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponseDTO<Void>> deleteById(@PathVariable UUID id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, MessageUtils.getCrudSuccess(MessageKeys.CRUD_DELETED_SUCCESS, "User"), null, null));
        } catch (Exception e) {
            // GlobalExceptionHandler will handle specific exceptions
            throw e;
        }
    }
}
