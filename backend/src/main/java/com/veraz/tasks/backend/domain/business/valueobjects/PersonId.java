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
public class PersonId {
    
    private final UUID value;
    
    private PersonId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_NULL));
        }
        this.value = value;
    }
    
    public static PersonId generate() {
        return new PersonId(UUID.randomUUID());
    }
    
    public static PersonId of(UUID uuid) {
        return new PersonId(uuid);
    }
    
    public static PersonId of(String uuidString) {
        try {
            return new PersonId(UUID.fromString(uuidString));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format: " + uuidString, e);
        }
    }
    
    public String getValueAsString() {
        return value.toString();
    }
    
}

