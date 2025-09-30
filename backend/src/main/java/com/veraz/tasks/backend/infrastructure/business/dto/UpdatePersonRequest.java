package com.veraz.tasks.backend.infrastructure.business.dto;

import com.veraz.tasks.backend.shared.constants.MessageKeys;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * DTO for updating a person - all fields are optional for partial updates
 */
public record UpdatePersonRequest(
    @Size(max = 20, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String identType,
    
    @Size(max = 20, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String identNumber,
    
    @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String firstName,
    
    @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String lastName,
    
    LocalDate birthDate,
    
    @Size(max = 3, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String gender,
    
    @Size(max = 50, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String nationality,
    
    @Email(message = "{" + MessageKeys.VALIDATION_FIELD_EMAIL + "}")
    @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String email,
    
    @Size(max = 20, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String mobile,
    
    @Size(max = 255, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String address,
    
    @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String city,
    
    @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String country,
    
    @Size(max = 20, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String postalCode,
    
    String notes,
    
    Boolean isActive
) {
    /**
     * Constructor de conveniencia para casos donde algunos campos son opcionales
     */
    public UpdatePersonRequest {
        // Validaciones adicionales si es necesario
        if (identType != null && identType.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageKeys.VALIDATION_FIELD_NOT_EMPTY);
        }
        if (identNumber != null && identNumber.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageKeys.VALIDATION_FIELD_NOT_EMPTY);
        }
    }
}


