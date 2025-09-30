package com.veraz.tasks.backend.infrastructure.business.persistence;

import com.veraz.tasks.backend.domain.business.entities.Employee;
import com.veraz.tasks.backend.domain.business.valueobjects.EmployeeId;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Employee JPA entity
 * 
 * Maps Employee domain entity to database table.
 * Part of the infrastructure layer in Clean Architecture.
 */
@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity {
    
    @Id
    @Column(name = "employees_id", nullable = false)
    private UUID id;
    
    @Column(name = "persons_id", nullable = false)
    private UUID personId;
    
    @Column(name = "position", nullable = false, length = 100)
    private String position;
    
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;
    
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    @Column(name = "salary", nullable = false, precision = 12, scale = 2)
    private BigDecimal salary;
    
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;
    
    @Column(name = "salary_type", nullable = false, length = 20)
    private String salaryType;
    
    // CAMPOS OPCIONALES
    @Column(name = "department", length = 100)
    private String department;
    
    @Column(name = "employment_type", length = 20)
    private String employmentType;
    
    @Column(name = "termination_date")
    private LocalDate terminationDate;
    
    @Column(name = "probation_end_date")
    private LocalDate probationEndDate;
    
    @Column(name = "work_email", length = 100)
    private String workEmail;
    
    @Column(name = "work_phone", length = 20)
    private String workPhone;
    
    @Column(name = "work_location", length = 100)
    private String workLocation;
    
    @Column(name = "work_schedule", length = 100)
    private String workSchedule;
    
    @Column(name = "job_level", length = 20)
    private String jobLevel;
    
    @Column(name = "cost_center", length = 50)
    private String costCenter;
    
    @Column(name = "work_shift", length = 20)
    private String workShift;
    
    @Column(name = "skills", columnDefinition = "TEXT")
    private String skills;
    
    @Column(name = "certifications", columnDefinition = "TEXT")
    private String certifications;
    
    @Column(name = "education", columnDefinition = "TEXT")
    private String education;
    
    @Column(name = "benefits", columnDefinition = "TEXT")
    private String benefits;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "created_by")
    private UUID createdBy;
    
    @Column(name = "updated_by")
    private UUID updatedBy;
    
    public static EmployeeEntity from(Employee employee) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.id = employee.getId().getValue();
        entity.personId = employee.getPersonId().getValue();
        entity.position = employee.getPosition();
        entity.department = employee.getDepartment();
        entity.employmentType = employee.getEmploymentType();
        entity.status = employee.getStatus();
        entity.hireDate = employee.getHireDate();
        entity.terminationDate = employee.getTerminationDate();
        entity.probationEndDate = employee.getProbationEndDate();
        entity.salary = employee.getSalary();
        entity.currency = employee.getCurrency();
        entity.salaryType = employee.getSalaryType();
        entity.workEmail = employee.getWorkEmail();
        entity.workPhone = employee.getWorkPhone();
        entity.workLocation = employee.getWorkLocation();
        entity.workSchedule = employee.getWorkSchedule();
        entity.jobLevel = employee.getJobLevel();
        entity.costCenter = employee.getCostCenter();
        entity.workShift = employee.getWorkShift();
        entity.skills = employee.getSkills();
        entity.certifications = employee.getCertifications();
        entity.education = employee.getEducation();
        entity.benefits = employee.getBenefits();
        entity.notes = employee.getNotes();
        entity.isActive = employee.isActive();
        entity.createdAt = employee.getCreatedAt();
        entity.updatedAt = employee.getUpdatedAt();
        entity.createdBy = employee.getCreatedBy() != null ? employee.getCreatedBy().getValue() : null;
        entity.updatedBy = employee.getUpdatedBy() != null ? employee.getUpdatedBy().getValue() : null;
        return entity;
    }
    
    public Employee toDomain() {
        return Employee.reconstruct(
            EmployeeId.of(this.id),
            PersonId.of(this.personId),
            this.position,
            this.department,
            this.employmentType,
            this.status,
            this.hireDate,
            this.terminationDate,
            this.probationEndDate,
            this.salary,
            this.currency,
            this.salaryType,
            this.workEmail,
            this.workPhone,
            this.workLocation,
            this.workSchedule,
            this.jobLevel,
            this.costCenter,
            this.workShift,
            this.skills,
            this.certifications,
            this.education,
            this.benefits,
            this.notes,
            this.isActive,
            this.createdBy != null ? com.veraz.tasks.backend.domain.identity.valueobjects.UserId.of(this.createdBy) : null,
            this.updatedBy != null ? com.veraz.tasks.backend.domain.identity.valueobjects.UserId.of(this.updatedBy) : null,
            this.createdAt,
            this.updatedAt
        );
    }
}

