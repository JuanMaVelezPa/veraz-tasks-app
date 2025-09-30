package com.veraz.tasks.backend.infrastructure.business.dto;

import com.veraz.tasks.backend.shared.constants.MessageKeys;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePersonRequest(
    @NotBlank(message = "{" + MessageKeys.VALIDATION_FIELD_REQUIRED + "}")
    @Size(max = 20, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String identType,
    
    @NotBlank(message = "{" + MessageKeys.VALIDATION_FIELD_REQUIRED + "}")
    @Size(max = 20, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String identNumber,
    
    @NotBlank(message = "{" + MessageKeys.VALIDATION_FIELD_REQUIRED + "}")
    @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String firstName,
    
    @NotBlank(message = "{" + MessageKeys.VALIDATION_FIELD_REQUIRED + "}")
    @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String lastName,
    
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
    
    String notes
) {
    // - Constructor compacto con todos los parámetros
    // - Getters (métodos con el mismo nombre del campo)
    // - equals(), hashCode(), toString()
    // - Es inmutable por defecto (no tiene setters)
    
    /**
     * Constructor de conveniencia para casos donde algunos campos son opcionales
     */
    public CreatePersonRequest {
        // Validaciones adicionales si es necesario
        if (identType != null && identType.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageKeys.VALIDATION_FIELD_NOT_EMPTY);
        }
        if (identNumber != null && identNumber.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageKeys.VALIDATION_FIELD_NOT_EMPTY);
        }
    }
}


