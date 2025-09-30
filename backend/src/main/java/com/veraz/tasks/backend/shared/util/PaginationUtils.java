package com.veraz.tasks.backend.shared.util;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;

public class PaginationUtils {

    public static Pageable createPageable(PaginationRequestDTO paginationRequest) {
        Sort sort = Sort.by(
                paginationRequest.sortDirection().equalsIgnoreCase("desc") ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                paginationRequest.sortBy());

        return PageRequest.of(paginationRequest.page(), paginationRequest.size(), sort);
    }

    public static <T> PaginatedResponseDTO<T> toPaginatedResponse(Page<T> page) {
        return PaginatedResponseDTO.from(page);
    }

    public static <T, R> PaginatedResponseDTO<R> toPaginatedResponse(Page<T> page, Function<T, R> mapper) {
        List<R> mappedContent = page.getContent().stream()
                .map(mapper)
                .collect(Collectors.toList());

        PaginatedResponseDTO.PaginationInfo paginationInfo = PaginatedResponseDTO.PaginationInfo.from(page);

        return new PaginatedResponseDTO<>(mappedContent, paginationInfo);
    }

    public static PaginatedResponseDTO.PaginationInfo buildPaginationInfo(Page<?> page) {
        return PaginatedResponseDTO.PaginationInfo.from(page);
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