package com.veraz.tasks.backend.person.service;

import com.veraz.tasks.backend.person.dto.EmployeeRequestDto;
import com.veraz.tasks.backend.person.dto.EmployeeResponseDto;
import com.veraz.tasks.backend.person.mapper.EmployeeMapper;
import com.veraz.tasks.backend.person.model.Employee;
import com.veraz.tasks.backend.person.model.Person;
import com.veraz.tasks.backend.person.repository.EmployeeRepository;
import com.veraz.tasks.backend.person.repository.PersonRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepository employeeRepository;
    private final PersonRepository personRepository;
    private final MessageSource messageSource;

    public EmployeeService(EmployeeRepository employeeRepository, PersonRepository personRepository,
            MessageSource messageSource) {
        this.employeeRepository = employeeRepository;
        this.personRepository = personRepository;
        this.messageSource = messageSource;
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeResponseDto> employeeResponseDtos = new ArrayList<>();
        for (Employee employee : employees) {
            employeeResponseDtos.add(EmployeeResponseDto.builder()
                    .employee(EmployeeMapper.toDto(employee))
                    .message(null)
                    .status("OK")
                    .build());
        }
        return employeeResponseDtos;
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeById(UUID id) {
        Optional<Employee> employeeOpt = employeeRepository.findById(id);
        if (employeeOpt.isEmpty()) {
            return EmployeeResponseDto.builder()
                    .employee(null)
                    .message(messageSource.getMessage("employee.not.found", null, LocaleContextHolder.getLocale()))
                    .status("NOT_FOUND")
                    .build();
        }
        return EmployeeResponseDto.builder()
                .employee(EmployeeMapper.toDto(employeeOpt.get()))
                .message(null)
                .status("OK")
                .build();
    }

    @Transactional
    public EmployeeResponseDto createEmployee(EmployeeRequestDto dto) {
        try {
            Optional<Person> personOpt = personRepository.findById(dto.getPersonId());
            if (personOpt.isEmpty()) {
                return EmployeeResponseDto.builder()
                        .employee(null)
                        .message(messageSource.getMessage("person.not.found", null, LocaleContextHolder.getLocale()))
                        .status("NOT_FOUND")
                        .build();
            }
            Employee employee = EmployeeMapper.toEntity(dto, personOpt.get());
            employee = employeeRepository.save(employee);
            return EmployeeResponseDto.builder()
                    .employee(EmployeeMapper.toDto(employee))
                    .message(messageSource.getMessage("employee.created.successfully", null,
                            LocaleContextHolder.getLocale()))
                    .status("CREATED")
                    .build();
        } catch (Exception e) {
            logger.error("Error creating employee: " + e.getMessage());
            return EmployeeResponseDto.builder()
                    .employee(null)
                    .message(messageSource.getMessage("employee.error.creating", null, LocaleContextHolder.getLocale()))
                    .status("ERROR")
                    .build();
        }
    }

    @Transactional
    public EmployeeResponseDto updateEmployee(UUID id, EmployeeRequestDto dto) {
        try {
            Optional<Employee> employeeOpt = employeeRepository.findById(id);
            if (employeeOpt.isEmpty()) {
                return EmployeeResponseDto.builder()
                        .employee(null)
                        .message(messageSource.getMessage("employee.not.found", null, LocaleContextHolder.getLocale()))
                        .status("NOT_FOUND")
                        .build();
            }
            Optional<Person> personOpt = personRepository.findById(dto.getPersonId());
            if (personOpt.isEmpty()) {
                return EmployeeResponseDto.builder()
                        .employee(null)
                        .message(messageSource.getMessage("person.not.found", null, LocaleContextHolder.getLocale()))
                        .status("NOT_FOUND")
                        .build();
            }
            Employee employee = employeeOpt.get();
            // Actualizar campos
            employee.setPerson(personOpt.get());
            employee.setEmployeeCode(dto.getEmployeeCode());
            employee.setPosition(dto.getPosition());
            employee.setDepartment(dto.getDepartment());
            employee.setSupervisorId(dto.getSupervisorId());
            employee.setHireDate(dto.getHireDate());
            employee.setTerminationDate(dto.getTerminationDate());
            employee.setSalary(dto.getSalary());
            employee.setCurrency(dto.getCurrency());
            employee.setEmploymentType(dto.getEmploymentType());
            employee.setStatus(dto.getStatus());
            employee.setWorkEmail(dto.getWorkEmail());
            employee.setWorkMobile(dto.getWorkMobile());
            employee.setWorkLocation(dto.getWorkLocation());
            employee.setWorkSchedule(dto.getWorkSchedule());
            employee.setSkills(dto.getSkills());
            employee.setCertifications(dto.getCertifications());
            employee.setEducation(dto.getEducation());
            employee.setBenefits(dto.getBenefits());
            employee.setNotes(dto.getNotes());
            employee = employeeRepository.save(employee);
            return EmployeeResponseDto.builder()
                    .employee(EmployeeMapper.toDto(employee))
                    .message(messageSource.getMessage("employee.updated.successfully", null,
                            LocaleContextHolder.getLocale()))
                    .status("UPDATED")
                    .build();
        } catch (Exception e) {
            logger.error("Error updating employee: " + e.getMessage());
            return EmployeeResponseDto.builder()
                    .employee(null)
                    .message(messageSource.getMessage("employee.error.updating", null, LocaleContextHolder.getLocale()))
                    .status("ERROR")
                    .build();
        }
    }

    @Transactional
    public EmployeeResponseDto deleteEmployee(UUID id) {
        try {
            Optional<Employee> employeeOpt = employeeRepository.findById(id);
            if (employeeOpt.isEmpty()) {
                return EmployeeResponseDto.builder()
                        .employee(null)
                        .message(messageSource.getMessage("employee.not.found", null, LocaleContextHolder.getLocale()))
                        .status("NOT_FOUND")
                        .build();
            }
            employeeRepository.delete(employeeOpt.get());
            return EmployeeResponseDto.builder()
                    .employee(null)
                    .message(messageSource.getMessage("employee.deleted.successfully", null,
                            LocaleContextHolder.getLocale()))
                    .status("DELETED")
                    .build();
        } catch (Exception e) {
            logger.error("Error deleting employee: " + e.getMessage());
            return EmployeeResponseDto.builder()
                    .employee(null)
                    .message(messageSource.getMessage("employee.error.deleting", null, LocaleContextHolder.getLocale()))
                    .status("ERROR")
                    .build();
        }
    }
}