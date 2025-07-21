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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonRequestDto {

    private UUID userId;

    @NotBlank(message = "{NotBlank.person.identType}")
    @Size(max = 20, message = "{Size.person.identType}")
    private String identType;

    @NotBlank(message = "{NotBlank.person.identNumber}")
    @Size(min = 3, max = 20, message = "{Size.person.identNumber.min} {Size.person.identNumber.max}")
    private String identNumber;

    @NotBlank(message = "{NotBlank.person.firstName}")
    @Size(min = 3, max = 100, message = "{Size.person.firstName.min} {Size.person.firstName.max}")
    private String firstName;

    @NotBlank(message = "{NotBlank.person.lastName}")
    @Size(min = 3, max = 100, message = "{Size.person.lastName.min} {Size.person.lastName.max}")
    private String lastName;

    private LocalDate birthDate;

    @Size(max = 3, message = "{Size.person.gender}")
    private String gender;

    @Size(max = 50, message = "{Size.person.nationality}")
    private String nationality;

    @Size(max = 20, message = "{Size.person.mobile}")
    private String mobile;

    @Email(message = "{Email.person.email}")
    @Size(min = 3, max = 100, message = "{Size.person.email.min} {Size.person.email.max}")
    private String email;

    @Size(max = 255, message = "{Size.person.address}")
    private String address;

    @Size(max = 100, message = "{Size.person.city}")
    private String city;

    @Size(max = 100, message = "{Size.person.country}")
    private String country;

    @Size(max = 20, message = "{Size.person.postalCode}")
    private String postalCode;

    @Size(max = 255, message = "{Size.person.notes}")
    private String notes;
}