package com.veraz.tasks.backend.person.mapper;

import org.springframework.stereotype.Component;

import com.veraz.tasks.backend.person.dto.EmployeeCreateRequestDTO;
import com.veraz.tasks.backend.person.dto.EmployeeResponseDTO;
import com.veraz.tasks.backend.person.dto.EmployeeUpdateRequestDTO;
import com.veraz.tasks.backend.person.model.Employee;
import com.veraz.tasks.backend.person.model.Person;

/**
 * Mapper for converting between Employee entities and DTOs
 * Provides methods for entity-DTO transformations
 */
@Component
public class EmployeeMapper {

    /**
     * Convert EmployeeCreateRequestDTO to Employee entity
     * 
     * @param dto the create request DTO
     * @param person the associated person
     * @return Employee entity
     */
    public Employee toEntity(EmployeeCreateRequestDTO dto, Person person) {
        return Employee.builder()
                .person(person)
                .employeeCode(dto.getEmployeeCode())
                .position(dto.getPosition())
                .department(dto.getDepartment())
                .employmentType(dto.getEmploymentType())
                .status(dto.getStatus() != null ? dto.getStatus() : "ACTIVE")
                .hireDate(dto.getHireDate())
                .terminationDate(dto.getTerminationDate())
                .probationEndDate(dto.getProbationEndDate())
                .salary(dto.getSalary())
                .currency(dto.getCurrency() != null ? dto.getCurrency() : "USD")
                .salaryType(dto.getSalaryType())
                .workEmail(dto.getWorkEmail())
                .workPhone(dto.getWorkPhone())
                .workLocation(dto.getWorkLocation())
                .workSchedule(dto.getWorkSchedule())
                .jobLevel(dto.getJobLevel())
                .costCenter(dto.getCostCenter())
                .workShift(dto.getWorkShift())
                .skills(dto.getSkills())
                .certifications(dto.getCertifications())
                .education(dto.getEducation())
                .benefits(dto.getBenefits())
                .notes(dto.getNotes())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();
    }

    /**
     * Update Employee entity with EmployeeUpdateRequestDTO
     * 
     * @param entity the existing employee entity
     * @param dto the update request DTO
     * @return updated Employee entity
     */
    public Employee updateEntity(Employee entity, EmployeeUpdateRequestDTO dto) {
        if (dto.getEmployeeCode() != null) {
            entity.setEmployeeCode(dto.getEmployeeCode());
        }
        if (dto.getPosition() != null) {
            entity.setPosition(dto.getPosition());
        }
        if (dto.getDepartment() != null) {
            entity.setDepartment(dto.getDepartment());
        }
        if (dto.getEmploymentType() != null) {
            entity.setEmploymentType(dto.getEmploymentType());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        if (dto.getHireDate() != null) {
            entity.setHireDate(dto.getHireDate());
        }
        if (dto.getTerminationDate() != null) {
            entity.setTerminationDate(dto.getTerminationDate());
        }
        if (dto.getProbationEndDate() != null) {
            entity.setProbationEndDate(dto.getProbationEndDate());
        }
        if (dto.getSalary() != null) {
            entity.setSalary(dto.getSalary());
        }
        if (dto.getCurrency() != null) {
            entity.setCurrency(dto.getCurrency());
        }
        if (dto.getSalaryType() != null) {
            entity.setSalaryType(dto.getSalaryType());
        }
        if (dto.getWorkEmail() != null) {
            entity.setWorkEmail(dto.getWorkEmail());
        }
        if (dto.getWorkPhone() != null) {
            entity.setWorkPhone(dto.getWorkPhone());
        }
        if (dto.getWorkLocation() != null) {
            entity.setWorkLocation(dto.getWorkLocation());
        }
        if (dto.getWorkSchedule() != null) {
            entity.setWorkSchedule(dto.getWorkSchedule());
        }
        if (dto.getJobLevel() != null) {
            entity.setJobLevel(dto.getJobLevel());
        }
        if (dto.getCostCenter() != null) {
            entity.setCostCenter(dto.getCostCenter());
        }
        if (dto.getWorkShift() != null) {
            entity.setWorkShift(dto.getWorkShift());
        }
        if (dto.getSkills() != null) {
            entity.setSkills(dto.getSkills());
        }
        if (dto.getCertifications() != null) {
            entity.setCertifications(dto.getCertifications());
        }
        if (dto.getEducation() != null) {
            entity.setEducation(dto.getEducation());
        }
        if (dto.getBenefits() != null) {
            entity.setBenefits(dto.getBenefits());
        }
        if (dto.getNotes() != null) {
            entity.setNotes(dto.getNotes());
        }
        if (dto.getIsActive() != null) {
            entity.setIsActive(dto.getIsActive());
        }
        return entity;
    }

    /**
     * Convert Employee entity to EmployeeResponseDTO
     * 
     * @param employee the employee entity
     * @return EmployeeResponseDTO
     */
    public EmployeeResponseDTO toResponseDTO(Employee employee) {
        if (employee == null) {
            return null;
        }
        
        return EmployeeResponseDTO.builder()
                .id(employee.getId())
                .personId(employee.getPerson().getId())
                .employeeCode(employee.getEmployeeCode())
                .position(employee.getPosition())
                .department(employee.getDepartment())
                .employmentType(employee.getEmploymentType())
                .status(employee.getStatus())
                .hireDate(employee.getHireDate())
                .terminationDate(employee.getTerminationDate())
                .probationEndDate(employee.getProbationEndDate())
                .salary(employee.getSalary())
                .currency(employee.getCurrency())
                .salaryType(employee.getSalaryType())
                .workEmail(employee.getWorkEmail())
                .workPhone(employee.getWorkPhone())
                .workLocation(employee.getWorkLocation())
                .workSchedule(employee.getWorkSchedule())
                .jobLevel(employee.getJobLevel())
                .costCenter(employee.getCostCenter())
                .workShift(employee.getWorkShift())
                .skills(employee.getSkills())
                .certifications(employee.getCertifications())
                .education(employee.getEducation())
                .benefits(employee.getBenefits())
                .notes(employee.getNotes())
                .isActive(employee.getIsActive())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .createdBy(employee.getCreatedBy())
                .updatedBy(employee.getUpdatedBy())
                .build();
    }
}

