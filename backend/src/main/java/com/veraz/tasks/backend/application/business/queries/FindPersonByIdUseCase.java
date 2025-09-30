package com.veraz.tasks.backend.application.business.queries;

import com.veraz.tasks.backend.application.business.dto.PersonResponse;
import com.veraz.tasks.backend.domain.business.repositories.PersonRepository;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;

import java.util.Optional;

/**
 * Use case for finding a person by ID
 * 
 * Implements the business logic for person retrieval.
 * Part of the application layer in Clean Architecture.
 * Orchestrates domain entities and repositories.
 */
public class FindPersonByIdUseCase {
    
    private final PersonRepository personRepository;
    
    public FindPersonByIdUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    
    public Optional<PersonResponse> execute(String personIdString) {
        return Optional.of(PersonId.of(personIdString))
                .flatMap(personRepository::findById)
                .map(PersonResponse::from);
    }
}

