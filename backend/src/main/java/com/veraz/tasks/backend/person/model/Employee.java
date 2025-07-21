package com.veraz.tasks.backend.person.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ge_templ")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "empl_empl", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empl_pers", nullable = false)
    private Person person;

    @Column(name = "empl_employee_code", nullable = false, length = 20)
    private String employeeCode;

    @Column(name = "empl_position", nullable = false, length = 100)
    private String position;

    @Column(name = "empl_department", length = 100)
    private String department;

    @Column(name = "empl_supervisor")
    private UUID supervisorId;

    @Column(name = "empl_hire_date", nullable = false)
    private LocalDate hireDate;

    @Column(name = "empl_termination_date")
    private LocalDate terminationDate;

    @Column(name = "empl_salary")
    private BigDecimal salary;

    @Column(name = "empl_currency", length = 3)
    private String currency;

    @Column(name = "empl_employment_type", nullable = false, length = 20)
    private String employmentType;

    @Column(name = "empl_status", nullable = false, length = 20)
    private String status;

    @Column(name = "empl_work_email", length = 100)
    private String workEmail;

    @Column(name = "empl_work_mobile", length = 20)
    private String workMobile;

    @Column(name = "empl_work_location", length = 100)
    private String workLocation;

    @Column(name = "empl_work_schedule", length = 100)
    private String workSchedule;

    @Column(name = "empl_skills")
    private String skills;

    @Column(name = "empl_certifications")
    private String certifications;

    @Column(name = "empl_education")
    private String education;

    @Column(name = "empl_benefits")
    private String benefits;

    @Column(name = "empl_notes")
    private String notes;

    @Column(name = "empl_is_active")
    private Boolean isActive;

    @Column(name = "empl_created_at")
    private LocalDateTime createdAt;

    @Column(name = "empl_updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
        this.isActive = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (this.employeeCode != null) {
            this.employeeCode = this.employeeCode.trim();
        }
    }
}