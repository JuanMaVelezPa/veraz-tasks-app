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
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO.PaginationInfo;
import com.veraz.tasks.backend.person.model.Person;
import com.veraz.tasks.backend.person.repository.PersonRepository;
import com.veraz.tasks.backend.exception.DataConflictException;
import com.veraz.tasks.backend.exception.ResourceNotFoundException;
import com.veraz.tasks.backend.shared.util.MessageUtils;

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

    @Transactional
    public PersonResponseDTO create(PersonCreateRequestDTO personRequest) {
        // Check if person already exists with same identification
        if (personRepository.existsByIdentNumberAndIdentType(personRequest.getIdentNumber(), personRequest.getIdentType())) {
            throw new DataConflictException(MessageUtils.getEntityAlreadyExists("Person"));
        }

        // Check if email already exists (if provided)
        if (personRequest.getEmail() != null && personRepository.existsByEmail(personRequest.getEmail())) {
            throw new DataConflictException(MessageUtils.getEntityAlreadyExists("Person"));
        }

        Person newPerson = PersonMapper.toEntity(personRequest);
        personRepository.save(newPerson);

        logger.info("Person created successfully: {} {} with createdAt: {} and updatedAt: {}",
                newPerson.getFirstName(), newPerson.getLastName(), newPerson.getCreatedAt(), newPerson.getUpdatedAt());

        return PersonMapper.toDto(newPerson);
    }

    @Transactional
    public PersonResponseDTO update(UUID id, PersonUpdateRequestDTO personRequestDTO) {
        Person personToUpdate = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("Person")));

        // Update identification if provided and not empty
        if (personRequestDTO.getIdentNumber() != null && !personRequestDTO.getIdentNumber().trim().isEmpty() &&
            personRequestDTO.getIdentType() != null && !personRequestDTO.getIdentType().trim().isEmpty()) {
            
            String newIdentNumber = personRequestDTO.getIdentNumber().trim();
            String newIdentType = personRequestDTO.getIdentType().trim();
            
            // Check if identification is different from current
            if (!newIdentNumber.equals(personToUpdate.getIdentNumber()) || 
                !newIdentType.equals(personToUpdate.getIdentType())) {
                
                // Check if new identification already exists
                if (personRepository.existsByIdentNumberAndIdentType(newIdentNumber, newIdentType)) {
                    throw new DataConflictException(MessageUtils.getEntityAlreadyExists("Person"));
                }
            }
        }

        // Update email if provided and not empty
        if (personRequestDTO.getEmail() != null && !personRequestDTO.getEmail().trim().isEmpty()) {
            String newEmail = personRequestDTO.getEmail().trim();
            
            // Check if email is different from current
            if (!newEmail.equalsIgnoreCase(personToUpdate.getEmail())) {
                // Check if new email already exists
                if (personRepository.existsByEmail(newEmail)) {
                    throw new DataConflictException(MessageUtils.getEntityAlreadyExists("Person"));
                }
            }
        }

        // Update person using mapper
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

    // Additional useful methods
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
}
