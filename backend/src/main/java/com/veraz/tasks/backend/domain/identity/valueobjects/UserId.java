package com.veraz.tasks.backend.domain.identity.valueobjects;

import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.util.MessageUtils;
import lombok.Getter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode(of = "value")
public class UserId {
    
    private final UUID value;
    
    private UserId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_NULL));
        }
        this.value = value;
    }
    
    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }
    
    public static UserId of(UUID uuid) {
        return new UserId(uuid);
    }
    
    public static UserId of(String uuidString) {
        try {
            return new UserId(UUID.fromString(uuidString));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format: " + uuidString, e);
        }
    }
    
    public String getValueAsString() {
        return value.toString();
    }
    
}

