package com.veraz.tasks.backend.domain.identity.valueobjects;

import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.util.MessageUtils;
import lombok.Getter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.util.UUID;

/**
 * RoleId value object
 * 
 * Represents a unique identifier for a role.
 * Immutable and self-validating.
 * Follows Clean Architecture principles.
 */
@Getter
@ToString
@EqualsAndHashCode(of = "value")
public class RoleId {
    
    private final UUID value;
    
    private RoleId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_NULL));
        }
        this.value = value;
    }
    
    public static RoleId generate() {
        return new RoleId(UUID.randomUUID());
    }
    
    public static RoleId of(UUID uuid) {
        return new RoleId(uuid);
    }
    
    public static RoleId of(String uuidString) {
        try {
            return new RoleId(UUID.fromString(uuidString));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format: " + uuidString, e);
        }
    }
    
    public String getValueAsString() {
        return value.toString();
    }
    
    // Lombok generates getValue(), equals(), hashCode(), and toString() automatically
}

