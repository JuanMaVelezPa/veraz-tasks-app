package com.veraz.tasks.backend.shared.dto;

import com.veraz.tasks.backend.shared.constants.AppConstants;

public record PaginationRequestDTO(
        int page,
        int size,
        String sortBy,
        String sortDirection,
        String search) {

    public static final int MIN_PAGE = AppConstants.DEFAULT_PAGE_NUMBER;
    public static final int MIN_SIZE = AppConstants.MIN_PAGE_SIZE;
    public static final int MAX_SIZE = AppConstants.MAX_PAGE_SIZE;
    public static final int DEFAULT_SIZE = AppConstants.DEFAULT_PAGE_SIZE;

    public PaginationRequestDTO() {
        this(
                AppConstants.DEFAULT_PAGE_NUMBER,
                AppConstants.DEFAULT_PAGE_SIZE,
                AppConstants.DEFAULT_SORT_BY,
                AppConstants.DEFAULT_SORT_DIRECTION,
                "");
    }

    public PaginationRequestDTO {
        if (page < MIN_PAGE) {
            page = MIN_PAGE;
        }

        if (size < MIN_SIZE) {
            size = DEFAULT_SIZE;
        }
        if (size > MAX_SIZE) {
            size = MAX_SIZE;
        }

        if (sortDirection == null ||
                (!sortDirection.equalsIgnoreCase("asc") && !sortDirection.equalsIgnoreCase("desc"))) {
            sortDirection = "desc";
        }

        if (search == null) {
            search = "";
        }

        if (sortBy == null || sortBy.trim().isEmpty()) {
            sortBy = AppConstants.DEFAULT_SORT_BY;
        }
    }

    public boolean hasSearch() {
        return search != null && !search.trim().isEmpty();
    }

    public static PaginationRequestDTO withDefaults() {
        return new PaginationRequestDTO();
    }

    public static PaginationRequestDTO of(int page, int size) {
        return new PaginationRequestDTO(page, size, AppConstants.DEFAULT_SORT_BY, AppConstants.DEFAULT_SORT_DIRECTION,
                "");
    }

    public static PaginationRequestDTO of(int page, int size, String sortBy, String sortDirection) {
        return new PaginationRequestDTO(page, size, sortBy, sortDirection, "");
    }
}