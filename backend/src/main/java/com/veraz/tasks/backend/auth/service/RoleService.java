package com.veraz.tasks.backend.auth.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veraz.tasks.backend.auth.model.Role;
import com.veraz.tasks.backend.auth.dto.RoleResponseDTO;
import com.veraz.tasks.backend.auth.dto.RolesResponseDTO;
import com.veraz.tasks.backend.auth.repository.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public RolesResponseDTO findAll() {
        List<Role> roles = roleRepository.findAll();
        List<RoleResponseDTO> roleResponseDTOs = roles.stream()
                .map(role -> new RoleResponseDTO(role.getName(), role.getDescription(), role.getIsActive()))
                .collect(Collectors.toList());
        return new RolesResponseDTO(roleResponseDTOs);
    }

    @Transactional(readOnly = true)
    public RolesResponseDTO findAllActive() {
        List<Role> roles = roleRepository.findAllByIsActiveTrue();
        List<RoleResponseDTO> roleResponseDTOs = roles.stream()
                .map(role -> new RoleResponseDTO(role.getName(), role.getDescription(), role.getIsActive()))
                .collect(Collectors.toList());
        return new RolesResponseDTO(roleResponseDTOs);
    }

}
