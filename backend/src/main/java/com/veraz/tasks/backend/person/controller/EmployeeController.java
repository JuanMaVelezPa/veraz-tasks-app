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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veraz.tasks.backend.person.dto.EmployeeCreateRequestDTO;
import com.veraz.tasks.backend.person.dto.EmployeeUpdateRequestDTO;
import com.veraz.tasks.backend.person.dto.EmployeeResponseDTO;
import com.veraz.tasks.backend.person.service.EmployeeService;
import com.veraz.tasks.backend.shared.controller.ControllerInterface;
import com.veraz.tasks.backend.shared.dto.ApiResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;
import com.veraz.tasks.backend.shared.util.PaginationUtils;
import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.util.MessageUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/employees")
@Tag(name = "Employee", description = "Employee management endpoints")
public class EmployeeController
        implements ControllerInterface<UUID, EmployeeCreateRequestDTO, EmployeeUpdateRequestDTO, EmployeeResponseDTO> {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    @Operation(summary = "Get all employees", description = "Admin/Manager/Supervisor access")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("@permissionService.canReadResources()")
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<EmployeeResponseDTO>>> findAll(
            @ModelAttribute PaginationRequestDTO paginationRequest) {

        paginationRequest.validateAndNormalize();
        Pageable pageable = PaginationUtils.createPageable(paginationRequest);

        PaginatedResponseDTO<EmployeeResponseDTO> response = paginationRequest.hasSearch()
                ? employeeService.findBySearch(paginationRequest.getSearch(), pageable)
                : employeeService.findAll(pageable);

        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK,
                MessageUtils.getCrudSuccessMessage(MessageKeys.CRUD_RETRIEVED_SUCCESS, "Employees"),
                response, null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID", description = "Admin/Manager/Supervisor access OR resource ownership")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PreAuthorize("@permissionService.canAccessResource(#id, 'EMPLOYEE') or @permissionService.hasAdminAccess()")
    public ResponseEntity<ApiResponseDTO<EmployeeResponseDTO>> findById(@PathVariable UUID id) {
        Optional<EmployeeResponseDTO> response = employeeService.findById(id);

        if (response.isPresent()) {
            return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK,
                    MessageUtils.getCrudSuccessMessage(MessageKeys.CRUD_RETRIEVED_SUCCESS, "Employee"),
                    response.get(), null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, HttpStatus.NOT_FOUND,
                            MessageUtils.getEntityNotFoundMessage("Employee"), null, null));
        }
    }

    @PostMapping
    @Operation(summary = "Create employee", description = "Admin/Manager access OR create for own person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("@permissionService.canCreateEmployee(#createRequest.personId)")
    public ResponseEntity<ApiResponseDTO<EmployeeResponseDTO>> create(
            @Valid @RequestBody EmployeeCreateRequestDTO createRequest) {
        EmployeeResponseDTO response = employeeService.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, HttpStatus.CREATED,
                        MessageUtils.getCrudSuccessMessage(MessageKeys.CRUD_CREATED_SUCCESS, "Employee"),
                        response, null));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update employee", description = "Admin/Manager access OR employee ownership")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PreAuthorize("@permissionService.canWriteResources() or @permissionService.isEmployeeOwner(#id)")
    public ResponseEntity<ApiResponseDTO<EmployeeResponseDTO>> update(@PathVariable UUID id,
            @Valid @RequestBody EmployeeUpdateRequestDTO updateRequest) {
        EmployeeResponseDTO response = employeeService.update(id, updateRequest);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK,
                MessageUtils.getCrudSuccessMessage(MessageKeys.CRUD_UPDATED_SUCCESS, "Employee"),
                response, null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employee", description = "Admin/Manager access only")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("@permissionService.canWriteResources()")
    public ResponseEntity<ApiResponseDTO<Void>> deleteById(@PathVariable UUID id) {
        employeeService.deleteById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK,
                MessageUtils.getCrudSuccessMessage(MessageKeys.CRUD_DELETED_SUCCESS, "Employee"),
                null, null));
    }

    @GetMapping("/by-person/{personId}")
    @Operation(summary = "Get employee by person ID", description = "Admin/Manager/Supervisor access OR person ownership")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PreAuthorize("@permissionService.canReadResources() or @permissionService.isPersonOwner(#personId)")
    public ResponseEntity<ApiResponseDTO<EmployeeResponseDTO>> findByPersonId(@PathVariable UUID personId) {
        Optional<EmployeeResponseDTO> response = employeeService.findByPersonId(personId);

        if (response.isPresent()) {
            return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK,
                    MessageUtils.getCrudSuccessMessage(MessageKeys.CRUD_RETRIEVED_SUCCESS, "Employee"),
                    response.get(), null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, HttpStatus.NOT_FOUND,
                            MessageUtils.getEntityNotFoundMessage("Employee"), null, null));
        }
    }

}