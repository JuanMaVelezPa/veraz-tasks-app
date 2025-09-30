package com.veraz.tasks.backend.infrastructure.business.dto;

import com.veraz.tasks.backend.shared.constants.MessageKeys;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for updating an employee - all fields are optional for partial updates
 */
public record UpdateEmployeeRequest(
    @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String position,
    
    @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String department,
    
    @Size(max = 50, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String employmentType,
    
    @Size(max = 50, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String status,
    
    LocalDate hireDate,
    
    LocalDate terminationDate,
    
    LocalDate probationEndDate,
    
    @DecimalMin(value = "0.0", message = "{" + MessageKeys.VALIDATION_FIELD_DECIMAL_MIN + "}")
    BigDecimal salary,
    
    @Size(max = 3, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String currency,
    
    @Size(max = 50, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String salaryType,
    
    @Email(message = "{" + MessageKeys.VALIDATION_FIELD_EMAIL + "}")
    @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String workEmail,
    
    @Size(max = 20, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String workPhone,
    
    @Size(max = 255, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String workLocation,
    
    @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String workSchedule,
    
    @Size(max = 50, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String jobLevel,
    
    @Size(max = 50, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String costCenter,
    
    @Size(max = 50, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}")
    String workShift,
    
    String skills,
    
    String certifications,
    
    String education,
    
    String benefits,
    
    String notes,
    
    Boolean isActive
) {
    /**
     * Constructor de conveniencia para casos donde algunos campos son opcionales
     */
    public UpdateEmployeeRequest {
        // Validaciones adicionales si es necesario
        if (position != null && position.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageKeys.VALIDATION_FIELD_NOT_EMPTY);
        }
        if (department != null && department.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageKeys.VALIDATION_FIELD_NOT_EMPTY);
        }
        if (status != null && status.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageKeys.VALIDATION_FIELD_NOT_EMPTY);
        }
    }
}


