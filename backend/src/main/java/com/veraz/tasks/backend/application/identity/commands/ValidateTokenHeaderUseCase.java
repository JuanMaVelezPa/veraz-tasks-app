package com.veraz.tasks.backend.application.identity.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.veraz.tasks.backend.application.business.dto.LoginResponse;

public class ValidateTokenHeaderUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ValidateTokenHeaderUseCase.class);

    private final ValidateTokenUseCase validateTokenUseCase;

    public ValidateTokenHeaderUseCase(ValidateTokenUseCase validateTokenUseCase) {
        this.validateTokenUseCase = validateTokenUseCase;
    }

    public LoginResponse execute(String authorizationHeader) {
        logger.debug("Validating authorization header");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.warn("Invalid authorization header format");
            return LoginResponse.error("No valid token provided");
        }

        String token = authorizationHeader.substring(7);
        logger.debug("Extracted token from header");

        return validateTokenUseCase.execute(token);
    }

    public boolean isSuccessfulValidation(LoginResponse response) {
        return response.user() != null && response.token() != null;
    }
}
