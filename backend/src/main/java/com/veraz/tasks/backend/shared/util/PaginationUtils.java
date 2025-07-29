package com.veraz.tasks.backend.shared.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;

public class PaginationUtils {

    public static Pageable createPageable(PaginationRequestDTO paginationRequest) {
        paginationRequest.validateAndNormalize();

        Sort sort = Sort.by(
                paginationRequest.getSortDirection().equalsIgnoreCase("desc") ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                paginationRequest.getSortBy());

        return PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize(), sort);
    }

    public static <T> PaginatedResponseDTO<T> toPaginatedResponse(Page<T> page) {
        PaginatedResponseDTO.PaginationInfo paginationInfo = PaginatedResponseDTO.PaginationInfo.builder()
                .currentPage(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .pageSize(page.getSize())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .build();

        return PaginatedResponseDTO.<T>builder()
                .data(page.getContent())
                .pagination(paginationInfo)
                .build();
    }

    public static void validatePaginationParams(int page, int size) {
        if (page < PaginationRequestDTO.MIN_PAGE) {
            throw new IllegalArgumentException("Page must be >= " + PaginationRequestDTO.MIN_PAGE);
        }
        if (size < PaginationRequestDTO.MIN_SIZE || size > PaginationRequestDTO.MAX_SIZE) {
            throw new IllegalArgumentException("Size must be between " + PaginationRequestDTO.MIN_SIZE +
                    " and " + PaginationRequestDTO.MAX_SIZE);
        }
    }
}