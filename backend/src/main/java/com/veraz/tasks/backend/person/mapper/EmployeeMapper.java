package com.veraz.tasks.backend.person.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import com.veraz.tasks.backend.person.dto.EmployeeRequestDTO;
import com.veraz.tasks.backend.person.dto.EmployeeResponseDTO;
import com.veraz.tasks.backend.person.model.Employee;
import com.veraz.tasks.backend.person.model.Person;

public class EmployeeMapper {

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

    public static Employee toEntity(EmployeeRequestDTO employeeRequest, Person person) {
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
     * Actualiza una entidad Employee existente con datos del DTO
     * @param employee Entidad existente
     * @param employeeRequest DTO con datos a actualizar
     * @return Employee actualizado
     */
    public static Employee updateEntity(Employee employee, EmployeeRequestDTO employeeRequest) {
        if (employee == null || employeeRequest == null) return employee;
        
        if (employeeRequest.getEmployeeCode() != null) {
            employee.setEmployeeCode(employeeRequest.getEmployeeCode().trim());
        }
        
        if (employeeRequest.getPosition() != null) {
            employee.setPosition(employeeRequest.getPosition().trim());
        }
        
        if (employeeRequest.getDepartment() != null) {
            employee.setDepartment(employeeRequest.getDepartment());
        }
        
        if (employeeRequest.getSupervisorId() != null) {
            employee.setSupervisorId(employeeRequest.getSupervisorId());
        }
        
        if (employeeRequest.getHireDate() != null) {
            employee.setHireDate(employeeRequest.getHireDate());
        }
        
        if (employeeRequest.getTerminationDate() != null) {
            employee.setTerminationDate(employeeRequest.getTerminationDate());
        }
        
        if (employeeRequest.getSalary() != null) {
            employee.setSalary(employeeRequest.getSalary());
        }
        
        if (employeeRequest.getCurrency() != null) {
            employee.setCurrency(employeeRequest.getCurrency());
        }
        
        if (employeeRequest.getEmploymentType() != null) {
            employee.setEmploymentType(employeeRequest.getEmploymentType());
        }
        
        if (employeeRequest.getStatus() != null) {
            employee.setStatus(employeeRequest.getStatus());
        }
        
        if (employeeRequest.getWorkEmail() != null) {
            employee.setWorkEmail(employeeRequest.getWorkEmail().trim().toLowerCase());
        }
        
        if (employeeRequest.getWorkMobile() != null) {
            employee.setWorkMobile(employeeRequest.getWorkMobile().trim());
        }
        
        if (employeeRequest.getWorkLocation() != null) {
            employee.setWorkLocation(employeeRequest.getWorkLocation());
        }
        
        if (employeeRequest.getWorkSchedule() != null) {
            employee.setWorkSchedule(employeeRequest.getWorkSchedule());
        }
        
        if (employeeRequest.getSkills() != null) {
            employee.setSkills(employeeRequest.getSkills().trim());
        }
        
        if (employeeRequest.getCertifications() != null) {
            employee.setCertifications(employeeRequest.getCertifications().trim());
        }
        
        if (employeeRequest.getEducation() != null) {
            employee.setEducation(employeeRequest.getEducation().trim());
        }
        
        if (employeeRequest.getBenefits() != null) {
            employee.setBenefits(employeeRequest.getBenefits().trim());
        }
        
        if (employeeRequest.getNotes() != null) {
            employee.setNotes(employeeRequest.getNotes().trim());
        }
        
        if (employeeRequest.getIsActive() != null) {
            employee.setIsActive(employeeRequest.getIsActive());
        }
        
        return employee;
    }

    /**
     * Convierte una lista de Employees a DTOs
     * @param employees Lista de empleados
     * @return Lista de DTOs
     */
    public static Set<EmployeeResponseDTO> toDtoSet(Set<Employee> employees) {
        if (employees == null) return Set.of();
        
        return employees.stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toSet());
    }

} 