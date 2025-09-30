package com.veraz.tasks.backend.infrastructure.business.persistence;

import com.veraz.tasks.backend.domain.business.entities.Person;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;
import com.veraz.tasks.backend.domain.identity.valueobjects.UserId;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "persons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonEntity {

    @Id
    @Column(name = "persons_id", nullable = false)
    private UUID id;

    @Column(name = "users_id")
    private UUID userId;

    @Column(name = "ident_type", nullable = false, length = 20)
    private String identType;

    @Column(name = "ident_number", nullable = false, length = 20)
    private String identNumber;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "gender", length = 3)
    private String gender;

    @Column(name = "nationality", length = 50)
    private String nationality;

    @Column(name = "mobile", length = 20)
    private String mobile;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static PersonEntity from(Person person) {
        PersonEntity entity = new PersonEntity();
        entity.id = person.getId().getValue();
        entity.userId = person.getUserId() != null ? person.getUserId().getValue() : null;
        entity.identType = person.getIdentType();
        entity.identNumber = person.getIdentNumber();
        entity.firstName = person.getFirstName();
        entity.lastName = person.getLastName();
        entity.birthDate = person.getBirthDate();
        entity.gender = person.getGender();
        entity.nationality = person.getNationality();
        entity.mobile = person.getMobile();
        entity.email = person.getEmail() != null ? person.getEmail().getValue() : null;
        entity.address = person.getAddress();
        entity.city = person.getCity();
        entity.country = person.getCountry();
        entity.postalCode = person.getPostalCode();
        entity.notes = person.getNotes();
        entity.isActive = person.isActive();
        entity.createdAt = person.getCreatedAt();
        entity.updatedAt = person.getUpdatedAt();
        return entity;
    }

    public Person toDomain() {
        return Person.reconstruct(
                PersonId.of(this.id),
                this.userId != null ? UserId.of(this.userId) : null,
                this.identType,
                this.identNumber,
                this.firstName,
                this.lastName,
                this.birthDate,
                this.gender,
                this.nationality,
                this.mobile,
                this.email,
                this.address,
                this.city,
                this.country,
                this.postalCode,
                this.notes,
                this.isActive,
                this.createdAt,
                this.updatedAt);
    }
}
