package com.veraz.tasks.backend.infrastructure.business.dto;

import com.veraz.tasks.backend.shared.constants.MessageKeys;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public record CreateClientRequest(
    @NotNull(message = "{" + MessageKeys.VALIDATION_FIELD_REQUIRED + "}")
    String personId,
    
    @NotBlank(message = "{" + MessageKeys.VALIDATION_FIELD_REQUIRED + "}")
    @Size(max = 20, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String type,
    
    @NotBlank(message = "{" + MessageKeys.VALIDATION_FIELD_REQUIRED + "}")
    @Size(max = 50, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String category,
    
    @NotBlank(message = "{" + MessageKeys.VALIDATION_FIELD_REQUIRED + "}")
    @Size(max = 20, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String status,
    
    @NotNull(message = "{" + MessageKeys.VALIDATION_FIELD_REQUIRED + "}")
    @DecimalMin(value = "0.01", message = "{" + MessageKeys.VALIDATION_FIELD_DECIMAL_MIN + "}")
    BigDecimal creditLimit,
    
    @NotBlank(message = "{" + MessageKeys.VALIDATION_FIELD_REQUIRED + "}")
    @Size(max = 3, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String currency,
    
    @Size(max = 500, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String notes
) {
}


