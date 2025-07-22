package com.veraz.tasks.backend.person.controller;

import com.veraz.tasks.backend.person.dto.PersonRequestDto;
import com.veraz.tasks.backend.person.dto.PersonResponseDto;
import com.veraz.tasks.backend.person.dto.UserSummaryDto;
import com.veraz.tasks.backend.person.service.PersonService;

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
@RequestMapping("/person")
@Tag(name = "Person", description = "Person endpoints")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get all persons", description = "Get all persons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Persons fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    public ResponseEntity<List<PersonResponseDto>> getAllPersons() {
        return ResponseEntity.ok(personService.getPersons());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get a person by id", description = "Get a person by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    public ResponseEntity<PersonResponseDto> getPerson(@PathVariable UUID id) {
        PersonResponseDto response = personService.getPersonById(id);
        if ("NOT_FOUND".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available-users")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get all available users", description = "Get all available users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Available users fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    public ResponseEntity<List<UserSummaryDto>> getAvailableUsers() {
        return ResponseEntity.ok(personService.getAvailableUsers());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Create a person", description = "Create a person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Person created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    public ResponseEntity<PersonResponseDto> createPerson(@Valid @RequestBody PersonRequestDto personRequestDto) {
        PersonResponseDto response = personService.createPerson(personRequestDto);
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
    @Operation(summary = "Update a person", description = "Update a person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person updated successfully"),
            @ApiResponse(responseCode = "404", description = "Person or user not found"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    public ResponseEntity<PersonResponseDto> updatePerson(@PathVariable UUID id,
            @Valid @RequestBody PersonRequestDto dto) {
        PersonResponseDto response = personService.updatePerson(id, dto);
        if ("NOT_FOUND".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if ("ERROR".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Delete a person", description = "Delete a person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Person deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    public ResponseEntity<PersonResponseDto> deletePerson(@PathVariable UUID id) {
        PersonResponseDto response = personService.deletePerson(id);
        if ("NOT_FOUND".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if ("DELETED".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }
        return ResponseEntity.ok(response);
    }
}