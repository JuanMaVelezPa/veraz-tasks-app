package com.veraz.tasks.backend.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veraz.tasks.backend.auth.dto.AuthResponseDTO;
import com.veraz.tasks.backend.auth.dto.AuthRequestDTO;
import com.veraz.tasks.backend.auth.dto.UserCreateRequestDTO;
import com.veraz.tasks.backend.auth.dto.UserResponseDTO;
import com.veraz.tasks.backend.auth.model.User;
import com.veraz.tasks.backend.auth.service.AuthService;
import com.veraz.tasks.backend.auth.service.UserService;
import com.veraz.tasks.backend.shared.dto.ApiResponseDTO;
import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.util.MessageUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Authentication and authorization endpoints")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("sign-in")
    @Operation(summary = "Sign-in user", description = "Sign-in user with email or username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sign-in successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data")
    })
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> signInUser(@Valid @RequestBody AuthRequestDTO signInRequest) {
        // Service already handles authentication logic
        AuthResponseDTO response = authService.signInUser(signInRequest);
        if (response.getUser() != null && response.getToken() != null) {
            return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, MessageUtils.getMessage(MessageKeys.AUTH_SIGNIN_SUCCESS), response, null));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDTO<>(false, HttpStatus.UNAUTHORIZED, response.getMessage(), null, null));
        }
    }

    @PostMapping("/sign-up")
    @Operation(summary = "Register new user", description = "Creates a new user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> signUpUser(@Valid @RequestBody UserCreateRequestDTO signUpRequest) {
        try {
            UserResponseDTO response = userService.create(signUpRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO<>(true, HttpStatus.CREATED, MessageUtils.getCrudSuccess(MessageKeys.CRUD_CREATED_SUCCESS, "User"), response, null));
        } catch (Exception e) {
            // GlobalExceptionHandler will handle specific exceptions
            throw e;
        }
    }

    @GetMapping("/check-status")
    @Operation(summary = "Check authentication status", description = "Checks if the provided JWT token is valid and returns user details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication status checked successfully. Token is valid."),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> checkAuthStatus(@AuthenticationPrincipal User userPrincipal) {
        AuthResponseDTO response = authService.checkAuthStatus(userPrincipal);
        
        if (response.getUser() != null) {
            return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, MessageUtils.getMessage(MessageKeys.AUTH_SIGNIN_SUCCESS), response, null));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDTO<>(false, HttpStatus.UNAUTHORIZED, response.getMessage(), null, null));
        }
    }
}