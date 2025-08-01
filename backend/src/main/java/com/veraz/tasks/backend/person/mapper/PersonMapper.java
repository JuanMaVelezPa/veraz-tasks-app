package com.veraz.tasks.backend.person.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import com.veraz.tasks.backend.person.dto.PersonRequestDTO;
import com.veraz.tasks.backend.person.dto.PersonResponseDTO;
import com.veraz.tasks.backend.person.model.Person;

public class PersonMapper {

    public static PersonResponseDTO toDto(Person person) {
        if (person == null) return null;
        
        return PersonResponseDTO.builder()
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

    public static Person toEntity(PersonRequestDTO personRequest) {
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
     * Actualiza una entidad Person existente con datos del DTO
     * @param person Entidad existente
     * @param personRequest DTO con datos a actualizar
     * @return Person actualizado
     */
    public static Person updateEntity(Person person, PersonRequestDTO personRequest) {
        if (person == null || personRequest == null) return person;
        
        if (personRequest.getIdentType() != null) {
            person.setIdentType(personRequest.getIdentType());
        }
        
        if (personRequest.getIdentNumber() != null) {
            person.setIdentNumber(personRequest.getIdentNumber());
        }
        
        if (personRequest.getFirstName() != null) {
            person.setFirstName(personRequest.getFirstName().trim());
        }
        
        if (personRequest.getLastName() != null) {
            person.setLastName(personRequest.getLastName().trim());
        }
        
        if (personRequest.getBirthDate() != null) {
            person.setBirthDate(personRequest.getBirthDate());
        }
        
        if (personRequest.getGender() != null) {
            person.setGender(personRequest.getGender());
        }
        
        if (personRequest.getNationality() != null) {
            person.setNationality(personRequest.getNationality());
        }
        
        if (personRequest.getMobile() != null) {
            person.setMobile(personRequest.getMobile().trim());
        }
        
        if (personRequest.getEmail() != null) {
            person.setEmail(personRequest.getEmail().trim().toLowerCase());
        }
        
        if (personRequest.getAddress() != null) {
            person.setAddress(personRequest.getAddress().trim());
        }
        
        if (personRequest.getCity() != null) {
            person.setCity(personRequest.getCity());
        }
        
        if (personRequest.getCountry() != null) {
            person.setCountry(personRequest.getCountry());
        }
        
        if (personRequest.getPostalCode() != null) {
            person.setPostalCode(personRequest.getPostalCode());
        }
        
        if (personRequest.getNotes() != null) {
            person.setNotes(personRequest.getNotes().trim());
        }
        
        if (personRequest.getIsActive() != null) {
            person.setIsActive(personRequest.getIsActive());
        }
        
        return person;
    }

    /**
     * Convierte una lista de Persons a DTOs
     * @param persons Lista de personas
     * @return Lista de DTOs
     */
    public static Set<PersonResponseDTO> toDtoSet(Set<Person> persons) {
        if (persons == null) return Set.of();
        
        return persons.stream()
                .map(PersonMapper::toDto)
                .collect(Collectors.toSet());
    }

} 