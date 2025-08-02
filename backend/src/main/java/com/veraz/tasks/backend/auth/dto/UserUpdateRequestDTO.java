package com.veraz.tasks.backend.auth.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating existing users
 * Contains optional fields with format validations but no mandatory constraints
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDTO {

    @Size(min = 6, max = 50, message = "{validation.field.size}")
    private String username;

    @Email(message = "{validation.field.email}")
    private String email;

    @Size(min = 8, max = 255, message = "{validation.field.size}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "{validation.field.password}")
    private String password;

    private Boolean isActive;

    private Set<String> roles;
} 