package com.veraz.tasks.backend.auth.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veraz.tasks.backend.auth.config.JwtUtils;
import com.veraz.tasks.backend.auth.dto.AuthResponseDTO;
import com.veraz.tasks.backend.auth.dto.AuthRequestDTO;
import com.veraz.tasks.backend.auth.mapper.UserMapper;
import com.veraz.tasks.backend.auth.model.User;
import com.veraz.tasks.backend.auth.repository.UserRepository;
import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.util.MessageUtils;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Transactional(readOnly = true)
    public User getUserByIDAuth(UUID id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.getRoles().size(); // Force lazy loading
        }
        return user;
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO signInUser(AuthRequestDTO authRequest) {
        try {
            User user = userRepository.findByUsernameOrEmailAllIgnoreCase(
                    authRequest.getUsernameOrEmail(),
                    authRequest.getUsernameOrEmail())
                    .orElse(null);

            if (user == null) {
                logger.warn("Sign-in attempt with non-existent user: {}", authRequest.getUsernameOrEmail());
                return new AuthResponseDTO(null, null, MessageUtils.getMessage(MessageKeys.AUTH_INVALID_CREDENTIALS));
            }

            // Verify password
            if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                logger.warn("Invalid password attempt for user: {}", user.getUsername());
                return new AuthResponseDTO(null, null, MessageUtils.getMessage(MessageKeys.AUTH_INVALID_CREDENTIALS));
            }

            // Verify if user is active
            if (!user.getIsActive()) {
                logger.warn("Inactive user sign-in attempt: {}", user.getUsername());
                return new AuthResponseDTO(null, null, MessageUtils.getMessage(MessageKeys.AUTH_USER_INACTIVE));
            }

            // Generate JWT token
            String token = generateJwtToken(user);
            
            logger.info("Successful signIn for user: {}", user.getUsername());
            return new AuthResponseDTO(
                    UserMapper.toDto(user),
                    token,
                    MessageUtils.getMessage(MessageKeys.AUTH_SIGNIN_SUCCESS));
                    
        } catch (Exception e) {
            logger.error("Error signing in user: {}", e.getMessage(), e);
            return new AuthResponseDTO(null, null, MessageUtils.getMessage(MessageKeys.AUTH_INVALID_CREDENTIALS));
        }
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO checkAuthStatus(User user) {
        try {
            if (user == null) {
                logger.warn("Auth status check with null user");
                return new AuthResponseDTO(null, null, MessageUtils.getMessage(MessageKeys.AUTH_USER_NOT_AUTHENTICATED));
            }

            if (user.getId() == null) {
                logger.warn("Auth status check with invalid user ID");
                return new AuthResponseDTO(null, null, MessageUtils.getMessage(MessageKeys.AUTH_USER_NOT_FOUND));
            }

            // Verify that user exists in database
            User currentUser = userRepository.findById(user.getId())
                    .orElse(null);
            
            if (currentUser == null) {
                logger.warn("User not found in database during auth status check: {}", user.getId());
                return new AuthResponseDTO(null, null, MessageUtils.getMessage(MessageKeys.AUTH_USER_NOT_FOUND));
            }

            // Verify that user is active
            if (!currentUser.getIsActive()) {
                logger.warn("Inactive user auth status check: {}", currentUser.getUsername());
                return new AuthResponseDTO(null, null, MessageUtils.getMessage(MessageKeys.AUTH_USER_INACTIVE));
            }

            String token = generateJwtToken(currentUser);
            
            logger.info("Auth status check successful for user: {}", currentUser.getUsername());
            return new AuthResponseDTO(
                    UserMapper.toDto(currentUser),
                    token,
                    MessageUtils.getMessage(MessageKeys.AUTH_SIGNIN_SUCCESS));
                    
        } catch (Exception e) {
            logger.error("Error checking auth status: {}", e.getMessage(), e);
            return new AuthResponseDTO(null, null, MessageUtils.getMessage(MessageKeys.AUTH_USER_NOT_AUTHENTICATED));
        }
    }

    private String generateJwtToken(User user) {
        return jwtUtils.generateJwtToken(user);
    }
}