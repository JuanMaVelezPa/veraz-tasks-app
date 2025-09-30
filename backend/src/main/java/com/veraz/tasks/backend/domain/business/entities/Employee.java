package com.veraz.tasks.backend.domain.business.entities;

import com.veraz.tasks.backend.domain.business.valueobjects.EmployeeId;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;
import com.veraz.tasks.backend.domain.identity.valueobjects.UserId;
import com.veraz.tasks.backend.domain.business.exceptions.InvalidEmployeeDataException;
import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.util.MessageUtils;

import lombok.Getter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString(exclude = { "notes" })
@EqualsAndHashCode(of = "id")
public class Employee {

    private final EmployeeId id;
    private final PersonId personId;
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
    private boolean isActive;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserId createdBy;
    private UserId updatedBy;

    private Employee(EmployeeId id, PersonId personId, String position, String department,
            String employmentType, String status, LocalDate hireDate, LocalDate terminationDate,
            LocalDate probationEndDate, BigDecimal salary, String currency, String salaryType,
            String workEmail, String workPhone, String workLocation, String workSchedule,
            String jobLevel, String costCenter, String workShift, String skills,
            String certifications, String education, String benefits, String notes,
            UserId createdBy, UserId updatedBy, LocalDateTime createdAt) {
        this.id = id;
        this.personId = personId;
        this.position = position;
        this.department = department;
        this.employmentType = employmentType;
        this.status = status;
        this.hireDate = hireDate;
        this.terminationDate = terminationDate;
        this.probationEndDate = probationEndDate;
        this.salary = salary;
        this.currency = currency;
        this.salaryType = salaryType;
        this.workEmail = workEmail;
        this.workPhone = workPhone;
        this.workLocation = workLocation;
        this.workSchedule = workSchedule;
        this.jobLevel = jobLevel;
        this.costCenter = costCenter;
        this.workShift = workShift;
        this.skills = skills;
        this.certifications = certifications;
        this.education = education;
        this.benefits = benefits;
        this.notes = notes;
        this.isActive = true;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;

        validateBusinessRules();
    }

    public static Employee create(PersonId personId, LocalDate hireDate, String position,
            String status, BigDecimal salary, String currency, String salaryType) {
        return new Employee(
                EmployeeId.generate(),
                personId,
                position,
                null, // department - opcional
                null, // employmentType - opcional
                status,
                hireDate,
                null, // terminationDate - opcional
                null, // probationEndDate - opcional
                salary,
                currency,
                salaryType,
                null, // workEmail - opcional
                null, // workPhone - opcional
                null, // workLocation - opcional
                null, // workSchedule - opcional
                null, // jobLevel - opcional
                null, // costCenter - opcional
                null, // workShift - opcional
                null, // skills - opcional
                null, // certifications - opcional
                null, // education - opcional
                null, // benefits - opcional
                null, // notes - opcional
                null, // createdBy - opcional
                null, // updatedBy - opcional
                LocalDateTime.now());
    }

    public static Employee reconstruct(EmployeeId id, PersonId personId, String position, String department,
            String employmentType, String status, LocalDate hireDate, LocalDate terminationDate,
            LocalDate probationEndDate, BigDecimal salary, String currency, String salaryType,
            String workEmail, String workPhone, String workLocation, String workSchedule,
            String jobLevel, String costCenter, String workShift, String skills,
            String certifications, String education, String benefits, String notes,
            boolean isActive, UserId createdBy, UserId updatedBy,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        Employee employee = new Employee(
                id,
                personId,
                position,
                department,
                employmentType,
                status,
                hireDate,
                terminationDate,
                probationEndDate,
                salary,
                currency,
                salaryType,
                workEmail,
                workPhone,
                workLocation,
                workSchedule,
                jobLevel,
                costCenter,
                workShift,
                skills,
                certifications,
                education,
                benefits,
                notes,
                createdBy,
                updatedBy,
                createdAt);
        employee.isActive = isActive;
        employee.updatedAt = updatedAt;
        return employee;
    }

