package com.veraz.tasks.backend.application.business.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.veraz.tasks.backend.domain.business.entities.Person;

/**
 * Response DTO for Person
 * 
 * A Response represents data returned to the client.
 * Contains only information necessary for presentation.
 * 
 * Using Record for immutability and conciseness (Java 14+)
 */
public record PersonResponse(
    String id,
    String userId,
    String identType,
    String identNumber,
    String firstName,
    String lastName,
    String fullName,
    LocalDate birthDate,
    String gender,
    String nationality,
    String mobile,
    String email,
    String address,
    String city,
    String country,
    String postalCode,
    String notes,
    boolean isActive,
    int age,
    boolean isAdult,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    
    /**
     * Factory method to create a response from a domain entity
     */
    public static PersonResponse from(Person person) {
        return new PersonResponse(
            person.getId().getValueAsString(),
            person.getUserId() != null ? person.getUserId().getValueAsString() : null,
            person.getIdentType(),
            person.getIdentNumber(),
            person.getFirstName(),
            person.getLastName(),
            person.getFullName(),
            person.getBirthDate(),
            person.getGender(),
            person.getNationality(),
            person.getMobile(),
            person.getEmail() != null ? person.getEmail().getValue() : null,
            person.getAddress(),
            person.getCity(),
            person.getCountry(),
            person.getPostalCode(),
            person.getNotes(),
            person.isActive(),
            person.getAge(),
            person.isAdult(),
            person.getCreatedAt(),
            person.getUpdatedAt()
        );
    }
}
