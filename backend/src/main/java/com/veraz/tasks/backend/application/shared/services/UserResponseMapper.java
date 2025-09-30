package com.veraz.tasks.backend.application.shared.services;

import com.veraz.tasks.backend.application.business.dto.UserResponse;
import com.veraz.tasks.backend.domain.identity.entities.User;
import com.veraz.tasks.backend.domain.identity.repositories.RoleRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for mapping User domain entities to UserResponse DTOs
 * Handles role name resolution and response creation
 */
public class UserResponseMapper {
    
    private final RoleRepository roleRepository;
    
    public UserResponseMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    
    /**
     * Maps a User domain entity to UserResponse with role names
     * 
     * @param user the user domain entity
     * @return UserResponse with resolved role names
     */
    public UserResponse mapToResponse(User user) {
        List<String> roleNames = user.getUserRoles().stream()
                .map(userRole -> roleRepository.findById(userRole.getRoleId())
                        .map(role -> role.getName())
                        .orElse("UNKNOWN"))
                .collect(Collectors.toList());
        
        return UserResponse.from(user, roleNames);
    }
}

