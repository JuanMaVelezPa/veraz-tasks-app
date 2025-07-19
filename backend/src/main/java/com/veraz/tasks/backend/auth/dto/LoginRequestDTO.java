package com.veraz.tasks.backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "{login.usernameOrEmail.required}")
    private String usernameOrEmail;

    @NotBlank(message = "{login.password.required}")
    @Size(min = 8, max = 255, message = "{login.password.size}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "{login.password.pattern}")
    private String password;

}