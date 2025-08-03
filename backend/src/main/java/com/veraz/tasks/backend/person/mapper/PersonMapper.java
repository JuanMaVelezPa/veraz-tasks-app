package com.veraz.tasks.backend.person.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import com.veraz.tasks.backend.person.dto.PersonCreateRequestDTO;
import com.veraz.tasks.backend.person.dto.PersonUpdateRequestDTO;
import com.veraz.tasks.backend.person.dto.PersonResponseDTO;
import com.veraz.tasks.backend.person.model.Person;

public class PersonMapper {

    /**
     * Converts Person entity to PersonResponseDTO
     * @param person Person entity
     * @return PersonResponseDTO
     */
    public static PersonResponseDTO toDto(Person person) {
        if (person == null) return null;
        
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
     * @param personRequest Create request DTO
     * @return Person entity
     */
    public static Person toEntity(PersonCreateRequestDTO personRequest) {
        if (personRequest == null) return null;
        
        return Person.builder()
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
    }

    /**
     * Updates an existing Person entity with data from PersonUpdateRequestDTO
     * @param person Existing person entity
     * @param personRequest Update request DTO
     * @return Updated Person entity
     */
    public static Person updateEntity(Person person, PersonUpdateRequestDTO personRequest) {
        if (person == null || personRequest == null) return person;
        
        // Update identification type if provided and not empty
        if (personRequest.getIdentType() != null && !personRequest.getIdentType().trim().isEmpty()) {
            person.setIdentType(personRequest.getIdentType().trim());
        }
        
        // Update identification number if provided and not empty
        if (personRequest.getIdentNumber() != null && !personRequest.getIdentNumber().trim().isEmpty()) {
            person.setIdentNumber(personRequest.getIdentNumber().trim());
        }
        
        // Update first name if provided and not empty
        if (personRequest.getFirstName() != null && !personRequest.getFirstName().trim().isEmpty()) {
            person.setFirstName(personRequest.getFirstName().trim());
        }
        
        // Update last name if provided and not empty
        if (personRequest.getLastName() != null && !personRequest.getLastName().trim().isEmpty()) {
            person.setLastName(personRequest.getLastName().trim());
        }
        
        // Update birth date if provided
        if (personRequest.getBirthDate() != null) {
            person.setBirthDate(personRequest.getBirthDate());
        }
        
        // Update gender if provided and not empty
        if (personRequest.getGender() != null && !personRequest.getGender().trim().isEmpty()) {
            person.setGender(personRequest.getGender().trim());
        }
        
        // Update nationality if provided and not empty
        if (personRequest.getNationality() != null && !personRequest.getNationality().trim().isEmpty()) {
            person.setNationality(personRequest.getNationality().trim());
        }
        
        // Update mobile if provided and not empty
        if (personRequest.getMobile() != null && !personRequest.getMobile().trim().isEmpty()) {
            person.setMobile(personRequest.getMobile().trim());
        }
        
        // Update email if provided and not empty
        if (personRequest.getEmail() != null && !personRequest.getEmail().trim().isEmpty()) {
            person.setEmail(personRequest.getEmail().trim().toLowerCase());
        }
        
        // Update address if provided and not empty
        if (personRequest.getAddress() != null && !personRequest.getAddress().trim().isEmpty()) {
            person.setAddress(personRequest.getAddress().trim());
        }
        
        // Update city if provided and not empty
        if (personRequest.getCity() != null && !personRequest.getCity().trim().isEmpty()) {
            person.setCity(personRequest.getCity().trim());
        }
        
        // Update country if provided and not empty
        if (personRequest.getCountry() != null && !personRequest.getCountry().trim().isEmpty()) {
            person.setCountry(personRequest.getCountry().trim());
        }
        
        // Update postal code if provided and not empty
        if (personRequest.getPostalCode() != null && !personRequest.getPostalCode().trim().isEmpty()) {
            person.setPostalCode(personRequest.getPostalCode().trim());
        }
        
        // Update notes if provided and not empty
        if (personRequest.getNotes() != null && !personRequest.getNotes().trim().isEmpty()) {
            person.setNotes(personRequest.getNotes().trim());
        }
        
        // Update active status if provided
        if (personRequest.getIsActive() != null) {
            person.setIsActive(personRequest.getIsActive());
        }
        
        return person;
    }

    /**
     * Converts a set of Persons to DTOs
     * @param persons Set of persons
     * @return Set of DTOs
     */
    public static Set<PersonResponseDTO> toDtoSet(Set<Person> persons) {
        if (persons == null) return Set.of();
        
        return persons.stream()
                .map(PersonMapper::toDto)
                .collect(Collectors.toSet());
    }
} 