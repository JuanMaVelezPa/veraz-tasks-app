package com.veraz.tasks.backend.infrastructure.business.controllers;

import com.veraz.tasks.backend.application.business.commands.CreateEmployeeUseCase;

import com.veraz.tasks.backend.application.business.commands.UpdateEmployeeUseCase;
import com.veraz.tasks.backend.application.business.dto.EmployeeResponse;
import com.veraz.tasks.backend.application.business.queries.FindAllEmployeesUseCase;
import com.veraz.tasks.backend.application.business.queries.FindEmployeeByIdUseCase;
import com.veraz.tasks.backend.application.business.queries.FindEmployeeByPersonIdUseCase;
import com.veraz.tasks.backend.application.business.commands.DeleteEmployeeUseCase;
import com.veraz.tasks.backend.infrastructure.business.dto.CreateEmployeeRequest;
import com.veraz.tasks.backend.infrastructure.business.dto.UpdateEmployeeRequest;
import com.veraz.tasks.backend.shared.dto.ApiResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;
import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.util.MessageUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Employee", description = "Employee management endpoints using Clean Architecture")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private final CreateEmployeeUseCase createEmployeeUseCase;
    private final FindEmployeeByIdUseCase findEmployeeByIdUseCase;
    private final FindEmployeeByPersonIdUseCase findEmployeeByPersonIdUseCase;
    private final FindAllEmployeesUseCase findAllEmployeesUseCase;
    private final UpdateEmployeeUseCase updateEmployeeUseCase;
    private final DeleteEmployeeUseCase deleteEmployeeUseCase;

    public EmployeeController(CreateEmployeeUseCase createEmployeeUseCase,
            FindEmployeeByIdUseCase findEmployeeByIdUseCase,
            FindEmployeeByPersonIdUseCase findEmployeeByPersonIdUseCase,
            FindAllEmployeesUseCase findAllEmployeesUseCase,
            UpdateEmployeeUseCase updateEmployeeUseCase,
            DeleteEmployeeUseCase deleteEmployeeUseCase) {
        this.createEmployeeUseCase = createEmployeeUseCase;
        this.findEmployeeByIdUseCase = findEmployeeByIdUseCase;
        this.findEmployeeByPersonIdUseCase = findEmployeeByPersonIdUseCase;
        this.findAllEmployeesUseCase = findAllEmployeesUseCase;
        this.updateEmployeeUseCase = updateEmployeeUseCase;
        this.deleteEmployeeUseCase = deleteEmployeeUseCase;
    }

    @GetMapping
    @Operation(summary = "Get all employees", description = "Retrieves all employees with pagination using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<EmployeeResponse>>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) String search) {

        try {
            PaginationRequestDTO paginationRequest = new PaginationRequestDTO(page, size, sortBy, sortDirection,
                    search);
            PaginatedResponseDTO<EmployeeResponse> response = findAllEmployeesUseCase.execute(paginationRequest);

            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    HttpStatus.OK,
                    MessageUtils.getMessage(MessageKeys.CONTROLLER_RETRIEVED_SUCCESS,
                            MessageUtils.getLocalizedEntityName("employee")),
                    response,
                    null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_INVALID_PAGINATION, e.getMessage()),
                            null,
                            null));
        }
    }

    @PostMapping
    @Operation(summary = "Create a new employee", description = "Creates a new employee using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Employee already exists")
    })
    public ResponseEntity<ApiResponseDTO<EmployeeResponse>> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request) {

        try {
            logger.info("Creating employee: {}", request);
            EmployeeResponse appResponse = createEmployeeUseCase.execute(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO<>(
                            true,
                            HttpStatus.CREATED,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_CREATED_SUCCESS,
                                    MessageUtils.getLocalizedEntityName("employee")),
                            appResponse,
                            null));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.CONFLICT,
                            e.getMessage(),
                            null,
                            null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_INVALID_DATA, e.getMessage()),
                            null,
                            null));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID", description = "Retrieves an employee by ID using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format")
    })
    public ResponseEntity<ApiResponseDTO<EmployeeResponse>> getEmployeeById(@PathVariable String id) {

        try {
            Optional<EmployeeResponse> appResponse = findEmployeeByIdUseCase.execute(id);

            if (appResponse.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO<>(
                        true,
                        HttpStatus.OK,
                        MessageUtils.getMessage(MessageKeys.CONTROLLER_FOUND_SUCCESS,
                                MessageUtils.getLocalizedEntityName("employee")),
                        appResponse.get(),
                        null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(
                                false,
                                HttpStatus.NOT_FOUND,
                                MessageUtils.getMessage(MessageKeys.CONTROLLER_NOT_FOUND_WITH_ID,
                                        MessageUtils.getLocalizedEntityName("employee"), id),
                                null,
                                null));
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_INVALID_ID_FORMAT, e.getMessage()),
                            null,
                            null));
        }
    }

    @GetMapping("/by-person/{personId}")
    @Operation(summary = "Get employee by person ID", description = "Retrieves an employee by person ID using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found for person"),
            @ApiResponse(responseCode = "400", description = "Invalid person ID format")
    })
    public ResponseEntity<ApiResponseDTO<EmployeeResponse>> getEmployeeByPersonId(@PathVariable String personId) {

        try {
            EmployeeResponse appResponse = findEmployeeByPersonIdUseCase.execute(personId);

            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    HttpStatus.OK,
                    MessageUtils.getMessage(MessageKeys.CONTROLLER_FOUND_SUCCESS,
                            MessageUtils.getLocalizedEntityName("employee")),
                    appResponse,
                    null));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.NOT_FOUND,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_NOT_FOUND_FOR_PERSON,
                                    MessageUtils.getLocalizedEntityName("employee"), personId),
                            null,
                            null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_INVALID_PERSON_ID_FORMAT, e.getMessage()),
                            null,
                            null));
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update employee", description = "Updates an employee using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<ApiResponseDTO<EmployeeResponse>> updateEmployee(
            @PathVariable String id,
            @Valid @RequestBody UpdateEmployeeRequest request) {

        try {
            EmployeeResponse appResponse = updateEmployeeUseCase.execute(id, request);

            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    HttpStatus.OK,
                    MessageUtils.getMessage(MessageKeys.CONTROLLER_UPDATED_SUCCESS,
                            MessageUtils.getLocalizedEntityName("employee")),
                    appResponse,
                    null));

        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(
                                false,
                                HttpStatus.NOT_FOUND,
                                e.getMessage(),
                                null,
                                null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponseDTO<>(
                                false,
                                HttpStatus.BAD_REQUEST,
                                e.getMessage(),
                                null,
                                null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_INVALID_DATA, e.getMessage()),
                            null,
                            null));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employee", description = "Deletes an employee using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<ApiResponseDTO<Void>> deleteEmployee(@PathVariable String id) {

        try {
            deleteEmployeeUseCase.execute(id);

            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    HttpStatus.OK,
                    MessageUtils.getMessage(MessageKeys.CONTROLLER_DELETED_SUCCESS,
                            MessageUtils.getLocalizedEntityName("employee")),
                    null,
                    null));

        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(
                                false,
                                HttpStatus.NOT_FOUND,
                                e.getMessage(),
                                null,
                                null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponseDTO<>(
                                false,
                                HttpStatus.BAD_REQUEST,
                                e.getMessage(),
                                null,
                                null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_INVALID_DATA, e.getMessage()),
                            null,
                            null));
        }
    }
}
