package com.veraz.tasks.backend.person.controller;

import com.veraz.tasks.backend.auth.model.User;
import com.veraz.tasks.backend.person.dto.PersonRequestDto;
import com.veraz.tasks.backend.person.dto.PersonResponseDto;
import com.veraz.tasks.backend.person.service.ProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/profile")
@Tag(name = "Profile", description = "Profile endpoints")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get my person", description = "Get my person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "404", description = "User not found or person not associated")
    })
    public ResponseEntity<PersonResponseDto> getMyPerson(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        PersonResponseDto response = profileService.getPersonByUser(user);
        if (response.getPerson() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create my person", description = "Create my person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Person created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "409", description = "Conflict - Person already exists for user")
    })
    public ResponseEntity<PersonResponseDto> createMyPerson(@AuthenticationPrincipal User user,
            @Valid @RequestBody PersonRequestDto personRequestDto) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        PersonResponseDto response = profileService.createPersonForUser(user, personRequestDto);
        if ("CREATED".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else if ("ERROR".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else if (response.getMessage() != null && response.getMessage().contains("already exists")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update my person", description = "Update my person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "404", description = "Person not found for user")
    })
    public ResponseEntity<PersonResponseDto> updateMyPerson(@AuthenticationPrincipal User user,
            @Valid @RequestBody PersonRequestDto personRequestDto) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        PersonResponseDto response = profileService.updatePersonForUser(user, personRequestDto);
        if ("NOT_FOUND".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if ("ERROR".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        return ResponseEntity.ok(response);
    }

}