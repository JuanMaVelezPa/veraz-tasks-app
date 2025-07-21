package com.veraz.tasks.backend.person.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonDto {
    private UUID id;
    private UUID userId;
    private String identType;
    private String identNumber;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String gender;
    private String nationality;
    private String mobile;
    private String email;
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private String notes;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}