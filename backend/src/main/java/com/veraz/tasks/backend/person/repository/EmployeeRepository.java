package com.veraz.tasks.backend.person.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.veraz.tasks.backend.person.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findByEmployeeCode(String employeeCode);

    Optional<Employee> findByPersonId(UUID personId);

    @Query("SELECT e FROM Employee e JOIN e.person p WHERE " +
           "LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.position) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.department) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Employee> findByEmployeeCodeOrPositionOrDepartmentOrPersonNameContainingIgnoreCase(
            @Param("searchTerm") String searchTerm, Pageable pageable);

    // Additional useful methods
    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByPersonId(UUID personId);

    List<Employee> findByIsActive(Boolean isActive);

    List<Employee> findByPosition(String position);

    List<Employee> findByDepartment(String department);

    List<Employee> findByStatus(String status);

    List<Employee> findByEmploymentType(String employmentType);

    List<Employee> findBySupervisorId(UUID supervisorId);

    @Query("SELECT e FROM Employee e WHERE e.salary >= :minSalary")
    List<Employee> findBySalaryGreaterThanOrEqualTo(@Param("minSalary") java.math.BigDecimal minSalary);

    @Query("SELECT e FROM Employee e WHERE e.hireDate BETWEEN :startDate AND :endDate")
    List<Employee> findByHireDateBetween(@Param("startDate") java.time.LocalDate startDate, 
                                        @Param("endDate") java.time.LocalDate endDate);

    @Query("SELECT e FROM Employee e WHERE e.terminationDate IS NULL")
    List<Employee> findActiveEmployees();

    @Query("SELECT e FROM Employee e WHERE e.terminationDate IS NOT NULL")
    List<Employee> findTerminatedEmployees();

    long countByIsActive(Boolean isActive);

    long countByPosition(String position);

    long countByDepartment(String department);

    long countByStatus(String status);

    long countByEmploymentType(String employmentType);

}
