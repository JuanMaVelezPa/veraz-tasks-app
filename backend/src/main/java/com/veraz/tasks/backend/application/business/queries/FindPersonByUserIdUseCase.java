package com.veraz.tasks.backend.application.business.queries;

import com.veraz.tasks.backend.application.business.dto.PersonResponse;
import com.veraz.tasks.backend.domain.business.entities.Person;
import com.veraz.tasks.backend.domain.business.repositories.PersonRepository;
import com.veraz.tasks.backend.domain.identity.valueobjects.UserId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Use case for finding a person by user ID
 */
@Service
public class FindPersonByUserIdUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(FindPersonByUserIdUseCase.class);
    
    private final PersonRepository personRepository;
    
    public FindPersonByUserIdUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    
    @Transactional(readOnly = true)
    public PersonResponse execute(String userIdString) {
        logger.debug("Finding person by user ID: {}", userIdString);
        
        try {
            UserId userId = UserId.of(userIdString);
            
            Optional<Person> personOpt = personRepository.findByUserId(userId);
            if (personOpt.isEmpty()) {
                logger.warn("Person not found for user ID: {}", userIdString);
                throw new IllegalArgumentException("Person not found for user ID: " + userIdString);
            }
            
            Person person = personOpt.get();
            logger.debug("Successfully found person with ID: {} for user ID: {}", 
                person.getId().getValue(), userIdString);
            
            return PersonResponse.from(person);
            
        } catch (Exception e) {
            logger.error("Error finding person by user ID {}: {}", userIdString, e.getMessage(), e);
            throw e;
        }
    }
}

