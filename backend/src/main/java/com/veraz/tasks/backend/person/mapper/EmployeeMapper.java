package com.veraz.tasks.backend.person.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import com.veraz.tasks.backend.person.dto.EmployeeCreateRequestDTO;
import com.veraz.tasks.backend.person.dto.EmployeeUpdateRequestDTO;
import com.veraz.tasks.backend.person.dto.EmployeeResponseDTO;
import com.veraz.tasks.backend.person.model.Employee;
import com.veraz.tasks.backend.person.model.Person;

public class EmployeeMapper {

    /**
     * Converts Employee entity to EmployeeResponseDTO
     * @param employee Employee entity
     * @return EmployeeResponseDTO
     */
    public static EmployeeResponseDTO toDto(Employee employee) {
        if (employee == null) return null;
        
        return EmployeeResponseDTO.builder()
                .id(employee.getId())
                .personId(employee.getPerson().getId())
                .employeeCode(employee.getEmployeeCode())
                .position(employee.getPosition())
                .department(employee.getDepartment())
                .supervisorId(employee.getSupervisorId())
                .hireDate(employee.getHireDate())
                .terminationDate(employee.getTerminationDate())
                .salary(employee.getSalary())
                .currency(employee.getCurrency())
                .employmentType(employee.getEmploymentType())
                .status(employee.getStatus())
                .workEmail(employee.getWorkEmail())
                .workMobile(employee.getWorkMobile())
                .workLocation(employee.getWorkLocation())
                .workSchedule(employee.getWorkSchedule())
                .skills(employee.getSkills())
                .certifications(employee.getCertifications())
                .education(employee.getEducation())
                .benefits(employee.getBenefits())
                .notes(employee.getNotes())
                .isActive(employee.getIsActive())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }

    /**
     * Converts EmployeeCreateRequestDTO to Employee entity for creation
     * @param employeeRequest Create request DTO
     * @param person Person entity
     * @return Employee entity
     */
    public static Employee toEntity(EmployeeCreateRequestDTO employeeRequest, Person person) {
        if (employeeRequest == null) return null;
        
        return Employee.builder()
                .person(person)
                .employeeCode(employeeRequest.getEmployeeCode())
                .position(employeeRequest.getPosition())
                .department(employeeRequest.getDepartment())
                .supervisorId(employeeRequest.getSupervisorId())
                .hireDate(employeeRequest.getHireDate())
                .terminationDate(employeeRequest.getTerminationDate())
                .salary(employeeRequest.getSalary())
                .currency(employeeRequest.getCurrency())
                .employmentType(employeeRequest.getEmploymentType())
                .status(employeeRequest.getStatus())
                .workEmail(employeeRequest.getWorkEmail())
                .workMobile(employeeRequest.getWorkMobile())
                .workLocation(employeeRequest.getWorkLocation())
                .workSchedule(employeeRequest.getWorkSchedule())
                .skills(employeeRequest.getSkills())
                .certifications(employeeRequest.getCertifications())
                .education(employeeRequest.getEducation())
                .benefits(employeeRequest.getBenefits())
                .notes(employeeRequest.getNotes())
                .isActive(employeeRequest.getIsActive())
                .build();
    }

