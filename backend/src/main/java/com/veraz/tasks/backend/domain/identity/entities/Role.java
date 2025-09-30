package com.veraz.tasks.backend.domain.identity.entities;

import com.veraz.tasks.backend.domain.identity.valueobjects.RoleId;
import com.veraz.tasks.backend.domain.identity.exceptions.InvalidRoleDataException;
import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.util.MessageUtils;

import lombok.Getter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Role domain entity
 */
@Getter
@ToString(exclude = {"description"})
@EqualsAndHashCode(of = "id")
public class Role {
    
    private final RoleId id;
    private String name;
    private String description;
    private boolean isActive;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private Role(RoleId id, String name, String description, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = true;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        
        validateBusinessRules();
    }
    
    public static Role create(String name, String description) {
        return new Role(
            RoleId.generate(),
            name,
            description,
            LocalDateTime.now()
        );
    }
    
    public static Role reconstruct(RoleId id, String name, String description, 
                                 boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        Role role = new Role(
            id,
            name,
            description,
            createdAt
        );
        role.isActive = isActive;
        role.updatedAt = updatedAt;
        return role;
    }
    
    
    private void validateBusinessRules() {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidRoleDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        if (name.length() < 2) {
            throw new InvalidRoleDataException(MessageUtils.getMessage(MessageKeys.VALIDATION_FIELD_MIN_LENGTH, "Role name", 2));
        }
        if (name.length() > 50) {
            throw new InvalidRoleDataException(MessageUtils.getMessage(MessageKeys.VALIDATION_FIELD_MAX_LENGTH, "Role name", 50));
        }
    }
    
    public void updateName(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new InvalidRoleDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        if (newName.length() < 2) {
            throw new InvalidRoleDataException(MessageUtils.getMessage(MessageKeys.VALIDATION_FIELD_MIN_LENGTH, "Role name", 2));
        }
        if (newName.length() > 50) {
            throw new InvalidRoleDataException(MessageUtils.getMessage(MessageKeys.VALIDATION_FIELD_MAX_LENGTH, "Role name", 50));
        }
        this.name = newName;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateDescription(String newDescription) {
        this.description = newDescription;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }
    
    
}

