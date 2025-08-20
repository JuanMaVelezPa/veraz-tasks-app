package com.veraz.tasks.backend.person.controller;

import com.veraz.tasks.backend.auth.model.User;
import com.veraz.tasks.backend.auth.dto.UserUpdateRequestDTO;
import com.veraz.tasks.backend.auth.dto.UserResponseDTO;
import com.veraz.tasks.backend.person.dto.PersonCreateRequestDTO;
import com.veraz.tasks.backend.person.dto.PersonUpdateRequestDTO;
import com.veraz.tasks.backend.person.dto.PersonResponseDTO;
import com.veraz.tasks.backend.person.service.ProfileService;
import com.veraz.tasks.backend.shared.dto.ApiResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Profile", description = "Profile management endpoints - Self-service only")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get my profile", description = "Get current user's person profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "404", description = "Profile not found for current user")
    })
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> getMyProfile(@AuthenticationPrincipal User user) {
        PersonResponseDTO response = profileService.getPersonByUser(user);
        return ResponseEntity
                .ok(new ApiResponseDTO<>(true, HttpStatus.OK, "Profile fetched successfully", response, null));
    }

    @GetMapping("/exists")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Check if profile exists", description = "Check if current user has a profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile existence checked successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token.")
    })
    public ResponseEntity<ApiResponseDTO<Boolean>> checkProfileExists(@AuthenticationPrincipal User user) {
        boolean exists = profileService.hasPerson(user);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, "Profile existence checked", exists, null));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create my profile", description = "Create person profile for current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Profile created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "409", description = "Conflict - Profile already exists for user or with same email/identification")
    })
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> createMyProfile(@AuthenticationPrincipal User user,
            @Valid @RequestBody PersonCreateRequestDTO personRequestDto) {
        PersonResponseDTO response = profileService.createPersonForUser(user, personRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, HttpStatus.CREATED, "Profile created successfully", response, null));
    }

    @PatchMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update my profile", description = "Update current user's person profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "404", description = "Profile not found for current user"),
            @ApiResponse(responseCode = "409", description = "Conflict - Profile already exists with same email/identification")
    })
    public ResponseEntity<ApiResponseDTO<PersonResponseDTO>> updateMyProfile(@AuthenticationPrincipal User user,
            @Valid @RequestBody PersonUpdateRequestDTO personRequestDto) {
        PersonResponseDTO response = profileService.updatePersonForUser(user, personRequestDto);
        return ResponseEntity
                .ok(new ApiResponseDTO<>(true, HttpStatus.OK, "Profile updated successfully", response, null));
    }

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete my profile", description = "Delete current user's person profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "404", description = "Profile not found for current user")
    })
    public ResponseEntity<ApiResponseDTO<Void>> deleteMyProfile(@AuthenticationPrincipal User user) {
        profileService.deletePersonForUser(user);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, HttpStatus.OK, "Profile deleted successfully", null, null));
    }

    @GetMapping("/account")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get my account", description = "Get current user's account information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token.")
    })
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getMyUserAccount(@AuthenticationPrincipal User user) {
        UserResponseDTO response = profileService.getUserAccount(user);
        return ResponseEntity
                .ok(new ApiResponseDTO<>(true, HttpStatus.OK, "Account fetched successfully", response, null));
    }

    @PatchMapping("/account")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update my account", description = "Updates current user's account information (username, email, password, isActive)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> updateMyUserAccount(@AuthenticationPrincipal User user,
            @Valid @RequestBody UserUpdateRequestDTO userRequest) {
        UserResponseDTO response = profileService.updateUserAccount(user, userRequest);
        return ResponseEntity
                .ok(new ApiResponseDTO<>(true, HttpStatus.OK, "Account updated successfully", response, null));
    }
}
