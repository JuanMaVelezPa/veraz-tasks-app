package com.veraz.tasks.backend.application.business.commands;

import com.veraz.tasks.backend.application.business.dto.PersonResponse;
import com.veraz.tasks.backend.domain.business.entities.Person;
import com.veraz.tasks.backend.domain.business.repositories.PersonRepository;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Use case for removing user association from a person
 */
public class RemoveUserAssociationUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(RemoveUserAssociationUseCase.class);
    
    private final PersonRepository personRepository;
    
    public RemoveUserAssociationUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    
    public PersonResponse execute(String personIdString) {
        logger.debug("Removing user association from person with ID: {}", personIdString);
        
        try {
            PersonId personId = PersonId.of(personIdString);
            
            // Find existing person
            Optional<Person> personOpt = personRepository.findById(personId);
            if (personOpt.isEmpty()) {
                throw new IllegalArgumentException("Person not found with ID: " + personIdString);
            }
            
            Person person = personOpt.get();
            
            // Check if person has a user associated
            if (person.getUserId() == null) {
                throw new IllegalArgumentException("Person does not have a user associated");
            }
            
            // Remove user association
            person.disassociateUser();
            Person updatedPerson = personRepository.save(person);
            
            logger.debug("Successfully removed user association from person with ID: {}", personIdString);
            return PersonResponse.from(updatedPerson);
            
        } catch (Exception e) {
            logger.error("Error removing user association from person {}: {}", personIdString, e.getMessage(), e);
            throw e;
        }
    }
}

