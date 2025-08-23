package com.veraz.tasks.backend.person.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for employee responses
 * Contains all employment information for API responses
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {
    private UUID id;
    private UUID personId;
    private String position;
    private String department;
    private String employmentType;
    private String status;
    private LocalDate hireDate;
    private LocalDate terminationDate;
    private LocalDate probationEndDate;
    private BigDecimal salary;
    private String currency;
    private String salaryType;
    private String workEmail;
    private String workPhone;
    private String workLocation;
    private String workSchedule;
    private String jobLevel;
    private String costCenter;
    private String workShift;
    private String skills;
    private String certifications;
    private String education;
    private String benefits;
    private String notes;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
}

