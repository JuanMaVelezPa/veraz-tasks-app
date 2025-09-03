package com.veraz.tasks.backend.person.service;

import java.util.Optional;
import java.util.UUID;

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
import com.veraz.tasks.backend.shared.util.PaginationUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService implements
        ServiceInterface<Employee, UUID, EmployeeCreateRequestDTO, EmployeeUpdateRequestDTO, EmployeeResponseDTO> {

    private final EmployeeRepository employeeRepository;
    private final PersonRepository personRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<EmployeeResponseDTO> findAll(Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        return PaginationUtils.toPaginatedResponse(employeePage, employeeMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeResponseDTO> findById(UUID id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toResponseDTO);
    }

    @Override
    public EmployeeResponseDTO create(EmployeeCreateRequestDTO createRequest) {
        Person person = personRepository.findById(createRequest.getPersonId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Person not found with ID: " + createRequest.getPersonId()));

        if (employeeRepository.existsByPersonId(createRequest.getPersonId())) {
            throw new DataConflictException("Person is already an employee");
        }

        Employee employee = employeeMapper.toEntity(createRequest, person);
        Employee savedEmployee = employeeRepository.save(employee);

        return employeeMapper.toResponseDTO(savedEmployee);
    }

    @Override
    public EmployeeResponseDTO update(UUID id, EmployeeUpdateRequestDTO updateRequest) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        Employee updatedEmployee = employeeMapper.updateEntity(employee, updateRequest);
        Employee savedEmployee = employeeRepository.save(updatedEmployee);

        return employeeMapper.toResponseDTO(savedEmployee);
    }

    @Override
    public void deleteById(UUID id) {
        Employee employee = employeeRepository.findByIdWithPerson(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        // if (employee.getPerson() != null) {
        //     throw new DataConflictException(
        //             "Cannot delete employee with associated person. Remove person association first.");
        // }

        employeeRepository.delete(employee);
    }

    @Transactional(readOnly = true)
    public Optional<EmployeeResponseDTO> findByPersonId(UUID personId) {
        return employeeRepository.findByPersonId(personId)
                .map(employeeMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public PaginatedResponseDTO<EmployeeResponseDTO> findBySearch(String search, Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findBySearch(search, pageable);
        return PaginationUtils.toPaginatedResponse(employeePage, employeeMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public PaginatedResponseDTO<EmployeeResponseDTO> findByDepartment(String department, Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findByDepartment(department, pageable);
        return PaginationUtils.toPaginatedResponse(employeePage, employeeMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public PaginatedResponseDTO<EmployeeResponseDTO> findByEmploymentType(String employmentType, Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findByEmploymentType(employmentType, pageable);
        return PaginationUtils.toPaginatedResponse(employeePage, employeeMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public PaginatedResponseDTO<EmployeeResponseDTO> findByStatus(String status, Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findByStatus(status, pageable);
        return PaginationUtils.toPaginatedResponse(employeePage, employeeMapper::toResponseDTO);
    }

    @Transactional
    public EmployeeResponseDTO removePersonAssociation(UUID employeeId) {
        Employee employee = employeeRepository.findByIdWithPerson(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        employee.setPerson(null);
        employeeRepository.save(employee);

        return employeeMapper.toResponseDTO(employee);
    }

    @Transactional
    public EmployeeResponseDTO associatePerson(UUID employeeId, UUID personId) {
        Employee employee = employeeRepository.findByIdWithPerson(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with ID: " + personId));

        employee.setPerson(person);
        employeeRepository.save(employee);

        return employeeMapper.toResponseDTO(employee);
    }
}
