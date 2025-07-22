package com.veraz.tasks.backend.auth.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veraz.tasks.backend.auth.dto.UserResponseDTO;
import com.veraz.tasks.backend.auth.dto.UserUpdateDTO;
import com.veraz.tasks.backend.auth.service.UserService;

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
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user", description = "Get user with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR')")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID id) {
        UserResponseDTO response = userService.getUserByID(id);
        
        // Si no hay usuario, es un error 404
        if (response.getUser() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update user", description = "Update user with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        UserResponseDTO response = userService.patchUser(id, userUpdateDTO);
        
        // Si no hay usuario, es un error 404
        if (response.getUser() == null) {
            if (response.getMessage() != null && response.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete user with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> deleteUser(@PathVariable UUID id) {
        UserResponseDTO response = userService.deleteUser(id);
        
        // Si no hay usuario, es un error 404
        if (response.getUser() == null) {
            if (response.getMessage() != null && response.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

}
