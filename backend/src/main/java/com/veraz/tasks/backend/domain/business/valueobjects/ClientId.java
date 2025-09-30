package com.veraz.tasks.backend.domain.business.valueobjects;

import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.util.MessageUtils;
import lombok.Getter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode(of = "value")
public class ClientId {
    
    private final UUID value;
    
    private ClientId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_NULL));
        }
        this.value = value;
    }
    
    public static ClientId generate() {
        return new ClientId(UUID.randomUUID());
    }
    
    public static ClientId of(UUID uuid) {
        return new ClientId(uuid);
    }
    
    public static ClientId of(String uuidString) {
        try {
            return new ClientId(UUID.fromString(uuidString));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format: " + uuidString, e);
        }
    }
    
    public String getValueAsString() {
        return value.toString();
    }
    
}

