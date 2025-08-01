package com.veraz.tasks.backend.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO.PaginationInfo;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersResponseDTO {
    private List<UserResponseDTO> users;
    private PaginationInfo pagination;
}
