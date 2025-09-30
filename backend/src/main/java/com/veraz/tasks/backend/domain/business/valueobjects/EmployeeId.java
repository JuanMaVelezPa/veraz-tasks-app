package com.veraz.tasks.backend.domain.business.valueobjects;

import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.util.MessageUtils;
import lombok.Getter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.util.UUID;

/**
 * EmployeeId value object
 * 
 * Represents a unique identifier for an employee.
 * Immutable and self-validating.
 * Follows Clean Architecture principles.
 */
@Getter
@ToString
@EqualsAndHashCode(of = "value")
public class EmployeeId {
    
    private final UUID value;
    
    private EmployeeId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_NULL));
        }
        this.value = value;
    }
    
    public static EmployeeId generate() {
        return new EmployeeId(UUID.randomUUID());
    }
    
    public static EmployeeId of(UUID uuid) {
        return new EmployeeId(uuid);
    }
    
    public static EmployeeId of(String uuidString) {
        try {
            return new EmployeeId(UUID.fromString(uuidString));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format: " + uuidString, e);
        }
    }
    
    public String getValueAsString() {
        return value.toString();
    }
    
    // Lombok generates getValue(), equals(), hashCode(), and toString() automatically
}

