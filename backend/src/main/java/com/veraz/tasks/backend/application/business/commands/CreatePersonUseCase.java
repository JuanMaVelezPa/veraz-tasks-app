package com.veraz.tasks.backend.application.business.commands;

import com.veraz.tasks.backend.application.business.dto.PersonResponse;
import com.veraz.tasks.backend.domain.business.entities.Person;
import com.veraz.tasks.backend.domain.business.repositories.PersonRepository;
import com.veraz.tasks.backend.infrastructure.business.dto.CreatePersonRequest;

public class CreatePersonUseCase {

    private final PersonRepository personRepository;

    public CreatePersonUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonResponse execute(CreatePersonRequest request) {
        if (personRepository.existsByIdentNumber(request.identNumber())) {
            throw new IllegalArgumentException("Person with identification number " +
                    request.identNumber() + " already exists");
        }

        if (request.email() != null && !request.email().trim().isEmpty() &&
                personRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Person with email " +
                    request.email() + " already exists");
        }

        Person person = Person.create(
                request.identType(),
                request.identNumber(),
                request.firstName(),
                request.lastName(),
                request.email() != null && !request.email().trim().isEmpty() ? request.email() : null);

        if (request.mobile() != null) {
            person.setMobile(request.mobile());
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

        Person savedPerson = personRepository.save(person);
        return PersonResponse.from(savedPerson);
    }
}
