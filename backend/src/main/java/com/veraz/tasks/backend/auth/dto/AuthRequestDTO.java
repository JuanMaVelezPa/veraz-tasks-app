package com.veraz.tasks.backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequestDTO {

    @NotBlank(message = "{validation.field.required}")
    private String usernameOrEmail;

    @NotBlank(message = "{validation.field.required}")
    @Size(min = 8, max = 255, message = "{validation.field.size}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "{validation.field.password}")
    private String password;

}