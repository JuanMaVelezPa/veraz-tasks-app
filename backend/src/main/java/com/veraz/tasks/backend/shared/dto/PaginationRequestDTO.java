package com.veraz.tasks.backend.shared.dto;

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
    private int page = 0;
    
    @Builder.Default
    private int size = 10;
    
    @Builder.Default
    private String sortBy = "createdAt";
    
    @Builder.Default
    private String sortDirection = "desc";
    
    @Builder.Default
    private String search = "";
    
    public static final int MIN_PAGE = 0;
    public static final int MIN_SIZE = 1;
    public static final int MAX_SIZE = 100;
    public static final int DEFAULT_SIZE = 10;
    
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