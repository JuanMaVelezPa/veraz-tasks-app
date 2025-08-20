package com.veraz.tasks.backend.person.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for user association operations
 * Used specifically for linking users with persons through dedicated endpoints
 * This DTO is separate from regular person updates to ensure clear separation of concerns
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonUserAssociationDTO {
    private UUID userId;
}
