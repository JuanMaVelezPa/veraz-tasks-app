package com.veraz.tasks.backend.person.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for creating new persons
 * Contains all required fields with mandatory validations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonCreateRequestDTO {

    private UUID userId;

    @NotBlank(message = "{validation.field.required}")
    @Size(max = 20, message = "{validation.field.max.length}")
    private String identType;

    @NotBlank(message = "{validation.field.required}")
    @Size(min = 3, max = 20, message = "{validation.field.size}")
    private String identNumber;

    @NotBlank(message = "{validation.field.required}")
    @Size(min = 3, max = 100, message = "{validation.field.size}")
    private String firstName;

    @NotBlank(message = "{validation.field.required}")
    @Size(min = 3, max = 100, message = "{validation.field.size}")
    private String lastName;

    private LocalDate birthDate;

    @Size(max = 3, message = "{validation.field.max.length}")
    private String gender;

    @Size(max = 50, message = "{validation.field.max.length}")
    private String nationality;

    @Size(max = 20, message = "{validation.field.max.length}")
    private String mobile;

    @Email(message = "{validation.field.email}")
    @Size(min = 3, max = 100, message = "{validation.field.size}")
    private String email;

    @Size(max = 255, message = "{validation.field.max.length}")
    private String address;

    @Size(max = 100, message = "{validation.field.max.length}")
    private String city;

    @Size(max = 100, message = "{validation.field.max.length}")
    private String country;

    @Size(max = 20, message = "{validation.field.max.length}")
    private String postalCode;

    @Size(max = 255, message = "{validation.field.max.length}")
    private String notes;

    private Boolean isActive;
} 