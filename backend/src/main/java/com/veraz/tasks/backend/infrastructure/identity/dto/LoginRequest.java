package com.veraz.tasks.backend.infrastructure.identity.dto;

import com.veraz.tasks.backend.shared.constants.MessageKeys;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "{" + MessageKeys.VALIDATION_FIELD_REQUIRED + "}") String usernameOrEmail,

        @NotBlank(message = "{" + MessageKeys.VALIDATION_FIELD_REQUIRED + "}") @Size(min = 6, max = 100, message = "{"
                + MessageKeys.VALIDATION_FIELD_SIZE + "}") String password) {

    public LoginRequest {
        if (usernameOrEmail != null && usernameOrEmail.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageKeys.VALIDATION_FIELD_NOT_EMPTY);
        }
        if (password != null && password.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageKeys.VALIDATION_FIELD_NOT_EMPTY);
        }
    }
}
