package com.veraz.tasks.backend.person.mapper;

import com.veraz.tasks.backend.person.dto.PersonDto;
import com.veraz.tasks.backend.person.dto.PersonRequestDto;
import com.veraz.tasks.backend.person.model.Person;

public class PersonMapper {
    public static PersonDto toDto(Person person) {
        if (person == null) return null;
        return PersonDto.builder()
                .id(person.getId())
                .userId(person.getUser() != null ? person.getUser().getId() : null)
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

    public static Person toEntity(PersonRequestDto dto) {
        if (dto == null) return null;
        return Person.builder()
                .identType(dto.getIdentType())
                .identNumber(dto.getIdentNumber())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthDate(dto.getBirthDate())
                .gender(dto.getGender())
                .nationality(dto.getNationality())
                .mobile(dto.getMobile())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .city(dto.getCity())
                .country(dto.getCountry())
                .postalCode(dto.getPostalCode())
                .notes(dto.getNotes())
                .build();
    }
} 