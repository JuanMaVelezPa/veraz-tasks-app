package com.veraz.tasks.backend.person.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veraz.tasks.backend.person.dto.EmployeeCreateRequestDTO;
import com.veraz.tasks.backend.person.dto.EmployeeResponseDTO;
import com.veraz.tasks.backend.person.dto.EmployeeUpdateRequestDTO;
import com.veraz.tasks.backend.person.mapper.EmployeeMapper;
import com.veraz.tasks.backend.person.model.Employee;
import com.veraz.tasks.backend.person.repository.EmployeeRepository;
import com.veraz.tasks.backend.person.repository.PersonRepository;
import com.veraz.tasks.backend.exception.DataConflictException;
import com.veraz.tasks.backend.exception.ResourceNotFoundException;
import com.veraz.tasks.backend.person.model.Person;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.service.ServiceInterface;

import lombok.RequiredArgsConstructor;

/**
 * Service class for Employee entity operations
 * Provides business logic for employee management
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService implements ServiceInterface<Employee, UUID, EmployeeCreateRequestDTO, EmployeeUpdateRequestDTO, EmployeeResponseDTO> {

    private final EmployeeRepository employeeRepository;
    private final PersonRepository personRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<EmployeeResponseDTO> findAll(Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        List<EmployeeResponseDTO> dtoPage = employeePage.getContent().stream()
                .map(employeeMapper::toResponseDTO)
                .collect(Collectors.toList());

        return PaginatedResponseDTO.<EmployeeResponseDTO>builder()
                .data(dtoPage)
                .pagination(PaginatedResponseDTO.PaginationInfo.builder()
                        .currentPage(employeePage.getNumber())
                        .totalPages(employeePage.getTotalPages())
                        .totalElements(employeePage.getTotalElements())
                        .pageSize(employeePage.getSize())
                        .hasNext(employeePage.hasNext())
                        .hasPrevious(employeePage.hasPrevious())
                        .isFirst(employeePage.isFirst())
                        .isLast(employeePage.isLast())
                        .build())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeResponseDTO> findById(UUID id) {
        return employeeRepository.findById(id)
                .filter(Employee::getIsActive)
                .map(employeeMapper::toResponseDTO);
    }

    @Override
    public EmployeeResponseDTO create(EmployeeCreateRequestDTO createRequest) {
        // Validate person exists
        Person person = personRepository.findById(createRequest.getPersonId())
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with ID: " + createRequest.getPersonId()));

        // Check if person is already an employee
        if (employeeRepository.existsByPersonId(createRequest.getPersonId())) {
            throw new DataConflictException("Person is already an employee");
        }

        // Check if employee code already exists
        if (employeeRepository.existsByEmployeeCode(createRequest.getEmployeeCode())) {
            throw new DataConflictException("Employee code already exists: " + createRequest.getEmployeeCode());
        }

        Employee employee = employeeMapper.toEntity(createRequest, person);
        Employee savedEmployee = employeeRepository.save(employee);

        return employeeMapper.toResponseDTO(savedEmployee);
    }

    @Override
    public EmployeeResponseDTO update(UUID id, EmployeeUpdateRequestDTO updateRequest) {
        Employee employee = employeeRepository.findById(id)
                .filter(Employee::getIsActive)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        // Check if employee code is being changed and if it already exists
        if (updateRequest.getEmployeeCode() != null &&
                !updateRequest.getEmployeeCode().equals(employee.getEmployeeCode()) &&
                employeeRepository.existsByEmployeeCode(updateRequest.getEmployeeCode())) {
            throw new DataConflictException("Employee code already exists: " + updateRequest.getEmployeeCode());
        }

        Employee updatedEmployee = employeeMapper.updateEntity(employee, updateRequest);
        Employee savedEmployee = employeeRepository.save(updatedEmployee);

        return employeeMapper.toResponseDTO(savedEmployee);
    }

    @Override
    public void deleteById(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .filter(Employee::getIsActive)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        employee.setIsActive(false);
        employeeRepository.save(employee);
    }

    /**
     * Find employee by employee code
     * 
     * @param employeeCode employee code
     * @return Optional containing employee if found
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeResponseDTO> findByEmployeeCode(String employeeCode) {
        return employeeRepository.findByEmployeeCode(employeeCode)
                .filter(Employee::getIsActive)
                .map(employeeMapper::toResponseDTO);
    }

    /**
     * Find employee by person ID
     * 
     * @param personId person ID
     * @return Optional containing employee if found
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeResponseDTO> findByPersonId(UUID personId) {
        return employeeRepository.findByPersonId(personId)
                .filter(Employee::getIsActive)
                .map(employeeMapper::toResponseDTO);
    }

    /**
     * Search employees by multiple criteria
     * 
     * @param search   search term
     * @param pageable pagination parameters
     * @return paginated response of matching employees
     */
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<EmployeeResponseDTO> findBySearch(String search, Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findBySearch(search, pageable);
        List<EmployeeResponseDTO> dtoPage = employeePage.getContent().stream()
                .map(employeeMapper::toResponseDTO)
                .collect(Collectors.toList());

        return PaginatedResponseDTO.<EmployeeResponseDTO>builder()
                .data(dtoPage)
                .pagination(PaginatedResponseDTO.PaginationInfo.builder()
                        .currentPage(employeePage.getNumber())
                        .totalPages(employeePage.getTotalPages())
                        .totalElements(employeePage.getTotalElements())
                        .pageSize(employeePage.getSize())
                        .hasNext(employeePage.hasNext())
                        .hasPrevious(employeePage.hasPrevious())
                        .isFirst(employeePage.isFirst())
                        .isLast(employeePage.isLast())
                        .build())
                .build();
    }

    /**
     * Find employees by department
     * 
     * @param department department name
     * @param pageable   pagination parameters
     * @return paginated response of employees in department
     */
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<EmployeeResponseDTO> findByDepartment(String department, Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findByDepartment(department, pageable);
        List<EmployeeResponseDTO> dtoPage = employeePage.getContent().stream()
                .map(employeeMapper::toResponseDTO)
                .collect(Collectors.toList());

        return PaginatedResponseDTO.<EmployeeResponseDTO>builder()
                .data(dtoPage)
                .pagination(PaginatedResponseDTO.PaginationInfo.builder()
                        .currentPage(employeePage.getNumber())
                        .totalPages(employeePage.getTotalPages())
                        .totalElements(employeePage.getTotalElements())
                        .pageSize(employeePage.getSize())
                        .hasNext(employeePage.hasNext())
                        .hasPrevious(employeePage.hasPrevious())
                        .isFirst(employeePage.isFirst())
                        .isLast(employeePage.isLast())
                        .build())
                .build();
    }

    /**
     * Find employees by employment type
     * 
     * @param employmentType employment type
     * @param pageable       pagination parameters
     * @return paginated response of employees with employment type
     */
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<EmployeeResponseDTO> findByEmploymentType(String employmentType, Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findByEmploymentType(employmentType, pageable);
        List<EmployeeResponseDTO> dtoPage = employeePage.getContent().stream()
                .map(employeeMapper::toResponseDTO)
                .collect(Collectors.toList());

        return PaginatedResponseDTO.<EmployeeResponseDTO>builder()
                .data(dtoPage)
                .pagination(PaginatedResponseDTO.PaginationInfo.builder()
                        .currentPage(employeePage.getNumber())
                        .totalPages(employeePage.getTotalPages())
                        .totalElements(employeePage.getTotalElements())
                        .pageSize(employeePage.getSize())
                        .hasNext(employeePage.hasNext())
                        .hasPrevious(employeePage.hasPrevious())
                        .isFirst(employeePage.isFirst())
                        .isLast(employeePage.isLast())
                        .build())
                .build();
    }

    /**
     * Find employees by status
     * 
     * @param status   employee status
     * @param pageable pagination parameters
     * @return paginated response of employees with status
     */
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<EmployeeResponseDTO> findByStatus(String status, Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findByStatus(status, pageable);
        List<EmployeeResponseDTO> dtoPage = employeePage.getContent().stream()
                .map(employeeMapper::toResponseDTO)
                .collect(Collectors.toList());

        return PaginatedResponseDTO.<EmployeeResponseDTO>builder()
                .data(dtoPage)
                .pagination(PaginatedResponseDTO.PaginationInfo.builder()
                        .currentPage(employeePage.getNumber())
                        .totalPages(employeePage.getTotalPages())
                        .totalElements(employeePage.getTotalElements())
                        .pageSize(employeePage.getSize())
                        .hasNext(employeePage.hasNext())
                        .hasPrevious(employeePage.hasPrevious())
                        .isFirst(employeePage.isFirst())
                        .isLast(employeePage.isLast())
                        .build())
                .build();
    }
}

