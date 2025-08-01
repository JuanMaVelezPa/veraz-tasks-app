package com.veraz.tasks.backend.shared.service;

import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

/**
 * Base interface for services with generic CRUD operations
 * 
 * @param <T>            Type of the entity
 * @param <ID>           Type of the entity ID
 * @param <REQUEST_DTO>  Type of the request DTO
 * @param <RESPONSE_DTO> Type of the response DTO
 */
public interface ServiceInterface<T, ID, REQUEST_DTO, RESPONSE_DTO> {

    /**
     * Finds all elements with pagination
     * 
     * @param pageable Pagination parameters
     * @return Paginated response
     */
    PaginatedResponseDTO<RESPONSE_DTO> findAll(Pageable pageable);

    /**
     * Finds an element by its ID
     * 
     * @param id ID of the element
     * @return Found element or empty if it does not exist
     */
    Optional<RESPONSE_DTO> findById(ID id);

    /**
     * Creates a new element
     * 
     * @param requestDTO DTO with the element data
     * @return Created element
     */
    RESPONSE_DTO create(REQUEST_DTO requestDTO);

    /**
     * Updates an existing element
     * 
     * @param id         ID of the element to update
     * @param requestDTO DTO with the updated data
     * @return Updated element
     */
    RESPONSE_DTO update(ID id, REQUEST_DTO requestDTO);

    /**
     * Deletes an element by its ID
     * 
     * @param id ID of the element to delete
     */
    void deleteById(ID id);

}