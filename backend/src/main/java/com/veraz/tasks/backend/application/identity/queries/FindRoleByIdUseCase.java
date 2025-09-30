package com.veraz.tasks.backend.application.identity.queries;

import com.veraz.tasks.backend.application.business.dto.RoleResponse;
import com.veraz.tasks.backend.domain.identity.entities.Role;
import com.veraz.tasks.backend.domain.identity.repositories.RoleRepository;
import com.veraz.tasks.backend.domain.identity.valueobjects.RoleId;

import java.util.Optional;

public class FindRoleByIdUseCase {
    
    private final RoleRepository roleRepository;
    
    public FindRoleByIdUseCase(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    
    public Optional<RoleResponse> execute(String roleIdString) {
        RoleId roleId = RoleId.of(roleIdString);
        Optional<Role> role = roleRepository.findById(roleId);
        return role.map(RoleResponse::from);
    }
}

