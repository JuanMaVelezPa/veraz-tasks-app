package com.veraz.tasks.backend.shared.controller;

import com.veraz.tasks.backend.shared.dto.ApiResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Base interface for controllers with generic CRUD operations
 * 
 * @param <T>            Type of the entity
 * @param <ID>           Type of the entity ID
 * @param <REQUEST_DTO>  Type of the request DTO
 * @param <RESPONSE_DTO> Type of the response DTO
 */
public interface ControllerInterface<ID, REQUEST_DTO, RESPONSE_DTO> {

    /**
     * Gets all elements with pagination
     * 
     * @param paginationRequest Pagination parameters
     * @return Paginated response
     */
    @GetMapping
    ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<RESPONSE_DTO>>> findAll(
            @ModelAttribute PaginationRequestDTO paginationRequest);

    /**
     * Searches for an element by its ID
     * 
     * @param id ID of the element
     * @return Found element
     */
    @GetMapping("/{id}")
    ResponseEntity<ApiResponseDTO<RESPONSE_DTO>> findById(@PathVariable ID id);

    /**
     * Creates a new element
     * 
     * @param requestDTO DTO with the element data
     * @return Created element
     */
    @PostMapping
    ResponseEntity<ApiResponseDTO<RESPONSE_DTO>> create(@RequestBody REQUEST_DTO requestDTO);

    /**
     * Updates an existing element
     * 
     * @param id         ID of the element to update
     * @param requestDTO DTO with the updated data
     * @return Elemento actualizado
     */
    @PatchMapping("/{id}")
    ResponseEntity<ApiResponseDTO<RESPONSE_DTO>> update(@PathVariable ID id, @RequestBody REQUEST_DTO requestDTO);

    /**
     * Deletes an element by its ID
     * 
     * @param id ID of the element to delete
     * @return Confirmation response
     */
    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponseDTO<Void>> deleteById(@PathVariable ID id);
}