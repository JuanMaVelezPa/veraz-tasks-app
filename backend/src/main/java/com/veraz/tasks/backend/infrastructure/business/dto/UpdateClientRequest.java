package com.veraz.tasks.backend.infrastructure.business.dto;

import com.veraz.tasks.backend.shared.constants.MessageKeys;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record UpdateClientRequest(
    @Size(max = 50, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String type,
    
    @Size(max = 50, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String category,
    
    @Size(max = 50, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String source,
    
    @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String companyName,
    
    @Size(max = 255, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String companyWebsite,
    
    @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String companyIndustry,
    
    @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String contactPerson,
    
    @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String contactPosition,
    
    @Size(max = 255, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String address,
    
    @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String city,
    
    @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String country,
    
    @Size(max = 20, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String postalCode,
    
    @Size(max = 50, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String taxId,
    
    @DecimalMin(value = "0.0", message = "{" + MessageKeys.VALIDATION_FIELD_DECIMAL_MIN + "}")
    BigDecimal creditLimit,
    
    @Size(max = 3, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String currency,
    
    @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String paymentTerms,
    
    @Size(max = 50, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String paymentMethod,
    
    String preferences,
    
    String tags,
    
    @Min(value = 1, message = "{" + MessageKeys.VALIDATION_FIELD_DECIMAL_MIN + "}")
    @Max(value = 5, message = "{" + MessageKeys.VALIDATION_FIELD_DECIMAL_MIN + "}")
    Integer rating,
    
    @Size(max = 50, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String status,
    
    String notes,
    
    @DecimalMin(value = "0.0", message = "{" + MessageKeys.VALIDATION_FIELD_DECIMAL_MIN + "}")
    BigDecimal currentBalance,
    
    Boolean isActive
) {
    /**
     * Constructor de conveniencia para casos donde algunos campos son opcionales
     */
    public UpdateClientRequest {
        // Validaciones adicionales si es necesario
        if (type != null && type.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageKeys.VALIDATION_FIELD_NOT_EMPTY);
        }
        if (status != null && status.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageKeys.VALIDATION_FIELD_NOT_EMPTY);
        }
    }
}


