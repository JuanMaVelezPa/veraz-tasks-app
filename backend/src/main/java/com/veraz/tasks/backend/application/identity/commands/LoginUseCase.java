package com.veraz.tasks.backend.application.identity.commands;

import com.veraz.tasks.backend.infrastructure.identity.dto.LoginRequest;
import com.veraz.tasks.backend.application.business.dto.LoginResponse;
import com.veraz.tasks.backend.application.shared.services.UserResponseMapper;
import com.veraz.tasks.backend.domain.identity.entities.User;
import com.veraz.tasks.backend.domain.identity.repositories.UserRepository;
import com.veraz.tasks.backend.domain.identity.services.PasswordEncoder;
import com.veraz.tasks.backend.domain.identity.services.JwtTokenGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class LoginUseCase {

    private static final Logger logger = LoggerFactory.getLogger(LoginUseCase.class);

    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenGenerator jwtTokenGenerator;

    public LoginUseCase(UserRepository userRepository,
            UserResponseMapper userResponseMapper,
            PasswordEncoder passwordEncoder,
            JwtTokenGenerator jwtTokenGenerator) {
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    public LoginResponse execute(LoginRequest request) {
        String usernameOrEmail = request.usernameOrEmail().toLowerCase().trim();
        logger.debug("Attempting login for: {}", usernameOrEmail);

        Optional<User> userOpt = userRepository.findByUsernameOrEmailForAuthentication(usernameOrEmail);

        if (userOpt.isEmpty()) {
            logger.warn("User not found in database: {}", usernameOrEmail);
            return LoginResponse.error("Invalid credentials");
        }

        User user = userOpt.get();
        logger.debug("User found: ID={}, Username={}, Email={}, Active={}",
                user.getId().getValue(), user.getUsername(), user.getEmail(), user.isActive());

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            logger.warn("Password mismatch for user: {}", usernameOrEmail);
            return LoginResponse.error("Invalid credentials");
        }

        if (!user.isActive()) {
            logger.warn("Inactive user attempted login: {}", usernameOrEmail);
            return LoginResponse.error("User account is inactive");
        }

        String token = jwtTokenGenerator.generateToken(user);
        
        logger.debug("Login successful for user: {}", usernameOrEmail);
        return LoginResponse.from(userResponseMapper.mapToResponse(user), token, "Login successful");
    }

    public boolean isSuccessfulLogin(LoginResponse response) {
        return response.user() != null && response.token() != null;
    }
}


