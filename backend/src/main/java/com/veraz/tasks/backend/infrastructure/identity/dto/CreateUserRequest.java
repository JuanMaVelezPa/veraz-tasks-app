package com.veraz.tasks.backend.infrastructure.identity.dto;

import com.veraz.tasks.backend.shared.constants.MessageKeys;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateUserRequest(
        @NotBlank(message = "{" + MessageKeys.VALIDATION_FIELD_REQUIRED + "}") @Size(min = 3, max = 50, message = "{"
                + MessageKeys.VALIDATION_FIELD_SIZE + "}") String username,

        @NotBlank(message = "{" + MessageKeys.VALIDATION_FIELD_REQUIRED + "}") @Size(min = 6, max = 100, message = "{"
                + MessageKeys.VALIDATION_FIELD_SIZE + "}") String password,

        @Email(message = "{" + MessageKeys.VALIDATION_FIELD_EMAIL + "}") @Size(max = 100, message = "{"
                + MessageKeys.VALIDATION_FIELD_MAX_LENGTH + "}") String email,

        List<String> roles) {
}
