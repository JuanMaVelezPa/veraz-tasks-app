package com.veraz.tasks.backend.auth.dto;

import java.util.List;

import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO.PaginationInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersResponseDTO {

    private List<UserDetailDto> users;
    private PaginationInfo pagination;

}
