package com.veraz.tasks.backend.application.business.dto;

import com.veraz.tasks.backend.domain.identity.entities.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for User
 * 
 * A Response represents data returned to the client.
 * Contains only information necessary for presentation.
 * 
 * Using Record for immutability and conciseness (Java 14+)
 */
public record UserResponse(
    String id,
    String username,
    String email,
    boolean isActive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<String> roles
) {
    
    /**
     * Factory method to create a response from a domain entity
     */
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId().getValueAsString(),
            user.getUsername(),
            user.getEmail() != null ? user.getEmail().getValue() : null,
            user.isActive(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            List.of() // Empty roles for now, will be populated by use cases
        );
    }
    
    /**
     * Factory method to create a response from a domain entity with roles
     */
    public static UserResponse from(User user, List<String> roleNames) {
        return new UserResponse(
            user.getId().getValueAsString(),
            user.getUsername(),
            user.getEmail() != null ? user.getEmail().getValue() : null,
            user.isActive(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            roleNames
        );
    }
}

