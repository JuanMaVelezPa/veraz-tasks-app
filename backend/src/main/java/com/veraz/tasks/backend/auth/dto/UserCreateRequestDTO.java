package com.veraz.tasks.backend.auth.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating new users
 * Contains all required fields with mandatory validations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDTO {

    @NotBlank(message = "{validation.field.required}")
    @Size(min = 6, max = 50, message = "{validation.field.size}")
    private String username;

    @Email(message = "{validation.field.email}")
    @NotBlank(message = "{validation.field.required}")
    private String email;

    @NotBlank(message = "{validation.field.required}")
    @Size(min = 8, max = 255, message = "{validation.field.size}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "{validation.field.password}")
    private String password;

    private Boolean isActive = true;

    private Set<String> roles = Set.of("USER");
} 