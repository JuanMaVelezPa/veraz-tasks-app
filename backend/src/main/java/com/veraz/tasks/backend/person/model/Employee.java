package com.veraz.tasks.backend.person.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ge_templ")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "empl_empl", nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "empl_pers", referencedColumnName = "pers_pers")
    private Person person;

    @Column(name = "empl_position", nullable = false, length = 100)
    private String position;

    @Column(name = "empl_department", length = 100)
    private String department;

    @Column(name = "empl_employment_type", nullable = false, length = 20)
    private String employmentType;

    @Column(name = "empl_status", nullable = false, length = 20)
    private String status;

    @Column(name = "empl_hire_date", nullable = false)
    private LocalDate hireDate;

    @Column(name = "empl_termination_date")
    private LocalDate terminationDate;

    @Column(name = "empl_probation_end_date")
    private LocalDate probationEndDate;

    @Column(name = "empl_salary", precision = 12, scale = 2)
    private BigDecimal salary;

    @Column(name = "empl_currency", length = 3)
    private String currency;

    @Column(name = "empl_salary_type", length = 20)
    private String salaryType;

    @Column(name = "empl_work_email", length = 100)
    private String workEmail;

    @Column(name = "empl_work_phone", length = 20)
    private String workPhone;

    @Column(name = "empl_work_location", length = 100)
    private String workLocation;

    @Column(name = "empl_work_schedule", length = 100)
    private String workSchedule;

    @Column(name = "empl_job_level", length = 20)
    private String jobLevel;

    @Column(name = "empl_cost_center", length = 50)
    private String costCenter;

    @Column(name = "empl_work_shift", length = 20)
    private String workShift;

    @Column(name = "empl_skills", columnDefinition = "TEXT")
    private String skills;

    @Column(name = "empl_certifications", columnDefinition = "TEXT")
    private String certifications;

    @Column(name = "empl_education", columnDefinition = "TEXT")
    private String education;

    @Column(name = "empl_benefits", columnDefinition = "TEXT")
    private String benefits;

    @Column(name = "empl_notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "empl_is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "empl_created_at")
    private LocalDateTime createdAt;

    @Column(name = "empl_updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "empl_created_by")
    private UUID createdBy;

    @Column(name = "empl_updated_by")
    private UUID updatedBy;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = "ACTIVE";
        }
        if (this.currency == null) {
            this.currency = "USD";
        }
        this.isActive = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (this.workEmail != null) {
            this.workEmail = this.workEmail.toLowerCase().trim();
        }
        if (this.workPhone != null) {
            this.workPhone = this.workPhone.trim();
        }
        if (this.workLocation != null) {
            this.workLocation = this.workLocation.trim();
        }
        if (this.workSchedule != null) {
            this.workSchedule = this.workSchedule.trim();
        }
        if (this.position != null) {
            this.position = this.position.trim();
        }
        if (this.department != null) {
            this.department = this.department.trim();
        }
    }
}
