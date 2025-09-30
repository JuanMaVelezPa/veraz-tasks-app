package com.veraz.tasks.backend.application.business.queries;

import com.veraz.tasks.backend.application.business.dto.EmployeeResponse;
import com.veraz.tasks.backend.domain.business.entities.Employee;
import com.veraz.tasks.backend.domain.business.repositories.EmployeeRepository;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;
import com.veraz.tasks.backend.shared.util.PaginationUtils;

import org.springframework.data.domain.Page;

public class FindAllEmployeesUseCase {
    
    private final EmployeeRepository employeeRepository;
    
    public FindAllEmployeesUseCase(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    public PaginatedResponseDTO<EmployeeResponse> execute(PaginationRequestDTO paginationRequest) {
        Page<Employee> employeesPage = employeeRepository.findAll(paginationRequest);
        return PaginationUtils.toPaginatedResponse(employeesPage, EmployeeResponse::from);
    }
}

