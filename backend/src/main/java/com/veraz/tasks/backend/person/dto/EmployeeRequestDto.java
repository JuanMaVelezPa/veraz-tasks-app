package com.veraz.tasks.backend.person.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class EmployeeRequestDto {
    @NotNull(message = "{NotNull.employee.personId}")
    private UUID personId;

    @NotBlank(message = "{NotBlank.employee.employeeCode}")
    @Size(min = 3, max = 20, message = "{Size.employee.employeeCode.min} {Size.employee.employeeCode.max}")
    private String employeeCode;

    @NotBlank(message = "{NotBlank.employee.position}")
    @Size(min = 3, max = 100, message = "{Size.employee.position.min} {Size.employee.position.max}")
    private String position;

    @Size(max = 100, message = "{Size.employee.department}")
    private String department;

    private UUID supervisorId;

    @NotNull(message = "{NotNull.employee.hireDate}")
    private LocalDate hireDate;

    private LocalDate terminationDate;

    private BigDecimal salary;

    @Size(max = 3, message = "{Size.employee.currency}")
    private String currency;

    @NotBlank(message = "{NotBlank.employee.employmentType}")
    @Size(min = 3, max = 20, message = "{Size.employee.employmentType.min} {Size.employee.employmentType.max}")
    private String employmentType;

    @Size(max = 20, message = "{Size.employee.status}")
    private String status;

    @Email(message = "{Email.employee.workEmail}")
    @Size(min = 3, max = 100, message = "{Size.employee.workEmail.min} {Size.employee.workEmail.max}")
    private String workEmail;

    @Size(max = 20, message = "{Size.employee.workMobile}")
    private String workMobile;

    @Size(max = 100, message = "{Size.employee.workLocation}")
    private String workLocation;

    @Size(max = 100, message = "{Size.employee.workSchedule}")
    private String workSchedule;

    @Size(max = 500, message = "{Size.employee.skills}")
    private String skills;
    
    @Size(max = 500, message = "{Size.employee.certifications}")
    private String certifications;
    
    @Size(max = 500, message = "{Size.employee.education}")
    private String education;
    
    @Size(max = 500, message = "{Size.employee.benefits}")
    private String benefits;
    
    @Size(max = 500, message = "{Size.employee.notes}")
    private String notes;
} 