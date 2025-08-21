package com.veraz.tasks.backend.person.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for updating existing employees
 * Contains optional fields for partial updates
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeUpdateRequestDTO {
    
    @Size(min = 3, max = 20, message = "{validation.field.size}")
    private String employeeCode;

    @Size(min = 3, max = 100, message = "{validation.field.size}")
    private String position;

    @Size(max = 100, message = "{validation.field.max.length}")
    private String department;

    @Size(min = 3, max = 20, message = "{validation.field.size}")
    private String employmentType;

    @Size(max = 20, message = "{validation.field.max.length}")
    private String status;

    private LocalDate hireDate;

    private LocalDate terminationDate;

    private LocalDate probationEndDate;

    private BigDecimal salary;

    @Size(max = 3, message = "{validation.field.max.length}")
    private String currency;

    @Size(max = 20, message = "{validation.field.max.length}")
    private String salaryType;

    @Email(message = "{validation.field.email}")
    @Size(min = 3, max = 100, message = "{validation.field.size}")
    private String workEmail;

    @Size(max = 20, message = "{validation.field.max.length}")
    private String workPhone;

    @Size(max = 100, message = "{validation.field.max.length}")
    private String workLocation;

    @Size(max = 100, message = "{validation.field.max.length}")
    private String workSchedule;

    @Size(max = 20, message = "{validation.field.max.length}")
    private String jobLevel;

    @Size(max = 50, message = "{validation.field.max.length}")
    private String costCenter;

    @Size(max = 20, message = "{validation.field.max.length}")
    private String workShift;

    private String skills;
    
    private String certifications;
    
    private String education;
    
    private String benefits;
    
    private String notes;

    private Boolean isActive;
}

