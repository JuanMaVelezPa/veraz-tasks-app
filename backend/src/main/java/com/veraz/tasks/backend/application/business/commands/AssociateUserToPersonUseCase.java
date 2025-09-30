package com.veraz.tasks.backend.application.business.commands;

import com.veraz.tasks.backend.application.business.dto.PersonResponse;
import com.veraz.tasks.backend.domain.business.entities.Person;
import com.veraz.tasks.backend.domain.business.repositories.PersonRepository;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;
import com.veraz.tasks.backend.domain.identity.entities.User;
import com.veraz.tasks.backend.domain.identity.repositories.UserRepository;
import com.veraz.tasks.backend.domain.identity.valueobjects.UserId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Use case for associating a user to a person
 */
public class AssociateUserToPersonUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(AssociateUserToPersonUseCase.class);
    
    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    
    public AssociateUserToPersonUseCase(PersonRepository personRepository, UserRepository userRepository) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
    }
    
    public PersonResponse execute(String personIdString, String userIdString) {
        logger.info("=== ASSOCIATE USER TO PERSON PROCESS START ===");
        logger.info("Person ID: {}, User ID: {}", personIdString, userIdString);
        
        try {
            PersonId personId = PersonId.of(personIdString);
            UserId userId = UserId.of(userIdString);
            
            // Verify person exists
            logger.info("Looking for person with ID: {}", personIdString);
            Optional<Person> personOpt = personRepository.findById(personId);
            if (personOpt.isEmpty()) {
                logger.error("Person not found with ID: {}", personIdString);
                throw new IllegalArgumentException("Person not found with ID: " + personIdString);
            }
            logger.info("Person found: {}", personOpt.get().getId().getValue());
            
            // Verify user exists
            logger.info("Looking for user with ID: {}", userIdString);
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                logger.error("User not found with ID: {}", userIdString);
                logger.info("Attempting to find user without roles requirement...");
                
                // Try to find user without roles requirement for debugging
                Optional<User> userWithoutRoles = userRepository.findByIdForAuthentication(userId);
                if (userWithoutRoles.isEmpty()) {
                    logger.error("User not found even without roles requirement");
                    throw new IllegalArgumentException("User not found with ID: " + userIdString);
                } else {
                    logger.warn("User found without roles, but roles are required for association");
                    throw new IllegalArgumentException("User not found with ID: " + userIdString);
                }
            }
            logger.info("User found: {}, Roles count: {}", userOpt.get().getId().getValue(), userOpt.get().getRoleIds().size());
            
            Person person = personOpt.get();
            // User user = userOpt.get(); // Not needed for this operation
            
            // Check if person already has a user associated
            if (person.getUserId() != null) {
                throw new IllegalArgumentException("Person already has a user associated");
            }
            
            // Check if user is already associated with another person
            // We need to check if any other person has this user associated
            // This could be done with a repository method, but for now we'll skip this check
            // as it would require additional repository methods
            
            // Associate user to person
            logger.info("Associating user {} to person {}", userIdString, personIdString);
            person.associateUser(userId);
            Person updatedPerson = personRepository.save(person);
            
            logger.info("Successfully associated user {} to person {}", userIdString, personIdString);
            logger.info("=== ASSOCIATE USER TO PERSON PROCESS END ===");
            return PersonResponse.from(updatedPerson);
            
        } catch (Exception e) {
            logger.error("Error associating user {} to person {}: {}", userIdString, personIdString, e.getMessage(), e);
            throw e;
        }
    }
}

