package com.veraz.tasks.backend.person.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import com.veraz.tasks.backend.auth.model.User;
import com.veraz.tasks.backend.person.dto.PersonCreateRequestDTO;
import com.veraz.tasks.backend.person.dto.PersonUpdateRequestDTO;
import com.veraz.tasks.backend.person.dto.PersonResponseDTO;
import com.veraz.tasks.backend.person.model.Person;

/**
 * Mapper class for converting between Person entities and DTOs
 * Provides static methods for entity-to-DTO and DTO-to-entity mapping
 */
public class PersonMapper {

    /**
     * Converts a Person entity to PersonResponseDTO
     * 
     * @param person The Person entity to convert
     * @return PersonResponseDTO or null if person is null
     */
    public static PersonResponseDTO toDto(Person person) {
        if (person == null)
            return null;

        return PersonResponseDTO.builder()
                .userId(person.getUser() != null ? person.getUser().getId() : null)
                .id(person.getId())
                .identType(person.getIdentType())
                .identNumber(person.getIdentNumber())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .birthDate(person.getBirthDate())
                .gender(person.getGender())
                .nationality(person.getNationality())
                .mobile(person.getMobile())
                .email(person.getEmail())
                .address(person.getAddress())
                .city(person.getCity())
                .country(person.getCountry())
                .postalCode(person.getPostalCode())
                .notes(person.getNotes())
                .isActive(person.getIsActive())
                .createdAt(person.getCreatedAt())
                .updatedAt(person.getUpdatedAt())
                .build();
    }

    /**
     * Converts PersonCreateRequestDTO to Person entity for creation
     * 
     * @param personRequest The create request DTO
     * @return Person entity or null if personRequest is null
     */
    public static Person toEntity(PersonCreateRequestDTO personRequest) {
        if (personRequest == null)
            return null;

        Person person = Person.builder()
                .identType(personRequest.getIdentType())
                .identNumber(personRequest.getIdentNumber())
                .firstName(personRequest.getFirstName())
                .lastName(personRequest.getLastName())
                .birthDate(personRequest.getBirthDate())
                .gender(personRequest.getGender())
                .nationality(personRequest.getNationality())
                .mobile(personRequest.getMobile())
                .email(personRequest.getEmail())
                .address(personRequest.getAddress())
                .city(personRequest.getCity())
                .country(personRequest.getCountry())
                .postalCode(personRequest.getPostalCode())
                .notes(personRequest.getNotes())
                .isActive(personRequest.getIsActive())
                .build();

        // Set user if userId is provided
        if (personRequest.getUserId() != null) {
            person.setUser(User.builder().id(personRequest.getUserId()).build());
        }

        return person;
    }

    /**
     * Updates an existing Person entity with data from PersonUpdateRequestDTO
     * Note: User association is never updated through this method
     * 
     * @param person The existing person entity to update
     * @param personRequest The update request DTO
     * @return Updated Person entity or original person if inputs are null
     */
    public static Person updateEntity(Person person, PersonUpdateRequestDTO personRequest) {
        if (person == null || personRequest == null)
            return person;

        if (personRequest.getIdentType() != null && !personRequest.getIdentType().trim().isEmpty()) {
            person.setIdentType(personRequest.getIdentType().trim());
        }

        if (personRequest.getIdentNumber() != null && !personRequest.getIdentNumber().trim().isEmpty()) {
            person.setIdentNumber(personRequest.getIdentNumber().trim());
        }

        if (personRequest.getFirstName() != null && !personRequest.getFirstName().trim().isEmpty()) {
            person.setFirstName(personRequest.getFirstName().trim());
        }

        if (personRequest.getLastName() != null && !personRequest.getLastName().trim().isEmpty()) {
            person.setLastName(personRequest.getLastName().trim());
        }

        if (personRequest.getBirthDate() != null) {
            person.setBirthDate(personRequest.getBirthDate());
        }

        if (personRequest.getGender() != null && !personRequest.getGender().trim().isEmpty()) {
            person.setGender(personRequest.getGender().trim());
        }

        if (personRequest.getNationality() != null && !personRequest.getNationality().trim().isEmpty()) {
            person.setNationality(personRequest.getNationality().trim());
        }

        if (personRequest.getMobile() != null && !personRequest.getMobile().trim().isEmpty()) {
            person.setMobile(personRequest.getMobile().trim());
        }

        if (personRequest.getEmail() != null && !personRequest.getEmail().trim().isEmpty()) {
            person.setEmail(personRequest.getEmail().trim().toLowerCase());
        }

        if (personRequest.getAddress() != null && !personRequest.getAddress().trim().isEmpty()) {
            person.setAddress(personRequest.getAddress().trim());
        }

        if (personRequest.getCity() != null && !personRequest.getCity().trim().isEmpty()) {
            person.setCity(personRequest.getCity().trim());
        }

        if (personRequest.getCountry() != null && !personRequest.getCountry().trim().isEmpty()) {
            person.setCountry(personRequest.getCountry().trim());
        }

        if (personRequest.getPostalCode() != null && !personRequest.getPostalCode().trim().isEmpty()) {
            person.setPostalCode(personRequest.getPostalCode().trim());
        }

        if (personRequest.getNotes() != null && !personRequest.getNotes().trim().isEmpty()) {
            person.setNotes(personRequest.getNotes().trim());
        }

        if (personRequest.getIsActive() != null) {
            person.setIsActive(personRequest.getIsActive());
        }
        return person;
    }

    /**
     * Converts a set of Person entities to a set of PersonResponseDTOs
     * 
     * @param persons Set of Person entities
     * @return Set of PersonResponseDTOs or empty set if persons is null
     */
    public static Set<PersonResponseDTO> toDtoSet(Set<Person> persons) {
        if (persons == null)
            return Set.of();

        return persons.stream()
                .map(PersonMapper::toDto)
                .collect(Collectors.toSet());
    }
}