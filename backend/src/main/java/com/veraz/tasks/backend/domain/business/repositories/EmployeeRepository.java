package com.veraz.tasks.backend.domain.business.repositories;

import com.veraz.tasks.backend.domain.business.entities.Employee;
import com.veraz.tasks.backend.domain.business.valueobjects.EmployeeId;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;

import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Employee repository interface
 * 
 * Defines the contract for employee data access.
 * Part of the domain layer in Clean Architecture.
 * Implementation details are in the infrastructure layer.
 */
public interface EmployeeRepository {
    
    Employee save(Employee employee);
    Optional<Employee> findById(EmployeeId id);
    Optional<Employee> findByPersonId(PersonId personId);
    Page<Employee> findAll(PaginationRequestDTO paginationRequest);
    boolean existsByPersonId(PersonId personId);
    void delete(Employee employee);
    void deleteById(EmployeeId id);
    long count();
}

