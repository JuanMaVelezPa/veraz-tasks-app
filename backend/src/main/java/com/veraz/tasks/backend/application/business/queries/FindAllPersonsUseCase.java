package com.veraz.tasks.backend.application.business.queries;

import com.veraz.tasks.backend.application.business.dto.PersonResponse;
import com.veraz.tasks.backend.domain.business.entities.Person;
import com.veraz.tasks.backend.domain.business.repositories.PersonRepository;
import com.veraz.tasks.backend.domain.business.repositories.EmployeeRepository;
import com.veraz.tasks.backend.domain.business.repositories.ClientRepository;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;
import com.veraz.tasks.backend.shared.util.PaginationUtils;

import org.springframework.data.domain.Page;

/**
 * Use case for finding all persons with pagination
 */
public class FindAllPersonsUseCase {
    
    private final PersonRepository personRepository;
    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;
    
    public FindAllPersonsUseCase(PersonRepository personRepository, 
                                EmployeeRepository employeeRepository,
                                ClientRepository clientRepository) {
        this.personRepository = personRepository;
        this.employeeRepository = employeeRepository;
        this.clientRepository = clientRepository;
    }
    
    public PaginatedResponseDTO<PersonResponse> execute(PaginationRequestDTO paginationRequest) {
        Page<Person> personsPage = personRepository.findAll(paginationRequest);
        return PaginationUtils.toPaginatedResponse(personsPage, this::createPersonResponse);
    }
    
    private PersonResponse createPersonResponse(Person person) {
        PersonId personId = person.getId();
        boolean isEmployee = employeeRepository.existsByPersonId(personId);
        boolean isClient = clientRepository.existsByPersonId(personId);
        return PersonResponse.from(person, isEmployee, isClient);
    }
}

