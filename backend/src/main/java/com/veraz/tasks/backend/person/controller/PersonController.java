package com.veraz.tasks.backend.person.controller;

import com.veraz.tasks.backend.auth.model.User;
import com.veraz.tasks.backend.auth.repository.UserRepository;
import com.veraz.tasks.backend.person.dto.PersonRequestDto;
import com.veraz.tasks.backend.person.dto.PersonResponseDto;
import com.veraz.tasks.backend.person.dto.UserSummaryDto;
import com.veraz.tasks.backend.person.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/person")
@Tag(name = "Person", description = "Person endpoints")
public class PersonController {

    private final PersonService personService;
    private final UserRepository userRepository;

    public PersonController(PersonService personService, UserRepository userRepository) {
        this.personService = personService;
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<PersonResponseDto>> getAllPersons() {
        return ResponseEntity.ok(personService.getAllPersons());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<PersonResponseDto> getPerson(@PathVariable UUID id) {
        return ResponseEntity.ok(personService.getPersonById(id));
    }

    @GetMapping("/available-users")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<UserSummaryDto>> getAvailableUsers() {
        // Obtener todos los usuarios que no tienen persona asociada
        List<User> allUsers = userRepository.findAll();
        List<UserSummaryDto> availableUsers = allUsers.stream()
                .filter(user -> user.getPerson() == null)
                .map(user -> UserSummaryDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .isActive(user.getIsActive())
                        .createdAt(user.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(availableUsers);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<PersonResponseDto> createPerson(@Valid @RequestBody PersonRequestDto personRequestDto) {
        return ResponseEntity.ok(personService.createPerson(personRequestDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<PersonResponseDto> updatePerson(@PathVariable UUID id,
            @Valid @RequestBody PersonRequestDto dto) {
        return ResponseEntity.ok(personService.updatePerson(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<PersonResponseDto> deletePerson(@PathVariable UUID id) {
        return ResponseEntity.ok(personService.deletePerson(id));
    }
}