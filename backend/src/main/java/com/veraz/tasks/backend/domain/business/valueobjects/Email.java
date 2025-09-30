package com.veraz.tasks.backend.domain.business.valueobjects;

import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.util.MessageUtils;
import lombok.Getter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.util.regex.Pattern;

@Getter
@ToString
@EqualsAndHashCode(of = "value")
public class Email {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    private final String value;
    
    private Email(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        
        String trimmedValue = value.trim().toLowerCase();
        
        if (!EMAIL_PATTERN.matcher(trimmedValue).matches()) {
            throw new IllegalArgumentException(MessageUtils.getMessage(MessageKeys.VALIDATION_FIELD_EMAIL));
        }
        
        if (trimmedValue.length() > 100) {
            throw new IllegalArgumentException(MessageUtils.getMessage(MessageKeys.VALIDATION_FIELD_MAX_LENGTH, "Email", 100));
        }
        
        this.value = trimmedValue;
    }
    
    public static Email of(String email) {
        return new Email(email);
    }
    
    public String getDomain() {
        return value.substring(value.indexOf('@') + 1);
    }
    
    public String getLocalPart() {
        return value.substring(0, value.indexOf('@'));
    }
    
    public boolean isFromDomain(String domain) {
        return getDomain().equalsIgnoreCase(domain);
    }
    
}

