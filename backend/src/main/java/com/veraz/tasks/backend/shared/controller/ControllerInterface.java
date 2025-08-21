package com.veraz.tasks.backend.shared.controller;

import com.veraz.tasks.backend.shared.dto.ApiResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;
import org.springframework.http.ResponseEntity;

/**
 * Base interface for controllers with generic CRUD operations
 * 
 * @param <ID>           Type of the entity ID
 * @param <CREATE_DTO>   Type of the create request DTO
 * @param <UPDATE_DTO>   Type of the update request DTO
 * @param <RESPONSE_DTO> Type of the response DTO
 */
public interface ControllerInterface<ID, CREATE_DTO, UPDATE_DTO, RESPONSE_DTO> {

    /**
     * Gets all elements with pagination
     * 
     * @param paginationRequest Pagination parameters
     * @return Paginated response
     */
    ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<RESPONSE_DTO>>> findAll(
            PaginationRequestDTO paginationRequest);

    /**
     * Searches for an element by its ID
     * 
     * @param id ID of the element
     * @return Found element
     */
    ResponseEntity<ApiResponseDTO<RESPONSE_DTO>> findById(ID id);

    /**
     * Creates a new element
     * 
     * @param requestDTO DTO with the element data for creation
     * @return Created element
     */
    ResponseEntity<ApiResponseDTO<RESPONSE_DTO>> create(CREATE_DTO requestDTO);

    /**
     * Updates an existing element
     * 
     * @param id         ID of the element to update
     * @param requestDTO DTO with the updated data
     * @return Updated element
     */
    ResponseEntity<ApiResponseDTO<RESPONSE_DTO>> update(ID id, UPDATE_DTO requestDTO);

    /**
     * Deletes an element by its ID
     * 
     * @param id ID of the element to delete
     * @return Confirmation response
     */
    ResponseEntity<ApiResponseDTO<Void>> deleteById(ID id);
}