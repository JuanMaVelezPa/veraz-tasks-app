package com.veraz.tasks.backend.domain.business.entities;

import com.veraz.tasks.backend.domain.business.valueobjects.Email;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;
import com.veraz.tasks.backend.domain.identity.valueobjects.UserId;
import com.veraz.tasks.backend.domain.shared.exceptions.InvalidPersonDataException;
import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.util.MessageUtils;

import lombok.Getter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Person domain entity
 */
@Getter
@ToString(exclude = { "notes" })
@EqualsAndHashCode(of = "id")
public class Person {

    private final PersonId id;
    private UserId userId;
    private String identType;
    private String identNumber;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String gender;
    private String nationality;
    private String mobile;
    private Email email;
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private String notes;
    private boolean isActive;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Person(PersonId id, UserId userId, String identType, String identNumber,
            String firstName, String lastName, LocalDate birthDate,
            String gender, String nationality, String mobile, Email email,
            String address, String city, String country, String postalCode,
            String notes, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.identType = identType;
        this.identNumber = identNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.nationality = nationality;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.notes = notes;
        this.isActive = true;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;

        validateBusinessRules();
    }

    public static Person create(String identType, String identNumber,
            String firstName, String lastName,
            String email) {
        return new Person(
                PersonId.generate(),
                null,
                identType,
                identNumber,
                firstName,
                lastName,
                null,
                "O",
                null,
                null,
                email != null ? Email.of(email) : null,
                null,
                null,
                null,
                null,
                null,
                LocalDateTime.now());
    }

    public static Person reconstruct(PersonId id, UserId userId, String identType, String identNumber,
            String firstName, String lastName, LocalDate birthDate,
            String gender, String nationality, String mobile,
            String email, String address, String city,
            String country, String postalCode, String notes,
            boolean isActive, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        Person person = new Person(
                id, userId, identType, identNumber, firstName, lastName, birthDate,
                gender, nationality, mobile, email != null ? Email.of(email) : null, address, city,
                country, postalCode, notes, createdAt);
        person.isActive = isActive;
        person.updatedAt = updatedAt;
        return person;
    }

    private void validateBusinessRules() {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new InvalidPersonDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new InvalidPersonDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        if (identType == null || identType.trim().isEmpty()) {
            throw new InvalidPersonDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        if (identNumber == null || identNumber.trim().isEmpty()) {
            throw new InvalidPersonDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        if (birthDate != null && birthDate.isAfter(LocalDate.now())) {
            throw new InvalidPersonDataException(
                    MessageUtils.getMessage(MessageKeys.DOMAIN_DATE_AFTER_OTHER, "Birth date", "current date"));
        }
        if (birthDate != null && birthDate.isBefore(LocalDate.now().minusYears(150))) {
            throw new InvalidPersonDataException(
                    MessageUtils.getMessage(MessageKeys.DOMAIN_DATE_BEFORE_OTHER, "Birth date", "150 years ago"));
        }
    }

    public boolean isAdult() {
        if (birthDate == null) {
            return false;
        }
        return birthDate.plusYears(18).isBefore(LocalDate.now()) ||
                birthDate.plusYears(18).isEqual(LocalDate.now());
    }

    public void changeEmail(String newEmail) {
        if (newEmail == null || newEmail.trim().isEmpty()) {
            throw new InvalidPersonDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        this.email = Email.of(newEmail);
        this.updatedAt = LocalDateTime.now();
    }

    public void changeName(String newFirstName, String newLastName) {
        if (newFirstName == null || newFirstName.trim().isEmpty()) {
            throw new InvalidPersonDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        if (newLastName == null || newLastName.trim().isEmpty()) {
            throw new InvalidPersonDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        this.firstName = newFirstName.trim();
        this.lastName = newLastName.trim();
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public int getAge() {
        if (birthDate == null) {
            return 0;
        }
        return LocalDate.now().getYear() - birthDate.getYear();
    }

    public UserId getUserId() {
        return userId;
    }

    // Lombok generates all getters automatically

    public void setIdentType(String identType) {
        if (identType == null || identType.trim().isEmpty()) {
            throw new InvalidPersonDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        this.identType = identType.trim();
        this.updatedAt = LocalDateTime.now();
    }

    public void setIdentNumber(String identNumber) {
        if (identNumber == null || identNumber.trim().isEmpty()) {
            throw new InvalidPersonDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        this.identNumber = identNumber.trim();
        this.updatedAt = LocalDateTime.now();
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new InvalidPersonDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        this.firstName = firstName.trim();
        this.updatedAt = LocalDateTime.now();
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new InvalidPersonDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        this.lastName = lastName.trim();
        this.updatedAt = LocalDateTime.now();
    }

    public void setBirthDate(LocalDate birthDate) {
        if (birthDate != null && birthDate.isAfter(LocalDate.now())) {
            throw new InvalidPersonDataException(
                    MessageUtils.getMessage(MessageKeys.DOMAIN_DATE_AFTER_OTHER, "Birth date", "current date"));
        }
        this.birthDate = birthDate;
        this.updatedAt = LocalDateTime.now();
    }

    public void setGender(String gender) {
        this.gender = gender != null ? gender.trim() : "O";
        this.updatedAt = LocalDateTime.now();
    }

    public void setNationality(String nationality) {
        this.nationality = nationality != null ? nationality.trim() : null;
        this.updatedAt = LocalDateTime.now();
    }

    public void setMobile(String mobile) {
        this.mobile = mobile != null ? mobile.trim() : null;
        this.updatedAt = LocalDateTime.now();
    }

    public void setEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            this.email = Email.of(email.trim());
        } else {
            this.email = null;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void setAddress(String address) {
        this.address = address != null ? address.trim() : null;
        this.updatedAt = LocalDateTime.now();
    }

    public void setCity(String city) {
        this.city = city != null ? city.trim() : null;
        this.updatedAt = LocalDateTime.now();
    }

    public void setCountry(String country) {
        this.country = country != null ? country.trim() : null;
        this.updatedAt = LocalDateTime.now();
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode != null ? postalCode.trim() : null;
        this.updatedAt = LocalDateTime.now();
    }

    public void setNotes(String notes) {
        this.notes = notes != null ? notes.trim() : null;
        this.updatedAt = LocalDateTime.now();
    }

    public void associateUser(UserId userId) {
        this.userId = userId;
        this.updatedAt = LocalDateTime.now();
    }

    public void disassociateUser() {
        this.userId = null;
        this.updatedAt = LocalDateTime.now();
    }

}
