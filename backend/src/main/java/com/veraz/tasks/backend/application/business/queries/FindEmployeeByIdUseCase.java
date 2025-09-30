package com.veraz.tasks.backend.application.business.queries;

import com.veraz.tasks.backend.application.business.dto.EmployeeResponse;
import com.veraz.tasks.backend.domain.business.entities.Employee;
import com.veraz.tasks.backend.domain.business.repositories.EmployeeRepository;
import com.veraz.tasks.backend.domain.business.valueobjects.EmployeeId;

import java.util.Optional;

public class FindEmployeeByIdUseCase {
    
    private final EmployeeRepository employeeRepository;
    
    public FindEmployeeByIdUseCase(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    public Optional<EmployeeResponse> execute(String employeeIdString) {
        EmployeeId employeeId = EmployeeId.of(employeeIdString);
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        return employee.map(EmployeeResponse::from);
    }
}

