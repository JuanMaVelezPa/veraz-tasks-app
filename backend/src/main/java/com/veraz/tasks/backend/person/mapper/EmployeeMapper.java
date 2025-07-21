package com.veraz.tasks.backend.person.mapper;

import com.veraz.tasks.backend.person.dto.EmployeeDto;
import com.veraz.tasks.backend.person.dto.EmployeeRequestDto;
import com.veraz.tasks.backend.person.dto.PersonDto;
import com.veraz.tasks.backend.person.model.Employee;
import com.veraz.tasks.backend.person.model.Person;

public class EmployeeMapper {
    public static EmployeeDto toDto(Employee employee) {
        if (employee == null) return null;
        PersonDto personDto = PersonMapper.toDto(employee.getPerson());
        return EmployeeDto.builder()
                .id(employee.getId())
                .person(personDto)
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

    public static Employee toEntity(EmployeeRequestDto dto, Person person) {
        if (dto == null || person == null) return null;
        return Employee.builder()
                .person(person)
                .employeeCode(dto.getEmployeeCode())
                .position(dto.getPosition())
                .department(dto.getDepartment())
                .supervisorId(dto.getSupervisorId())
                .hireDate(dto.getHireDate())
                .terminationDate(dto.getTerminationDate())
                .salary(dto.getSalary())
                .currency(dto.getCurrency())
                .employmentType(dto.getEmploymentType())
                .status(dto.getStatus())
                .workEmail(dto.getWorkEmail())
                .workMobile(dto.getWorkMobile())
                .workLocation(dto.getWorkLocation())
                .workSchedule(dto.getWorkSchedule())
                .skills(dto.getSkills())
                .certifications(dto.getCertifications())
                .education(dto.getEducation())
                .benefits(dto.getBenefits())
                .notes(dto.getNotes())
                .build();
    }
} 