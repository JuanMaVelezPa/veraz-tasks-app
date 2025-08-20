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
import com.veraz.tasks.backend.auth.model.User;
import com.veraz.tasks.backend.person.mapper.PersonMapper;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO.PaginationInfo;
import com.veraz.tasks.backend.person.model.Person;
import com.veraz.tasks.backend.person.repository.PersonRepository;
import com.veraz.tasks.backend.exception.DataConflictException;
import com.veraz.tasks.backend.exception.ResourceNotFoundException;
import com.veraz.tasks.backend.shared.util.MessageUtils;

/**
 * Service class for managing Person entities
 * Provides CRUD operations and business logic for person management
 * Includes specific methods for user association management
 */
@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public PaginatedResponseDTO<PersonResponseDTO> findAll(Pageable pageable) {
        Page<Person> personPage = personRepository.findAll(pageable);

        List<PersonResponseDTO> personDtos = personPage.getContent().stream()
                .map(PersonMapper::toDto)
                .collect(Collectors.toList());

        PaginationInfo paginationInfo = PaginationInfo
                .builder()
                .currentPage(personPage.getNumber())
                .totalPages(personPage.getTotalPages())
                .totalElements(personPage.getTotalElements())
                .pageSize(personPage.getSize())
                .hasNext(personPage.hasNext())
                .hasPrevious(personPage.hasPrevious())
                .isFirst(personPage.isFirst())
                .isLast(personPage.isLast())
                .build();

        return PaginatedResponseDTO.<PersonResponseDTO>builder()
                .data(personDtos)
                .pagination(paginationInfo)
                .build();
    }

    @Transactional(readOnly = true)
    public Optional<PersonResponseDTO> findById(UUID id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("Person")));
        
        return Optional.of(PersonMapper.toDto(person));
    }

    @Transactional(readOnly = true)
    public PaginatedResponseDTO<PersonResponseDTO> findBySearch(String query, Pageable pageable) {
        Page<Person> personPage = personRepository.findByFirstNameOrLastNameOrEmailOrIdentNumberOrMobileContainingIgnoreCase(query, pageable);

        List<PersonResponseDTO> personDtos = personPage.getContent().stream()
                .map(PersonMapper::toDto)
                .collect(Collectors.toList());

        PaginationInfo paginationInfo = PaginationInfo
                .builder()
                .currentPage(personPage.getNumber())
                .totalPages(personPage.getTotalPages())
                .totalElements(personPage.getTotalElements())
                .pageSize(personPage.getSize())
                .hasNext(personPage.hasNext())
                .hasPrevious(personPage.hasPrevious())
                .isFirst(personPage.isFirst())
                .isLast(personPage.isLast())
                .build();

        return PaginatedResponseDTO.<PersonResponseDTO>builder()
                .data(personDtos)
                .pagination(paginationInfo)
                .build();
    }

    @Transactional(readOnly = true)
    public PersonResponseDTO findByEmail(String email) {
        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("Person")));
        
        return PersonMapper.toDto(person);
    }

    @Transactional(readOnly = true)
    public PersonResponseDTO findByIdentNumber(String identNumber) {
        Person person = personRepository.findByIdentNumber(identNumber)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("Person")));
        
        return PersonMapper.toDto(person);
    }

    /**
     * Creates a new person with validation for unique constraints
     * 
     * @param personRequest The person creation request
     * @return PersonResponseDTO of the created person
     * @throws DataConflictException if person with same identification or email already exists
     */
    @Transactional
    public PersonResponseDTO create(PersonCreateRequestDTO personRequest) {
        // Validate identification uniqueness
        if (personRepository.existsByIdentNumberAndIdentType(personRequest.getIdentNumber(), personRequest.getIdentType())) {
            throw new DataConflictException(MessageUtils.getEntityAlreadyExists("Person"));
        }

        // Validate email uniqueness if provided
        if (personRequest.getEmail() != null && personRepository.existsByEmail(personRequest.getEmail())) {
            throw new DataConflictException(MessageUtils.getEntityAlreadyExists("Person"));
        }

        Person newPerson = PersonMapper.toEntity(personRequest);
        personRepository.save(newPerson);

        logger.info("Person created successfully: {} {}", newPerson.getFirstName(), newPerson.getLastName());

        return PersonMapper.toDto(newPerson);
    }

    /**
     * Updates an existing person with validation for unique constraints
     * Note: User association is never updated through this method
     * 
     * @param id The person ID to update
     * @param personRequestDTO The update request data
     * @return PersonResponseDTO of the updated person
     * @throws ResourceNotFoundException if person not found
     * @throws DataConflictException if new identification or email conflicts with existing data
     */
    @Transactional
    public PersonResponseDTO update(UUID id, PersonUpdateRequestDTO personRequestDTO) {
        Person personToUpdate = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("Person")));

        // Validate identification uniqueness if provided
        if (personRequestDTO.getIdentNumber() != null && !personRequestDTO.getIdentNumber().trim().isEmpty() &&
            personRequestDTO.getIdentType() != null && !personRequestDTO.getIdentType().trim().isEmpty()) {
            
            String newIdentNumber = personRequestDTO.getIdentNumber().trim();
            String newIdentType = personRequestDTO.getIdentType().trim();
            
            if (!newIdentNumber.equals(personToUpdate.getIdentNumber()) || 
                !newIdentType.equals(personToUpdate.getIdentType())) {
                
                if (personRepository.existsByIdentNumberAndIdentType(newIdentNumber, newIdentType)) {
                    throw new DataConflictException(MessageUtils.getEntityAlreadyExists("Person"));
                }
            }
        }

        // Validate email uniqueness if provided
        if (personRequestDTO.getEmail() != null && !personRequestDTO.getEmail().trim().isEmpty()) {
            String newEmail = personRequestDTO.getEmail().trim();
            
            if (!newEmail.equalsIgnoreCase(personToUpdate.getEmail())) {
                if (personRepository.existsByEmail(newEmail)) {
                    throw new DataConflictException(MessageUtils.getEntityAlreadyExists("Person"));
                }
            }
        }

        // Update person entity using mapper
        PersonMapper.updateEntity(personToUpdate, personRequestDTO);
        personToUpdate.setUpdatedAt(LocalDateTime.now());
        personRepository.save(personToUpdate);

        logger.info("Person updated successfully with ID: {}", personToUpdate.getId());

        return PersonMapper.toDto(personToUpdate);
    }

    @Transactional
    public void deleteById(UUID id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("Person")));
        
        personRepository.delete(person);
        logger.info("Person deleted successfully with ID: {}", id);
    }

    // Query methods
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

    /**
     * Removes user association from a person
     * This is the only method that can modify user association
     * 
     * @param personId The person ID to remove user association from
     * @return PersonResponseDTO of the updated person
     * @throws ResourceNotFoundException if person not found
     */
    @Transactional
    public PersonResponseDTO removeUserAssociation(UUID personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("Person")));
        
        person.setUser(null);
        person.setUpdatedAt(LocalDateTime.now());
        personRepository.save(person);
        
        logger.info("User association removed from person with ID: {}", personId);
        return PersonMapper.toDto(person);
    }

    /**
     * Associates a user with a person
     * This is the only method that can create user association
     * 
     * @param personId The person ID to associate with
     * @param userId The user ID to associate
     * @return PersonResponseDTO of the updated person
     * @throws ResourceNotFoundException if person not found
     */
    @Transactional
    public PersonResponseDTO associateUser(UUID personId, UUID userId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("Person")));
        
        person.setUser(User.builder().id(userId).build());
        person.setUpdatedAt(LocalDateTime.now());
        personRepository.save(person);
        
        logger.info("User {} associated with person with ID: {}", userId, personId);
        return PersonMapper.toDto(person);
    }
}
