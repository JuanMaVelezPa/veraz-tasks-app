package com.veraz.tasks.backend.auth.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veraz.tasks.backend.auth.config.JwtUtils;
import com.veraz.tasks.backend.auth.dto.SignInRequestDTO;
import com.veraz.tasks.backend.auth.dto.UserResponseDTO;
import com.veraz.tasks.backend.auth.model.User;
import com.veraz.tasks.backend.auth.repository.UserRepository;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final MessageSource messageSource;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public AuthService(MessageSource messageSource,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils,
            UserService userService) {
        this.messageSource = messageSource;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public User getUserByIDAuth(UUID id) {
        logger.info("Getting user by id: {}", id);
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.getRoles().size(); // Force lazy loading
        }
        return user;
    }

    @Transactional(readOnly = true)
    public UserResponseDTO signInUser(SignInRequestDTO signInRequest) {
        try {
            User user = userRepository.findByUsernameOrEmailAllIgnoreCase(
                    signInRequest.getUsernameOrEmail(),
                    signInRequest.getUsernameOrEmail());

            if (user == null) {
                return new UserResponseDTO(null, null,
                        messageSource.getMessage("user.invalid.password", null, LocaleContextHolder.getLocale()));
            }

            logger.info("SignIn attempt for user: {}", user.getUsername());

            if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
                logger.warn("Invalid password attempt for user: {}", user.getUsername());
                return new UserResponseDTO(null, null,
                        messageSource.getMessage("user.invalid.password", null, LocaleContextHolder.getLocale()));
            }

            if (!user.getIsActive()) {
                logger.warn("User is not active: {}", user.getUsername());
                return new UserResponseDTO(null, null,
                        messageSource.getMessage("user.not.active", null, LocaleContextHolder.getLocale()));
            }

            logger.info("Successful signIn for user: {}", user.getUsername());
            return new UserResponseDTO(
                    userService.toUserDetailDto(user),
                    generateJwtToken(user),
                    messageSource.getMessage("user.signIn.successfully", null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            logger.error("Error signing in user: " + e.getMessage());
            return new UserResponseDTO(null, null,
                    messageSource.getMessage("user.error.signIn", null, LocaleContextHolder.getLocale()));
        }
    }

    @Transactional(readOnly = true)
    public UserResponseDTO checkAuthStatus(User user) {
        try {
            if (user == null) {
                return new UserResponseDTO(null, null,
                        messageSource.getMessage("user.not.authenticated", null, LocaleContextHolder.getLocale()));
            }

            if (user.getId() == null) {
                return new UserResponseDTO(null, null,
                        messageSource.getMessage("user.invalid.id", null, LocaleContextHolder.getLocale()));
            }

            return new UserResponseDTO(
                    userService.toUserDetailDto(user),
                    generateJwtToken(user),
                    messageSource.getMessage("user.signIn.successfully", null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            logger.error("Error checking auth status: {}", e.getMessage(), e);
            return new UserResponseDTO(null, null,
                    messageSource.getMessage("user.error.checking.auth.status", null, LocaleContextHolder.getLocale()));
        }
    }

    private String generateJwtToken(User user) {
        return jwtUtils.generateJwtToken(user);
    }
}