package com.veraz.tasks.backend.application.business.queries;

import com.veraz.tasks.backend.application.business.dto.EmployeeResponse;
import com.veraz.tasks.backend.domain.business.entities.Employee;
import com.veraz.tasks.backend.domain.business.repositories.EmployeeRepository;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FindEmployeeByPersonIdUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindEmployeeByPersonIdUseCase.class);

    private final EmployeeRepository employeeRepository;

    public FindEmployeeByPersonIdUseCase(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public EmployeeResponse execute(String personIdString) {
        logger.debug("Finding employee by person ID: {}", personIdString);

        try {
            PersonId personId = PersonId.of(personIdString);

            Optional<Employee> employeeOpt = employeeRepository.findByPersonId(personId);
            if (employeeOpt.isEmpty()) {
                logger.warn("Employee not found for person ID: {}", personIdString);
                throw new IllegalArgumentException("Employee not found for person ID: " + personIdString);
            }

            Employee employee = employeeOpt.get();
            logger.debug("Successfully found employee with ID: {} for person ID: {}",
                    employee.getId().getValue(), personIdString);

            return EmployeeResponse.from(employee);

        } catch (Exception e) {
            logger.error("Error finding employee by person ID {}: {}", personIdString, e.getMessage(), e);
            throw e;
        }
    }
}
