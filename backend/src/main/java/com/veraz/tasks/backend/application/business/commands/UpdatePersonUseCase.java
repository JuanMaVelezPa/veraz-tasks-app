package com.veraz.tasks.backend.application.business.commands;

import com.veraz.tasks.backend.application.business.dto.PersonResponse;
import com.veraz.tasks.backend.domain.business.entities.Person;
import com.veraz.tasks.backend.domain.business.repositories.PersonRepository;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;
import com.veraz.tasks.backend.infrastructure.business.dto.UpdatePersonRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class UpdatePersonUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UpdatePersonUseCase.class);

    private final PersonRepository personRepository;

    public UpdatePersonUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonResponse execute(String personIdString, UpdatePersonRequest request) {
        logger.debug("Updating person with ID: {}", personIdString);

        try {
            PersonId personId = PersonId.of(personIdString);

            Optional<Person> personOpt = personRepository.findById(personId);
            if (personOpt.isEmpty()) {
                throw new IllegalArgumentException("Person not found with ID: " + personIdString);
            }

            Person person = personOpt.get();

            if (request.identType() != null) {
                person.setIdentType(request.identType());
            }
            if (request.identNumber() != null) {
                person.setIdentNumber(request.identNumber());
            }
            if (request.firstName() != null) {
                person.setFirstName(request.firstName());
            }
            if (request.lastName() != null) {
                person.setLastName(request.lastName());
            }
            if (request.birthDate() != null) {
                person.setBirthDate(request.birthDate());
            }
            if (request.gender() != null) {
                person.setGender(request.gender());
            }
            if (request.nationality() != null) {
                person.setNationality(request.nationality());
            }
            if (request.mobile() != null) {
                person.setMobile(request.mobile());
            }
            if (request.email() != null) {
                person.setEmail(request.email());
            }
            if (request.address() != null) {
                person.setAddress(request.address());
            }
            if (request.city() != null) {
                person.setCity(request.city());
            }
            if (request.country() != null) {
                person.setCountry(request.country());
            }
            if (request.postalCode() != null) {
                person.setPostalCode(request.postalCode());
            }
            if (request.notes() != null) {
                person.setNotes(request.notes());
            }
            if (request.isActive() != null) {
                if (request.isActive()) {
                    person.activate();
                } else {
                    person.deactivate();
                }
            }

            Person updatedPerson = personRepository.save(person);

            logger.debug("Successfully updated person with ID: {}", personIdString);
            return PersonResponse.from(updatedPerson);

        } catch (Exception e) {
            logger.error("Error updating person {}: {}", personIdString, e.getMessage(), e);
            throw e;
        }
    }
}
