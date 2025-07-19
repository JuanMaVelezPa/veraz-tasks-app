package com.veraz.tasks.backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDTO {

    @Size(min = 6, max = 50, message = "{user.username.size}")
    private String username;

    @Email(message = "{user.email.invalid}")
    private String email;

    @Size(min = 8, max = 255, message = "{user.password.size}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", 
             message = "{user.password.pattern}")
    private String password;

    private Boolean isActive;
} 