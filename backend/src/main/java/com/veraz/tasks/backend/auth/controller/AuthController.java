package com.veraz.tasks.backend.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veraz.tasks.backend.auth.dto.LoginRequestDTO;
import com.veraz.tasks.backend.auth.dto.UserRequestDTO;
import com.veraz.tasks.backend.auth.dto.UserResponseDTO;
import com.veraz.tasks.backend.auth.model.User;
import com.veraz.tasks.backend.auth.service.AuthService;
import com.veraz.tasks.backend.auth.service.UserService;

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

    @PostMapping("login")
    @Operation(summary = "Login user", description = "Login user with email or username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data")
    })
    public ResponseEntity<UserResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        UserResponseDTO response = authService.loginUser(loginRequest);
        if (response.getUser() == null || response.getToken() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Register user", description = "Register user with email, username, first name, last name, password and active")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data or user already exists"),
            @ApiResponse(responseCode = "409", description = "Conflict - User already exists")
    })
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRequestDTO user) {
        UserResponseDTO response = userService.createUser(user);
        if (response.getUser() == null) {
            if (response.getMessage() != null && response.getMessage().contains("already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/check-status")
    @Operation(summary = "Check authentication status", description = "Checks if the provided JWT token is valid and returns user details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication status checked successfully. Token is valid."),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponseDTO> checkAuthStatus(@AuthenticationPrincipal User userPrincipal) {
        UserResponseDTO response = authService.checkAuthStatus(userPrincipal);
        if (response.getUser() == null) {
            if (response.getMessage() != null && response.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.ok(response);
    }

}