package com.veraz.tasks.backend.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolesResponseDTO {

    private List<RoleResponseDTO> roles;

}
