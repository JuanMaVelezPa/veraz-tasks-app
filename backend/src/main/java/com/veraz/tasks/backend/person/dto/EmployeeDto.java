package com.veraz.tasks.backend.person.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDto {
    private UUID id;
    private PersonDto person;
    private String employeeCode;
    private String position;
    private String department;
    private UUID supervisorId;
    private LocalDate hireDate;
    private LocalDate terminationDate;
    private BigDecimal salary;
    private String currency;
    private String employmentType;
    private String status;
    private String workEmail;
    private String workMobile;
    private String workLocation;
    private String workSchedule;
    private String skills;
    private String certifications;
    private String education;
    private String benefits;
    private String notes;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 