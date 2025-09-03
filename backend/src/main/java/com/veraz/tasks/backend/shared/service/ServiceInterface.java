package com.veraz.tasks.backend.shared.service;

import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

public interface ServiceInterface<T, ID, CREATE_DTO, UPDATE_DTO, RESPONSE_DTO> {

    PaginatedResponseDTO<RESPONSE_DTO> findAll(Pageable pageable);

    Optional<RESPONSE_DTO> findById(ID id);

    RESPONSE_DTO create(CREATE_DTO createRequest);

    RESPONSE_DTO update(ID id, UPDATE_DTO updateRequest);

    void deleteById(ID id);
}