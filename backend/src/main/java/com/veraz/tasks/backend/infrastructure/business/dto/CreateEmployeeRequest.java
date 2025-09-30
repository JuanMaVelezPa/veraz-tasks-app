package com.veraz.tasks.backend.infrastructure.business.dto;

import com.veraz.tasks.backend.shared.constants.MessageKeys;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateEmployeeRequest(
        // CAMPOS OBLIGATORIOS
        @NotNull(message = "{" + MessageKeys.VALIDATION_FIELD_REQUIRED + "}") String personId,

        @NotNull(message = "{" + MessageKeys.VALIDATION_FIELD_REQUIRED + "}") LocalDate hireDate,

        @NotBlank(message = "{" + MessageKeys.VALIDATION_FIELD_REQUIRED + "}") @Size(max = 100, message = "{"
                + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}") String position,

        @NotBlank(message = "{" + MessageKeys.VALIDATION_FIELD_REQUIRED + "}") @Size(max = 20, message = "{"
                + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}") String status,

        @NotNull(message = "{" + MessageKeys.VALIDATION_FIELD_REQUIRED + "}") 
        @DecimalMin(value = "0.01", message = "{" + MessageKeys.VALIDATION_FIELD_DECIMAL_MIN + "}") 
        BigDecimal salary,

        @NotBlank(message = "{" + MessageKeys.VALIDATION_FIELD_REQUIRED + "}") @Size(max = 3, message = "{"
                + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}") String currency,

        @NotBlank(message = "{" + MessageKeys.VALIDATION_FIELD_REQUIRED + "}") @Size(max = 20, message = "{"
                + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}") String salaryType,

        // CAMPOS OPCIONALES
        @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}") String department,

        @Size(max = 20, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}") String employmentType,

        LocalDate terminationDate,

        LocalDate probationEndDate,

        @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}") String workEmail,

        @Size(max = 20, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}") String workPhone,

        @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}") String workLocation,

        @Size(max = 100, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}") String workSchedule,

        @Size(max = 20, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}") String jobLevel,

        @Size(max = 50, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}") String costCenter,

        @Size(max = 20, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}") String workShift,

        @Size(max = 1000, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}") String skills,

        @Size(max = 1000, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}") String certifications,

        @Size(max = 1000, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}") String education,

        @Size(max = 1000, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}") String benefits,

        @Size(max = 500, message = "{" + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}") String notes) {
}
