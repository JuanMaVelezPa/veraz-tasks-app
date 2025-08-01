package com.veraz.tasks.backend.person.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veraz.tasks.backend.person.dto.EmployeeRequestDTO;
import com.veraz.tasks.backend.person.dto.EmployeeResponseDTO;
import com.veraz.tasks.backend.person.mapper.EmployeeMapper;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO.PaginationInfo;
import com.veraz.tasks.backend.shared.service.ServiceInterface;
import com.veraz.tasks.backend.person.model.Employee;
import com.veraz.tasks.backend.person.model.Person;
import com.veraz.tasks.backend.person.repository.EmployeeRepository;
import com.veraz.tasks.backend.person.repository.PersonRepository;
import com.veraz.tasks.backend.exception.DataConflictException;
import com.veraz.tasks.backend.exception.ResourceNotFoundException;
import com.veraz.tasks.backend.shared.util.MessageUtils;

@Service
public class EmployeeService implements ServiceInterface<Employee, UUID, EmployeeRequestDTO, EmployeeResponseDTO> {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;
    private final PersonRepository personRepository;

    public EmployeeService(EmployeeRepository employeeRepository, PersonRepository personRepository) {
        this.employeeRepository = employeeRepository;
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public PaginatedResponseDTO<EmployeeResponseDTO> findAll(Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        List<EmployeeResponseDTO> employeeDtos = employeePage.getContent().stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toList());

        PaginationInfo paginationInfo = PaginationInfo
                .builder()
                .currentPage(employeePage.getNumber())
                .totalPages(employeePage.getTotalPages())
                .totalElements(employeePage.getTotalElements())
                .pageSize(employeePage.getSize())
                .hasNext(employeePage.hasNext())
                .hasPrevious(employeePage.hasPrevious())
                .isFirst(employeePage.isFirst())
                .isLast(employeePage.isLast())
                .build();

        return PaginatedResponseDTO.<EmployeeResponseDTO>builder()
                .data(employeeDtos)
                .pagination(paginationInfo)
                .build();
    }

    @Transactional(readOnly = true)
    public Optional<EmployeeResponseDTO> findById(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("Employee")));
        
        return Optional.of(EmployeeMapper.toDto(employee));
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO findByEmployeeCode(String employeeCode) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("Employee")));
        
        return EmployeeMapper.toDto(employee);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO findByPersonId(UUID personId) {
        Employee employee = employeeRepository.findByPersonId(personId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("Employee")));
        
        return EmployeeMapper.toDto(employee);
    }

    @Transactional
    public EmployeeResponseDTO create(EmployeeRequestDTO employeeRequest) {
        // Check if employee already exists with same employee code
        if (employeeRepository.existsByEmployeeCode(employeeRequest.getEmployeeCode())) {
            throw new DataConflictException(MessageUtils.getEntityAlreadyExists("Employee"));
        }

        // Check if person already has an employee record
        if (employeeRepository.existsByPersonId(employeeRequest.getPersonId())) {
            throw new DataConflictException(MessageUtils.getEntityAlreadyExists("Employee"));
        }

        // Get the person
        Person person = personRepository.findById(employeeRequest.getPersonId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("Person")));

        Employee newEmployee = EmployeeMapper.toEntity(employeeRequest, person);
        employeeRepository.save(newEmployee);

        logger.info("Employee created successfully: {} with createdAt: {} and updatedAt: {}",
                newEmployee.getEmployeeCode(), newEmployee.getCreatedAt(), newEmployee.getUpdatedAt());

        return EmployeeMapper.toDto(newEmployee);
    }

    @Transactional
    public EmployeeResponseDTO update(UUID id, EmployeeRequestDTO employeeRequestDTO) {
        Employee employeeToUpdate = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("Employee")));

        // Update employee code if provided
        if (employeeRequestDTO.getEmployeeCode() != null && !employeeRequestDTO.getEmployeeCode().trim().isEmpty()) {
            String newEmployeeCode = employeeRequestDTO.getEmployeeCode().trim();
            
            if (!newEmployeeCode.equalsIgnoreCase(employeeToUpdate.getEmployeeCode())) {
                if (employeeRepository.existsByEmployeeCode(newEmployeeCode)) {
                    throw new DataConflictException(MessageUtils.getEntityAlreadyExists("Employee"));
                }
            }
        }

        // Update employee using mapper
        EmployeeMapper.updateEntity(employeeToUpdate, employeeRequestDTO);
        employeeToUpdate.setUpdatedAt(LocalDateTime.now());
        employeeRepository.save(employeeToUpdate);

        logger.info("Employee updated successfully with ID: {}", employeeToUpdate.getId());

        return EmployeeMapper.toDto(employeeToUpdate);
    }

    @Transactional
    public void deleteById(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("Employee")));
        
        employeeRepository.delete(employee);
        logger.info("Employee deleted successfully with ID: {}", id);
    }

    // Additional useful methods
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> findByIsActive(Boolean isActive) {
        return employeeRepository.findByIsActive(isActive).stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> findByPosition(String position) {
        return employeeRepository.findByPosition(position).stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> findByDepartment(String department) {
        return employeeRepository.findByDepartment(department).stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> findByStatus(String status) {
        return employeeRepository.findByStatus(status).stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> findByEmploymentType(String employmentType) {
        return employeeRepository.findByEmploymentType(employmentType).stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> findBySupervisorId(UUID supervisorId) {
        return employeeRepository.findBySupervisorId(supervisorId).stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> findActiveEmployees() {
        return employeeRepository.findActiveEmployees().stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> findTerminatedEmployees() {
        return employeeRepository.findTerminatedEmployees().stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toList());
    }

}