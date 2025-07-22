package com.veraz.tasks.backend.person.controller;

import com.veraz.tasks.backend.person.dto.EmployeeRequestDto;
import com.veraz.tasks.backend.person.dto.EmployeeResponseDto;
import com.veraz.tasks.backend.person.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/employee")
@Tag(name = "Employee", description = "Employee endpoints")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get all employees", description = "Get all employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    public ResponseEntity<List<EmployeeResponseDto>> getEmployees() {
        return ResponseEntity.ok(employeeService.getEmployees());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get an employee by id", description = "Get an employee by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    public ResponseEntity<EmployeeResponseDto> getEmployee(@PathVariable UUID id) {
        EmployeeResponseDto response = employeeService.getEmployeeById(id);
        if ("NOT_FOUND".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Create an employee", description = "Create an employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    public ResponseEntity<EmployeeResponseDto> createEmployee(@Valid @RequestBody EmployeeRequestDto dto) {
        EmployeeResponseDto response = employeeService.createEmployee(dto);
        if ("CREATED".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else if ("NOT_FOUND".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if ("ERROR".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Update an employee", description = "Update an employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
            @ApiResponse(responseCode = "404", description = "Employee or person not found"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    public ResponseEntity<EmployeeResponseDto> updateEmployee(@PathVariable UUID id,
            @Valid @RequestBody EmployeeRequestDto dto) {
        EmployeeResponseDto response = employeeService.updateEmployee(id, dto);
        if ("NOT_FOUND".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if ("ERROR".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Delete an employee", description = "Delete an employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    public ResponseEntity<EmployeeResponseDto> deleteEmployee(@PathVariable UUID id) {
        EmployeeResponseDto response = employeeService.deleteEmployee(id);
        if ("NOT_FOUND".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if ("DELETED".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }
        return ResponseEntity.ok(response);
    }
}