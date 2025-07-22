package com.veraz.tasks.backend.person.controller;

import com.veraz.tasks.backend.person.dto.ClientRequestDto;
import com.veraz.tasks.backend.person.dto.ClientResponseDto;
import com.veraz.tasks.backend.person.service.ClientService;

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
@RequestMapping("/client")
@Tag(name = "Client", description = "Client endpoints")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get all clients", description = "Get all clients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clients fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    public ResponseEntity<List<ClientResponseDto>> getClients() {
        return ResponseEntity.ok(clientService.getClients());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get a client by id", description = "Get a client by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    public ResponseEntity<ClientResponseDto> getClient(@PathVariable UUID id) {
        ClientResponseDto response = clientService.getClientById(id);
        if ("NOT_FOUND".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Create a client", description = "Create a client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    public ResponseEntity<ClientResponseDto> createClient(@Valid @RequestBody ClientRequestDto dto) {
        ClientResponseDto response = clientService.createClient(dto);
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
    @Operation(summary = "Update a client", description = "Update a client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated successfully"),
            @ApiResponse(responseCode = "404", description = "Client or person not found"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    public ResponseEntity<ClientResponseDto> updateClient(@PathVariable UUID id,
            @Valid @RequestBody ClientRequestDto dto) {
        ClientResponseDto response = clientService.updateClient(id, dto);
        if ("NOT_FOUND".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if ("ERROR".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Delete a client", description = "Delete a client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - No token or invalid/expired token."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions.")
    })
    public ResponseEntity<ClientResponseDto> deleteClient(@PathVariable UUID id) {
        ClientResponseDto response = clientService.deleteClient(id);
        if ("NOT_FOUND".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if ("DELETED".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }
        return ResponseEntity.ok(response);
    }
}