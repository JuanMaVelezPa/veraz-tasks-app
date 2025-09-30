package com.veraz.tasks.backend.application.identity.queries;

import com.veraz.tasks.backend.application.business.dto.UserResponse;
import com.veraz.tasks.backend.application.shared.services.UserResponseMapper;
import com.veraz.tasks.backend.domain.identity.repositories.UserRepository;
import com.veraz.tasks.backend.domain.identity.valueobjects.UserId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class FindUserByIdUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindUserByIdUseCase.class);

    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;

    public FindUserByIdUseCase(UserRepository userRepository, UserResponseMapper userResponseMapper) {
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
    }

    public Optional<UserResponse> execute(String userIdString) {
        logger.debug("Finding user by ID: {}", userIdString);

        try {
            UserId userId = UserId.of(userIdString);
            logger.debug("Created UserId object: {}", userId.getValue());

            return userRepository.findById(userId)
                    .map(user -> {
                        logger.debug("User found: ID={}, Username={}, Email={}, Active={}",
                                user.getId().getValue(), user.getUsername(),
                                user.getEmail(), user.isActive());
                        return userResponseMapper.mapToResponse(user);
                    })
                    .or(() -> {
                        logger.warn("User not found in database for ID: {}", userIdString);
                        return Optional.empty();
                    });

        } catch (Exception e) {
            logger.error("Error finding user by ID {}: {}", userIdString, e.getMessage(), e);
            return Optional.empty();
        }
    }
}

