package com.veraz.tasks.backend.person.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.veraz.tasks.backend.person.model.Employee;
import com.veraz.tasks.backend.shared.repository.RepositoryInterface;

/**
 * Repository interface for Employee entity
 * Provides data access methods for employee operations
 */
@Repository
public interface EmployeeRepository extends RepositoryInterface<Employee, UUID> {

    /**
     * Find employee by employee code
     * 
     * @param employeeCode the employee code to search for
     * @return Optional containing the employee if found
     */
    Optional<Employee> findByEmployeeCode(String employeeCode);

    /**
     * Find employee by person ID
     * 
     * @param personId the person ID to search for
     * @return Optional containing the employee if found
     */
    @Query("SELECT e FROM Employee e WHERE e.person.id = :personId AND e.isActive = true")
    Optional<Employee> findByPersonId(@Param("personId") UUID personId);

    /**
     * Check if employee exists by employee code
     * 
     * @param employeeCode the employee code to check
     * @return true if employee exists, false otherwise
     */
    boolean existsByEmployeeCode(String employeeCode);

    /**
     * Check if employee exists by person ID
     * 
     * @param personId the person ID to check
     * @return true if employee exists, false otherwise
     */
    @Query("SELECT COUNT(e) > 0 FROM Employee e WHERE e.person.id = :personId AND e.isActive = true")
    boolean existsByPersonId(@Param("personId") UUID personId);

    /**
     * Find all active employees
     * 
     * @param pageable pagination parameters
     * @return Page of active employees
     */
    Page<Employee> findByIsActiveTrue(Pageable pageable);

    /**
     * Find employees by department
     * 
     * @param department the department to search for
     * @param pageable   pagination parameters
     * @return Page of employees in the department
     */
    Page<Employee> findByDepartment(String department, Pageable pageable);

    /**
     * Find employees by employment type
     * 
     * @param employmentType the employment type to search for
     * @param pageable       pagination parameters
     * @return Page of employees with the employment type
     */
    Page<Employee> findByEmploymentType(String employmentType, Pageable pageable);

    /**
     * Find employees by status
     * 
     * @param status   the status to search for
     * @param pageable pagination parameters
     * @return Page of employees with the status
     */
    Page<Employee> findByStatus(String status, Pageable pageable);

    /**
     * Search employees by multiple criteria
     * 
     * @param search   the search term
     * @param pageable pagination parameters
     * @return Page of employees matching the search criteria
     */
    @Query("SELECT e FROM Employee e " +
            "WHERE e.isActive = true AND (" +
            "LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.position) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.department) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.workEmail) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Employee> findBySearch(@Param("search") String search, Pageable pageable);
}

