package com.veraz.tasks.backend.shared.dto;

import java.util.List;

public record PaginatedResponseDTO<T>(
        List<T> data,
        PaginationInfo pagination) {

    public record PaginationInfo(
            int currentPage,
            int totalPages,
            long totalElements,
            int pageSize,
            boolean hasNext,
            boolean hasPrevious,
            boolean isFirst,
            boolean isLast) {

        public static PaginationInfo from(org.springframework.data.domain.Page<?> page) {
            return new PaginationInfo(
                    page.getNumber(),
                    page.getTotalPages(),
                    page.getTotalElements(),
                    page.getSize(),
                    page.hasNext(),
                    page.hasPrevious(),
                    page.isFirst(),
                    page.isLast());
        }
    }

    public static <T> PaginatedResponseDTO<T> from(org.springframework.data.domain.Page<T> page) {
        return new PaginatedResponseDTO<>(
                page.getContent(),
                PaginationInfo.from(page));
    }
}