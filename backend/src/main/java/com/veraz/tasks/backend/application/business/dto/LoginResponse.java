package com.veraz.tasks.backend.application.business.dto;

import com.veraz.tasks.backend.domain.identity.entities.User;

import java.util.List;

/**
 * Response DTO for login operation
 * 
 * A Response represents data returned to the client after authentication.
 * Contains user information and JWT token.
 * 
 * Using Record for immutability and conciseness (Java 14+)
 */
public record LoginResponse(
    UserResponse user,
    String token,
    String message
) {
    
    /**
     * Factory method to create a login response from a domain entity and token
     */
    public static LoginResponse from(User user, String token, String message) {
        return new LoginResponse(
            UserResponse.from(user),
            token,
            message
        );
    }
    
    /**
     * Factory method to create a login response from a domain entity and token with roles
     */
    public static LoginResponse from(User user, String token, String message, List<String> roleNames) {
        return new LoginResponse(
            UserResponse.from(user, roleNames),
            token,
            message
        );
    }
    
    /**
     * Factory method to create a login response from a UserResponse and token
     */
    public static LoginResponse from(UserResponse userResponse, String token, String message) {
        return new LoginResponse(
            userResponse,
            token,
            message
        );
    }
    
    /**
     * Factory method to create an error login response
     */
    public static LoginResponse error(String message) {
        return new LoginResponse(
            null,
            null,
            message
        );
    }
}

