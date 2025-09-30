package com.veraz.tasks.backend.infrastructure.identity.dto;

import com.veraz.tasks.backend.shared.constants.MessageKeys;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.util.List;

public record UpdateUserRequest(
        @Size(min = 3, max = 50, message = "{" + MessageKeys.VALIDATION_FIELD_SIZE + "}") String username,

        @Size(min = 6, message = "{" + MessageKeys.VALIDATION_FIELD_MIN_LENGTH + "}") String password,

        @Email(message = "{" + MessageKeys.VALIDATION_FIELD_EMAIL + "}") @Size(max = 100, message = "{"
                + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}") String email,

        Boolean isActive,

        List<String> roles) {

    public UpdateUserRequest {

        if (username != null && username.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageKeys.VALIDATION_FIELD_NOT_EMPTY);
        }
        if (password != null && password.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageKeys.VALIDATION_FIELD_NOT_EMPTY);
        }
    }
}
