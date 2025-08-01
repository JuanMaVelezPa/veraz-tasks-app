package com.veraz.tasks.backend.shared.dto;

import com.veraz.tasks.backend.shared.constants.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginationRequestDTO {
    
    @Builder.Default
    private int page = AppConstants.DEFAULT_PAGE_NUMBER;
    
    @Builder.Default
    private int size = AppConstants.DEFAULT_PAGE_SIZE;
    
    @Builder.Default
    private String sortBy = AppConstants.DEFAULT_SORT_BY;
    
    @Builder.Default
    private String sortDirection = AppConstants.DEFAULT_SORT_DIRECTION;
    
    @Builder.Default
    private String search = "";
    
    public static final int MIN_PAGE = AppConstants.DEFAULT_PAGE_NUMBER;
    public static final int MIN_SIZE = AppConstants.MIN_PAGE_SIZE;
    public static final int MAX_SIZE = AppConstants.MAX_PAGE_SIZE;
    public static final int DEFAULT_SIZE = AppConstants.DEFAULT_PAGE_SIZE;
    
    public void validateAndNormalize() {
        if (page < MIN_PAGE) {
            page = MIN_PAGE;
        }
        if (size < MIN_SIZE) {
            size = DEFAULT_SIZE;
        }
        if (size > MAX_SIZE) {
            size = MAX_SIZE;
        }
        if (sortDirection == null
                || (!sortDirection.equalsIgnoreCase("asc")
                        && !sortDirection.equalsIgnoreCase("desc"))) {
            sortDirection = "desc";
        }
        if (search == null) {
            search = "";
        }
    }
    
    public boolean hasSearch() {
        return search != null && !search.trim().isEmpty();
    }
}