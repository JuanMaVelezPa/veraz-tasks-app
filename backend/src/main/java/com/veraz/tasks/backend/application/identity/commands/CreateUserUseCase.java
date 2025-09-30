package com.veraz.tasks.backend.application.identity.commands;

import com.veraz.tasks.backend.infrastructure.identity.dto.CreateUserRequest;
import com.veraz.tasks.backend.application.business.dto.UserResponse;
import com.veraz.tasks.backend.application.shared.services.UserResponseMapper;
import com.veraz.tasks.backend.domain.identity.entities.User;
import com.veraz.tasks.backend.domain.identity.entities.Role;
import com.veraz.tasks.backend.domain.identity.repositories.UserRepository;
import com.veraz.tasks.backend.domain.identity.services.PasswordEncoder;
import com.veraz.tasks.backend.domain.identity.repositories.RoleRepository;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateUserUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreateUserUseCase.class);

    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public CreateUserUseCase(UserRepository userRepository, UserResponseMapper userResponseMapper,
            PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public UserResponse execute(CreateUserRequest request) {
        logger.info("=== CREATE USER PROCESS START ===");
        logger.info("Request received - Username: {}, Email: {}, Roles: {}",
                request.username(), request.email(), request.roles());

        if (userRepository.existsByUsername(request.username())) {
            logger.error("Username already exists: {}", request.username());
            throw new IllegalArgumentException("User with username " +
                    request.username() + " already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            logger.error("Email already exists: {}", request.email());
            throw new IllegalArgumentException("User with email " +
                    request.email() + " already exists");
        }

        logger.info("Creating user with username: {} and email: {}", request.username(), request.email());
        User user = User.create(
                request.username(),
                request.password(),
                request.email(),
                passwordEncoder);

        logger.info("User created successfully with ID: {}", user.getId().getValue());

        if (request.roles() != null && !request.roles().isEmpty()) {
            logger.info("Roles provided: {}, attempting to assign roles", request.roles());
            for (String roleName : request.roles()) {
                if (roleName != null && !roleName.trim().isEmpty()) {
                    try {

                        Optional<Role> roleOpt = roleRepository.findByName(roleName.trim());

                        if (roleOpt.isPresent()) {
                            Role role = roleOpt.get();
                            logger.info("Role found: {} (ID: {}), assigning to user", role.getName(),
                                    role.getId().getValue());
                            user.assignRole(role.getId());
                            logger.info("Role {} assigned to user {}", role.getName(), user.getId().getValue());
                        } else {
                            logger.warn("Role not found with name: {}, skipping role assignment", roleName);
                        }
                    } catch (Exception e) {
                        logger.error("Error assigning role {} to user: {}", roleName, e.getMessage());

                    }
                }
            }
        } else {
            logger.warn("No roles provided, user will be created without roles");
        }

        logger.info("Saving user to database");
        User savedUser = userRepository.save(user);
        logger.info("User saved successfully with ID: {}", savedUser.getId().getValue());

        UserResponse response = userResponseMapper.mapToResponse(savedUser);
        logger.info("=== CREATE USER PROCESS END ===");
        return response;
    }
}
