package com.veraz.tasks.backend.person.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeUpdateRequestDTO {
    private UUID personId;

    @Size(min = 3, max = 20, message = "{validation.field.size}")
    private String employeeCode;

    @Size(min = 3, max = 100, message = "{validation.field.size}")
    private String position;

    @Size(max = 100, message = "{validation.field.max.length}")
    private String department;

    private UUID supervisorId;

    private LocalDate hireDate;

    private LocalDate terminationDate;

    private BigDecimal salary;

    @Size(max = 3, message = "{validation.field.max.length}")
    private String currency;

    @Size(min = 3, max = 20, message = "{validation.field.size}")
    private String employmentType;

    @Size(max = 20, message = "{validation.field.max.length}")
    private String status;

    @Email(message = "{validation.field.email}")
    @Size(min = 3, max = 100, message = "{validation.field.size}")
    private String workEmail;

    @Size(max = 20, message = "{validation.field.max.length}")
    private String workMobile;

    @Size(max = 100, message = "{validation.field.max.length}")
    private String workLocation;

    @Size(max = 100, message = "{validation.field.max.length}")
    private String workSchedule;

    @Size(max = 500, message = "{validation.field.max.length}")
    private String skills;

    @Size(max = 500, message = "{validation.field.max.length}")
    private String certifications;

    @Size(max = 500, message = "{validation.field.max.length}")
    private String education;

    @Size(max = 500, message = "{validation.field.max.length}")
    private String benefits;

    @Size(max = 500, message = "{validation.field.max.length}")
    private String notes;

    private Boolean isActive;
}