package com.veraz.tasks.backend.auth.controller;

import com.veraz.tasks.backend.auth.dto.RolesResponseDTO;
import com.veraz.tasks.backend.auth.service.RoleService;
import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.dto.ApiResponseDTO;
import com.veraz.tasks.backend.shared.util.MessageUtils;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/roles")
@Tag(name = "Role", description = "Role endpoints")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    @Operation(summary = "Get all roles", description = "Get all roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponseDTO<RolesResponseDTO>> findAll() {
        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK,
                MessageUtils.getCrudSuccess(MessageKeys.CRUD_RETRIEVED_SUCCESS, "Roles"),
                roleService.findAll(), null));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active roles", description = "Get all active roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active roles retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponseDTO<RolesResponseDTO>> findAllActive() {
        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK,
                MessageUtils.getCrudSuccess(MessageKeys.CRUD_RETRIEVED_SUCCESS, "Roles"),
                roleService.findAllActive(), null));
    }
}
