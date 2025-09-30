package com.veraz.tasks.backend.application.identity.commands;

import com.veraz.tasks.backend.application.business.dto.UserResponse;
import com.veraz.tasks.backend.application.shared.services.UserResponseMapper;
import com.veraz.tasks.backend.domain.identity.entities.User;
import com.veraz.tasks.backend.domain.identity.repositories.UserRepository;
import com.veraz.tasks.backend.domain.identity.repositories.RoleRepository;
import com.veraz.tasks.backend.domain.identity.services.PasswordEncoder;
import com.veraz.tasks.backend.domain.identity.valueobjects.UserId;
import com.veraz.tasks.backend.domain.identity.exceptions.UserNotFoundException;
import com.veraz.tasks.backend.infrastructure.identity.dto.UpdateUserRequest;

import java.util.List;

public class UpdateUserUseCase {

    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UpdateUserUseCase(UserRepository userRepository, UserResponseMapper userResponseMapper,
            PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public UserResponse execute(String userIdString, UpdateUserRequest request) {

        UserId userId = UserId.of(userIdString);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userIdString));

        if (request.username() != null) {
            boolean exists = userRepository.existsByUsername(request.username());
            if (exists) {
                throw new IllegalArgumentException("Username already exists: ");
            }
            user.changeUsername(request.username());
        }
        if (request.password() != null) {
            user.changePassword(request.password(), passwordEncoder);
        }
        if (request.email() != null) {
            boolean exists = userRepository.existsByEmail(request.email());
            if (exists) {
                throw new IllegalArgumentException("Email already exists: ");
            }
            user.changeEmail(request.email());
        }

        if (request.isActive() != null) {
            if (request.isActive()) {
                user.activate();
            } else {
                user.deactivate();
            }
        }
        User updatedUser;
        if (request.roles() != null) {
            updateUserRoles(user, request.roles());
            updatedUser = userRepository.save(user);
        } else {
            updatedUser = userRepository.updateBasicFields(user);
        }

        return userResponseMapper.mapToResponse(updatedUser);
    }

    private void updateUserRoles(User user, List<String> roleNames) {
        user.clearRoles();

        for (String roleName : roleNames) {
            if (roleName != null && !roleName.trim().isEmpty()) {
                roleRepository.findByName(roleName.trim())
                        .ifPresent(role -> user.assignRole(role.getId()));
            }
        }
    }
}
