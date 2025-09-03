package com.veraz.tasks.backend.person.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veraz.tasks.backend.person.dto.PersonCreateRequestDTO;
import com.veraz.tasks.backend.person.dto.PersonUpdateRequestDTO;
import com.veraz.tasks.backend.person.dto.PersonResponseDTO;
import com.veraz.tasks.backend.person.mapper.PersonMapper;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.person.model.Person;
import com.veraz.tasks.backend.person.repository.PersonRepository;
import com.veraz.tasks.backend.exception.DataConflictException;
import com.veraz.tasks.backend.exception.ResourceNotFoundException;
import com.veraz.tasks.backend.shared.util.MessageUtils;
import com.veraz.tasks.backend.shared.util.PaginationUtils;
import com.veraz.tasks.backend.auth.model.User;
import com.veraz.tasks.backend.auth.repository.UserRepository;

@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository personRepository;
    private final UserRepository userRepository;

    public PersonService(PersonRepository personRepository, UserRepository userRepository) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public PaginatedResponseDTO<PersonResponseDTO> findAll(Pageable pageable) {
        Page<Person> personPage = personRepository.findAllWithEmployee(pageable);
        return PaginationUtils.toPaginatedResponse(personPage, PersonMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<PersonResponseDTO> findById(UUID id) {
        Person person = personRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFoundMessage("Person")));

        return Optional.of(PersonMapper.toDto(person));
    }

    @Transactional(readOnly = true)
    public PaginatedResponseDTO<PersonResponseDTO> findBySearch(String searchQuery, Pageable pageable) {
        Page<Person> personPage = personRepository.findBySearchWithEmployee(searchQuery, pageable);
        return PaginationUtils.toPaginatedResponse(personPage, PersonMapper::toDto);
    }

    @Transactional(readOnly = true)
    public PersonResponseDTO findByEmail(String email) {
        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFoundMessage("Person")));

        return PersonMapper.toDto(person);
    }

    @Transactional(readOnly = true)
    public PersonResponseDTO findByIdentNumber(String identNumber) {
        Person person = personRepository.findByIdentNumber(identNumber)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFoundMessage("Person")));

        return PersonMapper.toDto(person);
    }

    @Transactional(readOnly = true)
    public Optional<PersonResponseDTO> findByUserId(UUID userId) {
        return personRepository.findByUserIdWithUser(userId)
                .map(PersonMapper::toDto);
    }

    @Transactional
    public PersonResponseDTO create(PersonCreateRequestDTO createRequest) {
        validateIdentificationUniqueness(createRequest.getIdentNumber(), createRequest.getIdentType());
        validateEmailUniqueness(createRequest.getEmail());

        Person newPerson = PersonMapper.toEntity(createRequest);
        personRepository.save(newPerson);

        logger.info("Person created successfully: {} {}", newPerson.getFirstName(), newPerson.getLastName());

        return PersonMapper.toDto(newPerson);
    }

    @Transactional
    public PersonResponseDTO update(UUID id, PersonUpdateRequestDTO updateRequest) {
        Person personToUpdate = personRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFoundMessage("Person")));

        validateIdentificationUniquenessForUpdate(updateRequest, personToUpdate);
        validateEmailUniquenessForUpdate(updateRequest, personToUpdate);

        PersonMapper.updateEntity(personToUpdate, updateRequest);
        personToUpdate.setUpdatedAt(LocalDateTime.now());
        personRepository.save(personToUpdate);

        logger.info("Person updated successfully with ID: {}", personToUpdate.getId());

        return PersonMapper.toDto(personToUpdate);
    }

    @Transactional
    public void deleteById(UUID id) {
        logger.info("Starting deletion of person with ID: {}", id);

        Person person = personRepository.findByIdWithUserAndEmployee(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFoundMessage("Person")));

        logger.info("Found person: {} {} (ID: {})", person.getFirstName(), person.getLastName(), id);

        if (person.getUser() != null) {
            logger.info("Desassociating user {} from person {} before deletion", person.getUser().getId(), id);
            person.setUser(null);
            personRepository.save(person);
            logger.info("User disassociation persisted for person {}", id);
        }

        personRepository.delete(person);
        logger.info("Person and all associated data (Employee) deleted successfully with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public List<PersonResponseDTO> findByIsActive(Boolean isActive) {
        return personRepository.findByIsActive(isActive).stream()
                .map(PersonMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PersonResponseDTO> findByGender(String gender) {
        return personRepository.findByGender(gender).stream()
                .map(PersonMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PersonResponseDTO> findByNationality(String nationality) {
        return personRepository.findByNationality(nationality).stream()
                .map(PersonMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PersonResponseDTO> findByCity(String city) {
        return personRepository.findByCity(city).stream()
                .map(PersonMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PersonResponseDTO> findByCountry(String country) {
        return personRepository.findByCountry(country).stream()
                .map(PersonMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PersonResponseDTO associateUser(UUID personId, UUID userId) {
        logger.info("associateUser called with personId: {} and userId: {}", personId, userId);

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> {
                    logger.error("Person not found with ID: {}", personId);
                    return new ResourceNotFoundException(MessageUtils.getEntityNotFoundMessage("Person"));
                });

        logger.info("Person found: {} {} (ID: {})", person.getFirstName(), person.getLastName(), person.getId());

        Optional<Person> existingPersonWithUser = personRepository.findByUserId(userId);
        if (existingPersonWithUser.isPresent()) {
            logger.warn("User {} is already associated with person {}", userId, existingPersonWithUser.get().getId());
            throw new DataConflictException("User is already associated with another person");
        }

        if (person.getUser() != null) {
            logger.warn("Person {} is already associated with user {}", personId, person.getUser().getId());
            throw new DataConflictException("Person is already associated with a user");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", userId);
                    return new ResourceNotFoundException("User not found with ID: " + userId);
                });

        logger.info("User found: {} (ID: {})", user.getUsername(), user.getId());

        person.setUser(user);
        person.setUpdatedAt(LocalDateTime.now());

        Person savedPerson = personRepository.save(person);
        logger.info("User {} associated with person {} successfully", userId, personId);

        return PersonMapper.toDto(savedPerson);
    }

    @Transactional
    public PersonResponseDTO removeUserAssociation(UUID personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFoundMessage("Person")));

        if (person.getUser() == null) {
            throw new DataConflictException("Person is not associated with any user");
        }

        person.setUser(null);
        person.setUpdatedAt(LocalDateTime.now());

        Person savedPerson = personRepository.save(person);
        logger.info("User association removed from person {} successfully", personId);

        return PersonMapper.toDto(savedPerson);
    }

    private void validateIdentificationUniqueness(String identNumber, String identType) {
        if (personRepository.existsByIdentNumberAndIdentType(identNumber, identType)) {
            throw new DataConflictException(MessageUtils.getEntityAlreadyExistsMessage("Person"));
        }
    }

    private void validateEmailUniqueness(String email) {
        if (email != null && personRepository.existsByEmail(email)) {
            throw new DataConflictException(MessageUtils.getEntityAlreadyExistsMessage("Person"));
        }
    }

    private void validateIdentificationUniquenessForUpdate(PersonUpdateRequestDTO updateRequest,
            Person existingPerson) {
        if (updateRequest.getIdentNumber() != null && updateRequest.getIdentType() != null) {
            String newIdentNumber = updateRequest.getIdentNumber().trim();
            String newIdentType = updateRequest.getIdentType().trim();

            if (!newIdentNumber.equals(existingPerson.getIdentNumber()) ||
                    !newIdentType.equals(existingPerson.getIdentType())) {
                if (personRepository.existsByIdentNumberAndIdentType(newIdentNumber, newIdentType)) {
                    throw new DataConflictException(MessageUtils.getEntityAlreadyExistsMessage("Person"));
                }
            }
        }
    }

    private void validateEmailUniquenessForUpdate(PersonUpdateRequestDTO updateRequest, Person existingPerson) {
        if (updateRequest.getEmail() != null && !updateRequest.getEmail().trim().isEmpty()) {
            String newEmail = updateRequest.getEmail().trim();
            if (!newEmail.equalsIgnoreCase(existingPerson.getEmail())) {
                if (personRepository.existsByEmail(newEmail)) {
                    throw new DataConflictException(MessageUtils.getEntityAlreadyExistsMessage("Person"));
                }
            }
        }
    }
}
