package com.veraz.tasks.backend.person.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veraz.tasks.backend.auth.model.User;
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

    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);
    private final PersonRepository personRepository;

    public ProfileService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public PersonResponseDTO getPersonByUser(User user) {
        logger.info("Getting person for user: {}", user.getUsername());
        
        Person person = personRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for user: " + user.getUsername()));
        
        return PersonMapper.toDto(person);
    }

    @Transactional
    public PersonResponseDTO createPersonForUser(User user, PersonCreateRequestDTO personRequestDto) {
        logger.info("Creating person for user: {}", user.getUsername());
        
        // Check if person already exists with same email or identification
        if (personRepository.existsByEmail(personRequestDto.getEmail())) {
            throw new DataConflictException("Person already exists with email: " + personRequestDto.getEmail());
        }

        if (personRepository.existsByIdentNumberAndIdentType(personRequestDto.getIdentNumber(), personRequestDto.getIdentType())) {
            throw new DataConflictException("Person already exists with identification: " + personRequestDto.getIdentType() + " " + personRequestDto.getIdentNumber());
        }

        // Create new person
        Person person = PersonMapper.toEntity(personRequestDto);
        personRepository.save(person);

        logger.info("Person created successfully for user: {} with id: {}", user.getUsername(), person.getId());

        return PersonMapper.toDto(person);
    }

    @Transactional
    public PersonResponseDTO updatePersonForUser(User user, PersonUpdateRequestDTO personRequestDto) {
        logger.info("Updating person for user: {}", user.getUsername());

        // Get existing person for user
        Person person = personRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for user: " + user.getUsername()));

        // Check if identification already exists in another person (only if provided and different)
        if (personRequestDto.getIdentNumber() != null && personRequestDto.getIdentType() != null &&
            (!person.getIdentNumber().equals(personRequestDto.getIdentNumber()) || 
             !person.getIdentType().equals(personRequestDto.getIdentType()))) {
            
            if (personRepository.existsByIdentNumberAndIdentType(personRequestDto.getIdentNumber(), personRequestDto.getIdentType())) {
                throw new DataConflictException("Person already exists with identification: " + personRequestDto.getIdentType() + " " + personRequestDto.getIdentNumber());
            }
        }

        // Update person using mapper
        PersonMapper.updateEntity(person, personRequestDto);
        person = personRepository.save(person);
        
        logger.info("Person updated successfully for user: {} with id: {}", user.getUsername(), person.getId());
        
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
        logger.info("Deleting person for user: {}", user.getUsername());
        
        Person person = personRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for user: " + user.getUsername()));

        if (person.getUser() == null || !user.getId().equals(person.getUser().getId())) {
            throw new ResourceNotFoundException("Person not found for user: " + user.getUsername());
        }

        personRepository.delete(person);
        logger.info("Person deleted successfully for user: {}", user.getUsername());
    }
}