    private void validateBusinessRules() {
        // VALIDACIONES PARA CAMPOS OBLIGATORIOS
        if (personId == null) {
            throw new InvalidEmployeeDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_NULL));
        }
        if (hireDate == null) {
            throw new InvalidEmployeeDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_NULL));
        }
        if (position == null || position.trim().isEmpty()) {
            throw new InvalidEmployeeDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        if (status == null || status.trim().isEmpty()) {
            throw new InvalidEmployeeDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        if (salary == null) {
            throw new InvalidEmployeeDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_NULL));
        }
        if (salary.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidEmployeeDataException(
                    MessageUtils.getMessage(MessageKeys.VALIDATION_FIELD_DECIMAL_MIN, "Salary"));
        }
        if (currency == null || currency.trim().isEmpty()) {
            throw new InvalidEmployeeDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        if (salaryType == null || salaryType.trim().isEmpty()) {
            throw new InvalidEmployeeDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        if (terminationDate != null && hireDate != null && terminationDate.isBefore(hireDate)) {
            throw new InvalidEmployeeDataException(
                    MessageUtils.getMessage(MessageKeys.DOMAIN_DATE_BEFORE_OTHER, "Termination date", "hire date"));
        }
        if (probationEndDate != null && hireDate != null && probationEndDate.isBefore(hireDate)) {
            throw new InvalidEmployeeDataException(
                    MessageUtils.getMessage(MessageKeys.DOMAIN_DATE_BEFORE_OTHER, "Probation end date", "hire date"));
        }
    }

    public void updatePosition(String newPosition) {
        if (newPosition == null || newPosition.trim().isEmpty()) {
            throw new InvalidEmployeeDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        this.position = newPosition;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDepartment(String newDepartment) {
        // Department es opcional, puede ser null
        this.department = newDepartment;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateSalary(BigDecimal newSalary) {
        if (newSalary == null) {
            throw new InvalidEmployeeDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_NULL));
        }
        if (newSalary.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidEmployeeDataException(
                    MessageUtils.getMessage(MessageKeys.VALIDATION_FIELD_DECIMAL_MIN, "Salary"));
        }
        this.salary = newSalary;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(String newStatus) {
        if (newStatus == null || newStatus.trim().isEmpty()) {
            throw new InvalidEmployeeDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateNotes(String newNotes) {
        this.notes = newNotes;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateEmploymentType(String newEmploymentType) {
        // EmploymentType es opcional, puede ser null
        this.employmentType = newEmploymentType;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateSalary(BigDecimal newSalary, String newCurrency, String newSalaryType) {
        if (newSalary == null) {
            throw new InvalidEmployeeDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_NULL));
        }
        if (newSalary.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidEmployeeDataException(
                    MessageUtils.getMessage(MessageKeys.VALIDATION_FIELD_DECIMAL_MIN, "Salary"));
        }
        if (newCurrency == null || newCurrency.trim().isEmpty()) {
            throw new InvalidEmployeeDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        if (newSalaryType == null || newSalaryType.trim().isEmpty()) {
            throw new InvalidEmployeeDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        this.salary = newSalary;
        this.currency = newCurrency;
        this.salaryType = newSalaryType;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateWorkContact(String newWorkEmail, String newWorkPhone) {
        this.workEmail = newWorkEmail;
        this.workPhone = newWorkPhone;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateWorkLocation(String newWorkLocation, String newWorkSchedule, String newWorkShift) {
        this.workLocation = newWorkLocation;
        this.workSchedule = newWorkSchedule;
        this.workShift = newWorkShift;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateJobDetails(String newJobLevel, String newCostCenter) {
        this.jobLevel = newJobLevel;
        this.costCenter = newCostCenter;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateSkillsAndEducation(String newSkills, String newCertifications, String newEducation) {
        this.skills = newSkills;
        this.certifications = newCertifications;
        this.education = newEducation;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateBenefits(String newBenefits) {
        this.benefits = newBenefits;
        this.updatedAt = LocalDateTime.now();
    }

    public void terminate(LocalDate terminationDate) {
        if (terminationDate == null) {
            throw new InvalidEmployeeDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_NULL));
        }
        if (hireDate != null && terminationDate.isBefore(hireDate)) {
            throw new InvalidEmployeeDataException(
                    MessageUtils.getMessage(MessageKeys.DOMAIN_DATE_BEFORE_OTHER, "Termination date", "hire date"));
        }
        this.terminationDate = terminationDate;
        this.status = "TERMINATED";
        this.updatedAt = LocalDateTime.now();
    }

    public void updateHireDate(LocalDate newHireDate) {
        if (newHireDate == null) {
            throw new InvalidEmployeeDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_NULL));
        }
        if (terminationDate != null && newHireDate.isAfter(terminationDate)) {
            throw new InvalidEmployeeDataException(
                    MessageUtils.getMessage(MessageKeys.DOMAIN_DATE_AFTER_OTHER, "Hire date", "termination date"));
        }
        this.hireDate = newHireDate;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateTerminationDate(LocalDate newTerminationDate) {
        if (newTerminationDate != null && hireDate != null && newTerminationDate.isBefore(hireDate)) {
            throw new InvalidEmployeeDataException(
                    MessageUtils.getMessage(MessageKeys.DOMAIN_DATE_BEFORE_OTHER, "Termination date", "hire date"));
        }
        this.terminationDate = newTerminationDate;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateProbationEndDate(LocalDate newProbationEndDate) {
        if (newProbationEndDate != null && hireDate != null && newProbationEndDate.isBefore(hireDate)) {
            throw new InvalidEmployeeDataException(
                    MessageUtils.getMessage(MessageKeys.DOMAIN_DATE_BEFORE_OTHER, "Probation end date", "hire date"));
        }
        this.probationEndDate = newProbationEndDate;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

}
