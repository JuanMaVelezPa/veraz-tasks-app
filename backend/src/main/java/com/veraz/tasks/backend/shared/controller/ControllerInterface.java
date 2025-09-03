package com.veraz.tasks.backend.shared.controller;

import com.veraz.tasks.backend.shared.dto.ApiResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;
import org.springframework.http.ResponseEntity;

public interface ControllerInterface<ID, CREATE_DTO, UPDATE_DTO, RESPONSE_DTO> {

    ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<RESPONSE_DTO>>> findAll(
            PaginationRequestDTO paginationRequest);

    ResponseEntity<ApiResponseDTO<RESPONSE_DTO>> findById(ID id);

    ResponseEntity<ApiResponseDTO<RESPONSE_DTO>> create(CREATE_DTO createRequest);

    ResponseEntity<ApiResponseDTO<RESPONSE_DTO>> update(ID id, UPDATE_DTO updateRequest);

    ResponseEntity<ApiResponseDTO<Void>> deleteById(ID id);
}