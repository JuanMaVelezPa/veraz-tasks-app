package com.veraz.tasks.backend.application.business.queries;

import com.veraz.tasks.backend.application.business.dto.PersonResponse;
import com.veraz.tasks.backend.domain.business.entities.Person;
import com.veraz.tasks.backend.domain.business.repositories.PersonRepository;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;
import com.veraz.tasks.backend.shared.util.PaginationUtils;

import org.springframework.data.domain.Page;

/**
 * Use case for finding all persons with pagination
 */
public class FindAllPersonsUseCase {
    
    private final PersonRepository personRepository;
    
    public FindAllPersonsUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    
    public PaginatedResponseDTO<PersonResponse> execute(PaginationRequestDTO paginationRequest) {
        Page<Person> personsPage = personRepository.findAll(paginationRequest);
        return PaginationUtils.toPaginatedResponse(personsPage, PersonResponse::from);
    }
}

