package com.veraz.tasks.backend.person.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for updating existing persons
 * Contains optional fields with format validations
 * Note: User association is never updated through this DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonUpdateRequestDTO {

    @Size(max = 20, message = "{validation.field.max.length}")
    private String identType;

    @Size(min = 3, max = 20, message = "{validation.field.size}")
    private String identNumber;

    @Size(min = 3, max = 100, message = "{validation.field.size}")
    private String firstName;

    @Size(min = 3, max = 100, message = "{validation.field.size}")
    private String lastName;

    private LocalDate birthDate;

    @Size(max = 3, message = "{validation.field.max.length}")
    private String gender;

    @Size(max = 50, message = "{validation.field.max.length}")
    private String nationality;

    @Size(max = 20, message = "{validation.field.max.length}")
    private String mobile;

    @Size(max = 100, message = "{validation.field.max.length}")
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