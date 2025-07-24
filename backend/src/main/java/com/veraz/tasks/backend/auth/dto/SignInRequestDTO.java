package com.veraz.tasks.backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignInRequestDTO {

    @NotBlank(message = "{signIn.usernameOrEmail.required}")
    private String usernameOrEmail;

    @NotBlank(message = "{signIn.password.required}")
    @Size(min = 8, max = 255, message = "{signIn.password.size}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "{signIn.password.pattern}")
    private String password;

}