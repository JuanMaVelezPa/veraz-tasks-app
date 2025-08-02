package com.veraz.tasks.backend.person.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veraz.tasks.backend.person.dto.EmployeeCreateRequestDTO;
import com.veraz.tasks.backend.person.dto.EmployeeUpdateRequestDTO;
import com.veraz.tasks.backend.person.dto.EmployeeResponseDTO;
import com.veraz.tasks.backend.shared.controller.ControllerInterface;
import com.veraz.tasks.backend.shared.dto.ApiResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;
import com.veraz.tasks.backend.shared.util.PaginationUtils;
import com.veraz.tasks.backend.person.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/employees")
@Tag(name = "Employee", description = "Employee endpoints")
public class EmployeeController implements ControllerInterface<UUID, EmployeeCreateRequestDTO, EmployeeUpdateRequestDTO, EmployeeResponseDTO> {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    @Operation(summary = "Get all employees", description = "Get all employees with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR')")
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<EmployeeResponseDTO>>> findAll(
            @ModelAttribute PaginationRequestDTO paginationRequest) {

        paginationRequest.validateAndNormalize();
        Pageable pageable = PaginationUtils.createPageable(paginationRequest);

        PaginatedResponseDTO<EmployeeResponseDTO> response = employeeService.findAll(pageable);

        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, "Employees retrieved successfully",
                response, null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee", description = "Get employee with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR')")
    public ResponseEntity<ApiResponseDTO<EmployeeResponseDTO>> findById(@PathVariable UUID id) {
        Optional<EmployeeResponseDTO> response = employeeService.findById(id);
        return ResponseEntity
                .ok(new ApiResponseDTO<>(true, HttpStatus.OK, "Employee retrieved successfully", response.get(), null));
    }

    @PostMapping
    @Operation(summary = "Create employee", description = "Create employee with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions."),
            @ApiResponse(responseCode = "409", description = "Conflict - Employee already exists")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponseDTO<EmployeeResponseDTO>> create(@Valid @RequestBody EmployeeCreateRequestDTO employeeRequestDTO) {
        EmployeeResponseDTO response = employeeService.create(employeeRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, HttpStatus.CREATED, "Employee created successfully", response, null));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update employee", description = "Update employee with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions."),
            @ApiResponse(responseCode = "409", description = "Conflict - Employee already exists")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponseDTO<EmployeeResponseDTO>> update(@PathVariable UUID id,
            @Valid @RequestBody EmployeeUpdateRequestDTO employeeRequestDTO) {

        EmployeeResponseDTO response = employeeService.update(id, employeeRequestDTO);
        return ResponseEntity
                .ok(new ApiResponseDTO<>(true, HttpStatus.OK, "Employee updated successfully", response, null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employee", description = "Delete employee with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponseDTO<Void>> deleteById(@PathVariable UUID id) {
        employeeService.deleteById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, "Employee deleted successfully", null, null));
    }
}