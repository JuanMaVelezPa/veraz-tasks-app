package com.veraz.tasks.backend.application.business.commands;

import com.veraz.tasks.backend.domain.business.entities.Employee;
import com.veraz.tasks.backend.domain.business.repositories.EmployeeRepository;
import com.veraz.tasks.backend.domain.business.valueobjects.EmployeeId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class DeleteEmployeeUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DeleteEmployeeUseCase.class);

    private final EmployeeRepository employeeRepository;

    public DeleteEmployeeUseCase(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void execute(String employeeIdString) {
        logger.debug("Deleting employee with ID: {}", employeeIdString);

        try {
            EmployeeId employeeId = EmployeeId.of(employeeIdString);

            Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
            if (employeeOpt.isEmpty()) {
                throw new IllegalArgumentException("Employee not found with ID: " + employeeIdString);
            }

            employeeRepository.deleteById(employeeId);

            logger.debug("Successfully deleted employee with ID: {}", employeeIdString);

        } catch (Exception e) {
            logger.error("Error deleting employee {}: {}", employeeIdString, e.getMessage(), e);
            throw e;
        }
    }
}
