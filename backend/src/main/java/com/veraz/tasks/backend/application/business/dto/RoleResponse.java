package com.veraz.tasks.backend.application.business.dto;

import com.veraz.tasks.backend.domain.identity.entities.Role;

import java.time.LocalDateTime;

/**
 * Response DTO for Role
 * 
 * A Response represents data returned to the client.
 * Contains only information necessary for presentation.
 * 
 * Using Record for immutability and conciseness (Java 14+)
 */
public record RoleResponse(
    String id,
    String name,
    String description,
    boolean isActive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    
    /**
     * Factory method to create a response from a domain entity
     */
    public static RoleResponse from(Role role) {
        return new RoleResponse(
            role.getId().getValueAsString(),
            role.getName(),
            role.getDescription(),
            role.isActive(),
            role.getCreatedAt(),
            role.getUpdatedAt()
        );
    }
}

