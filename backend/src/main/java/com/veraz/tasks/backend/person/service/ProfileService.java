package com.veraz.tasks.backend.person.service;

import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veraz.tasks.backend.auth.model.User;
import com.veraz.tasks.backend.person.dto.PersonRequestDto;
import com.veraz.tasks.backend.person.dto.PersonResponseDto;
import com.veraz.tasks.backend.person.mapper.PersonMapper;
import com.veraz.tasks.backend.person.model.Person;
import com.veraz.tasks.backend.person.repository.PersonRepository;

@Service
public class ProfileService {

    private final PersonRepository personRepository;
    private final MessageSource messageSource;

    public ProfileService(PersonRepository personRepository, MessageSource messageSource) {
        this.personRepository = personRepository;
        this.messageSource = messageSource;
    }

    @Transactional(readOnly = true)
    public PersonResponseDto getPersonByUser(User user) {
        Optional<Person> personOpt = personRepository.findByUser(user);
        if (personOpt.isEmpty()) {
            return PersonResponseDto.builder()
                    .person(null)
                    .message(messageSource.getMessage("person.not.found", null, LocaleContextHolder.getLocale()))
                    .status("NOT_FOUND")
                    .build();
        }
        return PersonResponseDto.builder()
                .person(PersonMapper.toDto(personOpt.get()))
                .message(null)
                .status("OK")
                .build();
    }

    @Transactional
    public PersonResponseDto createPersonForUser(User user, PersonRequestDto personRequestDto) {
        try {
            // Verificar si el usuario actual ya tiene una persona
            Optional<Person> existingPerson = personRepository.findByUser(user);
            if (existingPerson.isPresent()) {
                return PersonResponseDto.builder()
                        .person(null)
                        .message(messageSource.getMessage("user.already.has.person", null,
                                LocaleContextHolder.getLocale()))
                        .status("USER_ALREADY_HAS_PERSON")
                        .build();
            }

            // Crear la persona asociada al usuario actual
            Person person = Person.builder()
                    .identType(personRequestDto.getIdentType())
                    .identNumber(personRequestDto.getIdentNumber())
                    .firstName(personRequestDto.getFirstName())
                    .lastName(personRequestDto.getLastName())
                    .birthDate(personRequestDto.getBirthDate())
                    .gender(personRequestDto.getGender())
                    .nationality(personRequestDto.getNationality())
                    .mobile(personRequestDto.getMobile())
                    .email(personRequestDto.getEmail())
                    .address(personRequestDto.getAddress())
                    .city(personRequestDto.getCity())
                    .country(personRequestDto.getCountry())
                    .postalCode(personRequestDto.getPostalCode())
                    .notes(personRequestDto.getNotes())
                    .user(user)
                    .build();

            person = personRepository.save(person);

            return PersonResponseDto.builder()
                    .person(PersonMapper.toDto(person))
                    .message(messageSource.getMessage("person.created.successfully", null,
                            LocaleContextHolder.getLocale()))
                    .status("CREATED")
                    .build();

        } catch (Exception e) {
            return PersonResponseDto.builder()
                    .person(null)
                    .message(messageSource.getMessage("person.error.creating", null, LocaleContextHolder.getLocale()))
                    .status("ERROR")
                    .build();
        }
    }

    @Transactional
    public PersonResponseDto updatePersonForUser(User user, PersonRequestDto personRequestDto) {
        try {
            Optional<Person> personOpt = personRepository.findByUser(user);
            if (personOpt.isEmpty()) {
                return PersonResponseDto.builder()
                        .person(null)
                        .message(messageSource.getMessage("person.not.found", null, LocaleContextHolder.getLocale()))
                        .status("NOT_FOUND")
                        .build();
            }

            // Verificar si el usuario actual es el mismo que el usuario de la persona
            if (!user.getId().equals(personOpt.get().getUser().getId())) {
                return PersonResponseDto.builder()
                        .person(null)
                        .message(messageSource.getMessage("user.not.authorized", null, LocaleContextHolder.getLocale()))
                        .status("USER_NOT_AUTHORIZED")
                        .build();
            }

            Person person = personOpt.get();

            // Actualizar campos (sin cambiar el usuario asociado)
            person.setIdentType(personRequestDto.getIdentType());
            person.setIdentNumber(personRequestDto.getIdentNumber());
            person.setFirstName(personRequestDto.getFirstName());
            person.setLastName(personRequestDto.getLastName());
            person.setBirthDate(personRequestDto.getBirthDate());
            person.setGender(personRequestDto.getGender());
            person.setNationality(personRequestDto.getNationality());
            person.setMobile(personRequestDto.getMobile());
            person.setEmail(personRequestDto.getEmail());
            person.setAddress(personRequestDto.getAddress());
            person.setCity(personRequestDto.getCity());
            person.setCountry(personRequestDto.getCountry());
            person.setPostalCode(personRequestDto.getPostalCode());
            person.setNotes(personRequestDto.getNotes());

            person = personRepository.save(person);
            return PersonResponseDto.builder()
                    .person(PersonMapper.toDto(person))
                    .message(messageSource.getMessage("person.updated.successfully", null,
                            LocaleContextHolder.getLocale()))
                    .status("UPDATED")
                    .build();
        } catch (Exception e) {
            return PersonResponseDto.builder()
                    .person(null)
                    .message(messageSource.getMessage("person.error.updating", null, LocaleContextHolder.getLocale()))
                    .status("ERROR")
                    .build();
        }
    }

}
