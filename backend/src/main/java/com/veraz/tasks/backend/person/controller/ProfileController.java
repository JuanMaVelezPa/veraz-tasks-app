package com.veraz.tasks.backend.person.controller;

import com.veraz.tasks.backend.auth.model.User;
import com.veraz.tasks.backend.person.dto.PersonRequestDto;
import com.veraz.tasks.backend.person.dto.PersonResponseDto;
import com.veraz.tasks.backend.person.service.ProfileService;

import io.swagger.v3.oas.annotations.tags.Tag;

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
    public ResponseEntity<PersonResponseDto> getMyPerson(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(profileService.getPersonByUser(user));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PersonResponseDto> createMyPerson(@AuthenticationPrincipal User user,
            @Valid @RequestBody PersonRequestDto personRequestDto) {
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(profileService.createPersonForUser(user, personRequestDto));
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PersonResponseDto> updateMyPerson(@AuthenticationPrincipal User user,
            @Valid @RequestBody PersonRequestDto personRequestDto) {
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(profileService.updatePersonForUser(user, personRequestDto));
    }

}