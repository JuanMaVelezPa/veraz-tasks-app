package com.veraz.tasks.backend.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

import com.veraz.tasks.backend.person.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findByPersonId(UUID personId);

}
