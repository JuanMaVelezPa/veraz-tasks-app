package com.veraz.tasks.backend.infrastructure.identity.controllers;

import com.veraz.tasks.backend.application.business.dto.RoleResponse;
import com.veraz.tasks.backend.application.identity.queries.FindAllActiveRolesUseCase;
import com.veraz.tasks.backend.application.identity.queries.FindRoleByIdUseCase;
import com.veraz.tasks.backend.shared.dto.ApiResponseDTO;
import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.util.MessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Role", description = "Role management endpoints using Clean Architecture")
public class RoleController {

    private final FindRoleByIdUseCase findRoleByIdUseCase;
    private final FindAllActiveRolesUseCase findAllActiveRolesUseCase;

    public RoleController(FindRoleByIdUseCase findRoleByIdUseCase,
            FindAllActiveRolesUseCase findAllActiveRolesUseCase) {
        this.findRoleByIdUseCase = findRoleByIdUseCase;
        this.findAllActiveRolesUseCase = findAllActiveRolesUseCase;
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active roles", description = "Retrieves all active roles using Clean Architecture")
    @ApiResponse(responseCode = "200", description = "Active roles retrieved successfully")
    public ResponseEntity<ApiResponseDTO<List<RoleResponse>>> getActiveRoles() {
        try {
            List<RoleResponse> activeRoles = findAllActiveRolesUseCase.execute();

            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    HttpStatus.OK,
                    MessageUtils.getMessage(MessageKeys.CONTROLLER_RETRIEVED_SUCCESS,
                            MessageUtils.getLocalizedEntityName("role")),
                    activeRoles,
                    null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Error retrieving active roles: " + e.getMessage(),
                            null,
                            null));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get role by ID", description = "Retrieves a role by ID using Clean Architecture")
    @ApiResponse(responseCode = "200", description = "Role found successfully")
    @ApiResponse(responseCode = "404", description = "Role not found")
    public ResponseEntity<ApiResponseDTO<RoleResponse>> getRoleById(@PathVariable UUID id) {
        return findRoleByIdUseCase.execute(id.toString())
                .map(role -> ResponseEntity.ok(new ApiResponseDTO<>(
                        true,
                        HttpStatus.OK,
                        MessageUtils.getMessage(MessageKeys.CONTROLLER_FOUND_SUCCESS,
                                MessageUtils.getLocalizedEntityName("role")),
                        role,
                        null)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(
                                false,
                                HttpStatus.NOT_FOUND,
                                MessageUtils.getMessage(MessageKeys.CONTROLLER_NOT_FOUND_WITH_ID,
                                        MessageUtils.getLocalizedEntityName("role"), id),
                                null,
                                null)));
    }
}
