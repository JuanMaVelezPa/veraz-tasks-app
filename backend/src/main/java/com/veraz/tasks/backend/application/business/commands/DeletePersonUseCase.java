package com.veraz.tasks.backend.application.business.commands;

import com.veraz.tasks.backend.domain.business.entities.Client;
import com.veraz.tasks.backend.domain.business.entities.Employee;
import com.veraz.tasks.backend.domain.business.entities.Person;
import com.veraz.tasks.backend.domain.business.repositories.ClientRepository;
import com.veraz.tasks.backend.domain.business.repositories.EmployeeRepository;
import com.veraz.tasks.backend.domain.business.repositories.PersonRepository;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Use case for deleting a person
 * Handles cascade deletion of associated clients and employees
 */
public class DeletePersonUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DeletePersonUseCase.class);

    private final PersonRepository personRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;

    public DeletePersonUseCase(PersonRepository personRepository,
            ClientRepository clientRepository,
            EmployeeRepository employeeRepository) {
        this.personRepository = personRepository;
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
    }

    public void execute(String personIdString) {
        logger.debug("Deleting person with ID: {}", personIdString);

        try {
            PersonId personId = PersonId.of(personIdString);

            // Check if person exists
            Optional<Person> personOpt = personRepository.findById(personId);
            if (personOpt.isEmpty()) {
                throw new IllegalArgumentException("Person not found with ID: " + personIdString);
            }

            Person person = personOpt.get();

            // Disassociate user if exists
            if (person.getUserId() != null) {
                logger.debug("Disassociating user {} from person {}", person.getUserId().getValue(), personIdString);
                person.disassociateUser();
                personRepository.save(person);
            }

            // Delete associated client if exists
            Optional<Client> clientOpt = clientRepository
                    .findByPersonId(personId);
            if (clientOpt.isPresent()) {
                logger.debug("Deleting associated client for person {}", personIdString);
                clientRepository.deleteById(clientOpt.get().getId());
            }

            // Delete associated employee if exists
            Optional<Employee> employeeOpt = employeeRepository
                    .findByPersonId(personId);
            if (employeeOpt.isPresent()) {
                logger.debug("Deleting associated employee for person {}", personIdString);
                employeeRepository.deleteById(employeeOpt.get().getId());
            }

            // Finally delete the person
            personRepository.deleteById(personId);

            logger.debug("Successfully deleted person with ID: {} and all associated data", personIdString);

        } catch (Exception e) {
            logger.error("Error deleting person {}: {}", personIdString, e.getMessage(), e);
            throw e;
        }
    }
}

