package com.veraz.tasks.backend.application.business.dto;

import com.veraz.tasks.backend.domain.business.entities.Employee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for Employee
 * 
 * A Response represents data returned to the client.
 * Contains only information necessary for presentation.
 * 
 * Using Record for immutability and conciseness (Java 14+)
 */
public record EmployeeResponse(
    String id,
    String personId,
    String position,
    String department,
    String employmentType,
    String status,
    LocalDate hireDate,
    LocalDate terminationDate,
    LocalDate probationEndDate,
    BigDecimal salary,
    String currency,
    String salaryType,
    String workEmail,
    String workPhone,
    String workLocation,
    String workSchedule,
    String jobLevel,
    String costCenter,
    String workShift,
    String skills,
    String certifications,
    String education,
    String benefits,
    String notes,
    boolean isActive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String createdBy,
    String updatedBy
) {
    
    /**
     * Factory method to create a response from a domain entity
     */
    public static EmployeeResponse from(Employee employee) {
        return new EmployeeResponse(
            employee.getId().getValueAsString(),
            employee.getPersonId().getValueAsString(),
            employee.getPosition(),
            employee.getDepartment(),
            employee.getEmploymentType(),
            employee.getStatus(),
            employee.getHireDate(),
            employee.getTerminationDate(),
            employee.getProbationEndDate(),
            employee.getSalary(),
            employee.getCurrency(),
            employee.getSalaryType(),
            employee.getWorkEmail(),
            employee.getWorkPhone(),
            employee.getWorkLocation(),
            employee.getWorkSchedule(),
            employee.getJobLevel(),
            employee.getCostCenter(),
            employee.getWorkShift(),
            employee.getSkills(),
            employee.getCertifications(),
            employee.getEducation(),
            employee.getBenefits(),
            employee.getNotes(),
            employee.isActive(),
            employee.getCreatedAt(),
            employee.getUpdatedAt(),
            employee.getCreatedBy() != null ? employee.getCreatedBy().getValueAsString() : null,
            employee.getUpdatedBy() != null ? employee.getUpdatedBy().getValueAsString() : null
        );
    }
}

