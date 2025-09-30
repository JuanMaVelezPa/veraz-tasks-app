package com.veraz.tasks.backend.application.identity.commands;

import com.veraz.tasks.backend.application.business.dto.LoginResponse;
import com.veraz.tasks.backend.application.shared.services.UserResponseMapper;
import com.veraz.tasks.backend.domain.identity.entities.User;
import com.veraz.tasks.backend.domain.identity.repositories.UserRepository;
import com.veraz.tasks.backend.domain.identity.services.JwtTokenGenerator;
import com.veraz.tasks.backend.domain.identity.valueobjects.UserId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

public class ValidateTokenUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ValidateTokenUseCase.class);

    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final JwtTokenGenerator jwtTokenGenerator;

    public ValidateTokenUseCase(UserRepository userRepository,
            UserResponseMapper userResponseMapper,
            JwtTokenGenerator jwtTokenGenerator) {
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    public LoginResponse execute(String token) {
        logger.debug("Validating token: {}...", token.substring(0, Math.min(20, token.length())));

        if (!jwtTokenGenerator.validateToken(token)) {
            logger.warn("Token validation failed: Invalid or expired token");
            return LoginResponse.error("Invalid or expired token");
        }

        try {
            String userIdString = jwtTokenGenerator.getUserIdFromToken(token);
            logger.debug("Extracted user ID from token: {}", userIdString);

            UUID userId = UUID.fromString(userIdString);

            Optional<User> userOpt = userRepository.findByIdForAuthentication(UserId.of(userId));

            if (userOpt.isEmpty()) {
                logger.warn("User not found in database for ID: {}", userId);
                return LoginResponse.error("User not found");
            }

            User user = userOpt.get();
            logger.debug("User found for token validation: ID={}, Username={}, Email={}, Active={}",
                    user.getId().getValue(), user.getUsername(), user.getEmail(), user.isActive());

            if (!user.isActive()) {
                logger.warn("Inactive user token validation: {}", userId);
                return LoginResponse.error("User account is inactive");
            }

            logger.debug("Token validation successful for user: {}", userId);
            return LoginResponse.from(userResponseMapper.mapToResponse(user), token, "Token is valid");

        } catch (Exception e) {
            logger.error("Error during token validation: {}", e.getMessage(), e);
            return LoginResponse.error("Invalid token format");
        }
    }
}

