package com.veraz.tasks.backend.infrastructure.identity.controllers;

import com.veraz.tasks.backend.application.identity.commands.LoginUseCase;
import com.veraz.tasks.backend.application.identity.commands.ValidateTokenHeaderUseCase;
import com.veraz.tasks.backend.application.business.dto.LoginResponse;
import com.veraz.tasks.backend.application.business.dto.UserResponse;
import com.veraz.tasks.backend.application.identity.commands.CreateUserUseCase;
import com.veraz.tasks.backend.infrastructure.identity.dto.LoginRequest;
import com.veraz.tasks.backend.infrastructure.identity.dto.CreateUserRequest;
import com.veraz.tasks.backend.shared.dto.ApiResponseDTO;
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

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Auth", description = "Authentication endpoints using Clean Architecture")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final ValidateTokenHeaderUseCase validateTokenHeaderUseCase;
    private final CreateUserUseCase createUserUseCase;

    public AuthController(LoginUseCase loginUseCase,
            ValidateTokenHeaderUseCase validateTokenHeaderUseCase,
            CreateUserUseCase createUserUseCase) {
        this.loginUseCase = loginUseCase;
        this.validateTokenHeaderUseCase = validateTokenHeaderUseCase;
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping("/sign-in")
    @Operation(summary = "Sign-in user", description = "Sign-in user with email or username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sign-in successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data")
    })
    public ResponseEntity<ApiResponseDTO<LoginResponse>> signIn(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse appResponse = loginUseCase.execute(request);

            if (loginUseCase.isSuccessfulLogin(appResponse)) {
                return ResponseEntity.ok(new ApiResponseDTO<>(
                        true,
                        HttpStatus.OK,
                        MessageUtils.getMessage(MessageKeys.AUTH_SIGNIN_SUCCESS),
                        appResponse,
                        null));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(
                                false,
                                HttpStatus.UNAUTHORIZED,
                                appResponse.message(),
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

    @PostMapping("/sign-up")
    @Operation(summary = "Register new user", description = "Creates a new user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<ApiResponseDTO<UserResponse>> signUp(@Valid @RequestBody CreateUserRequest request) {
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

    @GetMapping("/check-status")
    @Operation(summary = "Check authentication status", description = "Checks if the provided JWT token is valid and returns user details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication status checked successfully. Token is valid."),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ApiResponseDTO<LoginResponse>> checkAuthenticationStatus(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        try {
            LoginResponse appResponse = validateTokenHeaderUseCase.execute(authorization);

            if (validateTokenHeaderUseCase.isSuccessfulValidation(appResponse)) {
                return ResponseEntity.ok(new ApiResponseDTO<>(
                        true,
                        HttpStatus.OK,
                        "Token is valid",
                        appResponse,
                        null));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseDTO<>(
                                false,
                                HttpStatus.UNAUTHORIZED,
                                appResponse.message(),
                                null,
                                null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.UNAUTHORIZED,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_INVALID_TOKEN, e.getMessage()),
                            null,
                            null));
        }
    }
}
