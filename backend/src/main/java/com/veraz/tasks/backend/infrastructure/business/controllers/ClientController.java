package com.veraz.tasks.backend.infrastructure.business.controllers;

import com.veraz.tasks.backend.application.business.commands.CreateClientUseCase;

import com.veraz.tasks.backend.application.business.commands.UpdateClientUseCase;
import com.veraz.tasks.backend.application.business.dto.ClientResponse;
import com.veraz.tasks.backend.application.business.queries.FindAllClientsUseCase;
import com.veraz.tasks.backend.application.business.queries.FindClientByIdUseCase;
import com.veraz.tasks.backend.application.business.queries.FindClientByPersonIdUseCase;
import com.veraz.tasks.backend.application.business.commands.DeleteClientUseCase;
import com.veraz.tasks.backend.infrastructure.business.dto.CreateClientRequest;
import com.veraz.tasks.backend.infrastructure.business.dto.UpdateClientRequest;
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

import java.util.Optional;

/**
 * Client REST controller
 */
@RestController
@RequestMapping("/clients")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Client", description = "Client management endpoints using Clean Architecture")
public class ClientController {

    private final CreateClientUseCase createClientUseCase;
    private final FindClientByIdUseCase findClientByIdUseCase;
    private final FindClientByPersonIdUseCase findClientByPersonIdUseCase;
    private final FindAllClientsUseCase findAllClientsUseCase;
    private final UpdateClientUseCase updateClientUseCase;
    private final DeleteClientUseCase deleteClientUseCase;

    public ClientController(CreateClientUseCase createClientUseCase,
            FindClientByIdUseCase findClientByIdUseCase,
            FindClientByPersonIdUseCase findClientByPersonIdUseCase,
            FindAllClientsUseCase findAllClientsUseCase,
            UpdateClientUseCase updateClientUseCase,
            DeleteClientUseCase deleteClientUseCase) {
        this.createClientUseCase = createClientUseCase;
        this.findClientByIdUseCase = findClientByIdUseCase;
        this.findClientByPersonIdUseCase = findClientByPersonIdUseCase;
        this.findAllClientsUseCase = findAllClientsUseCase;
        this.updateClientUseCase = updateClientUseCase;
        this.deleteClientUseCase = deleteClientUseCase;
    }

    @GetMapping
    @Operation(summary = "Get all clients", description = "Retrieves all clients with pagination using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clients retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<ClientResponse>>> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) String search) {

        try {
            PaginationRequestDTO paginationRequest = new PaginationRequestDTO(page, size, sortBy, sortDirection,
                    search);
            PaginatedResponseDTO<ClientResponse> response = findAllClientsUseCase.execute(paginationRequest);

            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    HttpStatus.OK,
                    MessageUtils.getMessage(MessageKeys.CONTROLLER_RETRIEVED_SUCCESS,
                            MessageUtils.getLocalizedEntityName("client")),
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
    @Operation(summary = "Create a new client", description = "Creates a new client using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Client already exists")
    })
    public ResponseEntity<ApiResponseDTO<ClientResponse>> createClient(
            @Valid @RequestBody CreateClientRequest request) {

        try {

            ClientResponse appResponse = createClientUseCase.execute(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO<>(
                            true,
                            HttpStatus.CREATED,
                            MessageUtils.getMessage(MessageKeys.CONTROLLER_CREATED_SUCCESS,
                                    MessageUtils.getLocalizedEntityName("client")),
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
    @Operation(summary = "Get client by ID", description = "Retrieves a client by ID using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client found successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format")
    })
    public ResponseEntity<ApiResponseDTO<ClientResponse>> getClientById(@PathVariable String id) {

        try {
            Optional<ClientResponse> appResponse = findClientByIdUseCase.execute(id);

            if (appResponse.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO<>(
                        true,
                        HttpStatus.OK,
                        MessageUtils.getMessage(MessageKeys.CONTROLLER_FOUND_SUCCESS,
                                MessageUtils.getLocalizedEntityName("client")),
                        appResponse.get(),
                        null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(
                                false,
                                HttpStatus.NOT_FOUND,
                                MessageUtils.getMessage(MessageKeys.CONTROLLER_NOT_FOUND_WITH_ID,
                                        MessageUtils.getLocalizedEntityName("client"), id),
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
    @Operation(summary = "Get client by person ID", description = "Retrieves a client by person ID using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client found successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found for person"),
            @ApiResponse(responseCode = "400", description = "Invalid person ID format")
    })
    public ResponseEntity<ApiResponseDTO<ClientResponse>> getClientByPersonId(@PathVariable String personId) {

        try {
            ClientResponse appResponse = findClientByPersonIdUseCase.execute(personId);

            if (appResponse == null) {
                // No se encontró cliente para esta persona - esto es normal
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(
                                false,
                                HttpStatus.NOT_FOUND,
                                "No client found for person ID: " + personId,
                                null,
                                null));
            }

            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    HttpStatus.OK,
                    MessageUtils.getMessage(MessageKeys.CONTROLLER_FOUND_SUCCESS,
                            MessageUtils.getLocalizedEntityName("client")),
                    appResponse,
                    null));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.BAD_REQUEST,
                            e.getMessage(),
                            null,
                            null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(
                            false,
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Internal server error occurred",
                            null,
                            null));
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update client", description = "Updates a client using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<ApiResponseDTO<ClientResponse>> updateClient(
            @PathVariable String id,
            @Valid @RequestBody UpdateClientRequest request) {

        try {
            ClientResponse appResponse = updateClientUseCase.execute(id, request);

            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    HttpStatus.OK,
                    MessageUtils.getMessage(MessageKeys.CONTROLLER_UPDATED_SUCCESS,
                            MessageUtils.getLocalizedEntityName("client")),
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
    @Operation(summary = "Delete client", description = "Deletes a client using Clean Architecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<ApiResponseDTO<Void>> deleteClient(@PathVariable String id) {

        try {
            deleteClientUseCase.execute(id);

            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    HttpStatus.OK,
                    MessageUtils.getMessage(MessageKeys.CONTROLLER_DELETED_SUCCESS,
                            MessageUtils.getLocalizedEntityName("client")),
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
