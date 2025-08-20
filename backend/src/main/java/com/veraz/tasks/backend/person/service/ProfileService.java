package com.veraz.tasks.backend.person.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veraz.tasks.backend.auth.model.User;
import com.veraz.tasks.backend.auth.dto.UserUpdateRequestDTO;
import com.veraz.tasks.backend.auth.dto.UserResponseDTO;
import com.veraz.tasks.backend.auth.service.UserService;
import com.veraz.tasks.backend.exception.DataConflictException;
import com.veraz.tasks.backend.exception.ResourceNotFoundException;
import com.veraz.tasks.backend.person.dto.PersonCreateRequestDTO;
import com.veraz.tasks.backend.person.dto.PersonUpdateRequestDTO;
import com.veraz.tasks.backend.person.dto.PersonResponseDTO;
import com.veraz.tasks.backend.person.mapper.PersonMapper;
import com.veraz.tasks.backend.person.model.Person;
import com.veraz.tasks.backend.person.repository.PersonRepository;

@Service
public class ProfileService {

    private final PersonRepository personRepository;
    private final UserService userService;

    public ProfileService(PersonRepository personRepository, UserService userService) {
        this.personRepository = personRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public PersonResponseDTO getPersonByUser(User user) {
        Person person = personRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for user: " + user.getUsername()));

        return PersonMapper.toDto(person);
    }

    @Transactional
    public PersonResponseDTO createPersonForUser(User user, PersonCreateRequestDTO personRequestDto) {
        if (personRepository.findByUser(user).isPresent()) {
            throw new DataConflictException("User already has a person profile associated");
        }

        if (personRequestDto.getEmail() != null && !personRequestDto.getEmail().trim().isEmpty()) {
            String email = personRequestDto.getEmail().trim();
            if (personRepository.existsByEmail(email)) {
                throw new DataConflictException("Person already exists with email: " + email);
            }
        }

        if (personRepository.existsByIdentNumberAndIdentType(personRequestDto.getIdentNumber(),
                personRequestDto.getIdentType())) {
            throw new DataConflictException("Person already exists with identification: "
                    + personRequestDto.getIdentType() + " " + personRequestDto.getIdentNumber());
        }

        personRequestDto.setUserId(user.getId());
        Person person = PersonMapper.toEntity(personRequestDto);
        personRepository.save(person);

        return PersonMapper.toDto(person);
    }

    @Transactional
    public PersonResponseDTO updatePersonForUser(User user, PersonUpdateRequestDTO personRequestDto) {
        Person person = personRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for user: " + user.getUsername()));

        if (personRequestDto.getIdentNumber() != null && personRequestDto.getIdentType() != null &&
                (!person.getIdentNumber().equals(personRequestDto.getIdentNumber()) ||
                        !person.getIdentType().equals(personRequestDto.getIdentType()))) {

            if (personRepository.existsByIdentNumberAndIdentType(personRequestDto.getIdentNumber(),
                    personRequestDto.getIdentType())) {
                throw new DataConflictException("Person already exists with identification: "
                        + personRequestDto.getIdentType() + " " + personRequestDto.getIdentNumber());
            }
        }

        if (personRequestDto.getEmail() != null && !personRequestDto.getEmail().trim().isEmpty() &&
                !personRequestDto.getEmail().trim().equalsIgnoreCase(person.getEmail())) {

            String email = personRequestDto.getEmail().trim();
            if (personRepository.existsByEmail(email)) {
                throw new DataConflictException("Person already exists with email: " + email);
            }
        }

        PersonMapper.updateEntity(person, personRequestDto);
        person = personRepository.save(person);

        return PersonMapper.toDto(person);
    }

    @Transactional(readOnly = true)
    public boolean hasPerson(User user) {
        return personRepository.findByUser(user).isPresent();
    }

    @Transactional(readOnly = true)
    public Optional<Person> findPersonByUser(User user) {
        return personRepository.findByUser(user);
    }

    @Transactional
    public void deletePersonForUser(User user) {
        Person person = personRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for user: " + user.getUsername()));

        if (person.getUser() == null || !user.getId().equals(person.getUser().getId())) {
            throw new ResourceNotFoundException("Person not found for user: " + user.getUsername());
        }

        personRepository.delete(person);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserAccount(User user) {
        UserResponseDTO response = userService.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return response;
    }

    @Transactional
    public UserResponseDTO updateUserAccount(User user, UserUpdateRequestDTO userRequest) {
        UserResponseDTO response = userService.update(user.getId(), userRequest);

        return response;
    }
}