    /**
     * Updates an existing Employee entity with data from EmployeeUpdateRequestDTO
     * @param employee Existing employee entity
     * @param employeeRequest Update request DTO
     * @return Updated Employee entity
     */
    public static Employee updateEntity(Employee employee, EmployeeUpdateRequestDTO employeeRequest) {
        if (employee == null || employeeRequest == null) return employee;
        
        // Update employee code if provided and not empty
        if (employeeRequest.getEmployeeCode() != null && !employeeRequest.getEmployeeCode().trim().isEmpty()) {
            employee.setEmployeeCode(employeeRequest.getEmployeeCode().trim());
        }
        
        // Update position if provided and not empty
        if (employeeRequest.getPosition() != null && !employeeRequest.getPosition().trim().isEmpty()) {
            employee.setPosition(employeeRequest.getPosition().trim());
        }
        
        // Update department if provided and not empty
        if (employeeRequest.getDepartment() != null && !employeeRequest.getDepartment().trim().isEmpty()) {
            employee.setDepartment(employeeRequest.getDepartment().trim());
        }
        
        // Update supervisor ID if provided
        if (employeeRequest.getSupervisorId() != null) {
            employee.setSupervisorId(employeeRequest.getSupervisorId());
        }
        
        // Update hire date if provided
        if (employeeRequest.getHireDate() != null) {
            employee.setHireDate(employeeRequest.getHireDate());
        }
        
        // Update termination date if provided
        if (employeeRequest.getTerminationDate() != null) {
            employee.setTerminationDate(employeeRequest.getTerminationDate());
        }
        
        // Update salary if provided
        if (employeeRequest.getSalary() != null) {
            employee.setSalary(employeeRequest.getSalary());
        }
        
        // Update currency if provided and not empty
        if (employeeRequest.getCurrency() != null && !employeeRequest.getCurrency().trim().isEmpty()) {
            employee.setCurrency(employeeRequest.getCurrency().trim());
        }
        
        // Update employment type if provided and not empty
        if (employeeRequest.getEmploymentType() != null && !employeeRequest.getEmploymentType().trim().isEmpty()) {
            employee.setEmploymentType(employeeRequest.getEmploymentType().trim());
        }
        
        // Update status if provided and not empty
        if (employeeRequest.getStatus() != null && !employeeRequest.getStatus().trim().isEmpty()) {
            employee.setStatus(employeeRequest.getStatus().trim());
        }
        
        // Update work email if provided and not empty
        if (employeeRequest.getWorkEmail() != null && !employeeRequest.getWorkEmail().trim().isEmpty()) {
            employee.setWorkEmail(employeeRequest.getWorkEmail().trim().toLowerCase());
        }
        
        // Update work mobile if provided and not empty
        if (employeeRequest.getWorkMobile() != null && !employeeRequest.getWorkMobile().trim().isEmpty()) {
            employee.setWorkMobile(employeeRequest.getWorkMobile().trim());
        }
        
        // Update work location if provided and not empty
        if (employeeRequest.getWorkLocation() != null && !employeeRequest.getWorkLocation().trim().isEmpty()) {
            employee.setWorkLocation(employeeRequest.getWorkLocation().trim());
        }
        
        // Update work schedule if provided and not empty
        if (employeeRequest.getWorkSchedule() != null && !employeeRequest.getWorkSchedule().trim().isEmpty()) {
            employee.setWorkSchedule(employeeRequest.getWorkSchedule().trim());
        }
        
        // Update skills if provided and not empty
        if (employeeRequest.getSkills() != null && !employeeRequest.getSkills().trim().isEmpty()) {
            employee.setSkills(employeeRequest.getSkills().trim());
        }
        
        // Update certifications if provided and not empty
        if (employeeRequest.getCertifications() != null && !employeeRequest.getCertifications().trim().isEmpty()) {
            employee.setCertifications(employeeRequest.getCertifications().trim());
        }
        
        // Update education if provided and not empty
        if (employeeRequest.getEducation() != null && !employeeRequest.getEducation().trim().isEmpty()) {
            employee.setEducation(employeeRequest.getEducation().trim());
        }
        
        // Update benefits if provided and not empty
        if (employeeRequest.getBenefits() != null && !employeeRequest.getBenefits().trim().isEmpty()) {
            employee.setBenefits(employeeRequest.getBenefits().trim());
        }
        
        // Update notes if provided and not empty
        if (employeeRequest.getNotes() != null && !employeeRequest.getNotes().trim().isEmpty()) {
            employee.setNotes(employeeRequest.getNotes().trim());
        }
        
        // Update active status if provided
        if (employeeRequest.getIsActive() != null) {
            employee.setIsActive(employeeRequest.getIsActive());
        }
        
        return employee;
    }

    /**
     * Converts a set of Employees to DTOs
     * @param employees Set of employees
     * @return Set of DTOs
     */
    public static Set<EmployeeResponseDTO> toDtoSet(Set<Employee> employees) {
        if (employees == null) return Set.of();
        
        return employees.stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toSet());
    }
} 