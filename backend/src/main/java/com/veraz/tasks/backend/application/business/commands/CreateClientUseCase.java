package com.veraz.tasks.backend.application.business.commands;

import com.veraz.tasks.backend.infrastructure.business.dto.CreateClientRequest;
import com.veraz.tasks.backend.application.business.dto.ClientResponse;
import com.veraz.tasks.backend.domain.business.entities.Client;
import com.veraz.tasks.backend.domain.business.repositories.ClientRepository;
import com.veraz.tasks.backend.domain.business.repositories.PersonRepository;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;

public class CreateClientUseCase {

    private final ClientRepository clientRepository;
    private final PersonRepository personRepository;

    public CreateClientUseCase(ClientRepository clientRepository, PersonRepository personRepository) {
        this.clientRepository = clientRepository;
        this.personRepository = personRepository;
    }

    public ClientResponse execute(CreateClientRequest request) {

        PersonId personId = PersonId.of(request.personId());
        if (!personRepository.findById(personId).isPresent()) {
            throw new IllegalArgumentException("Person with ID " + request.personId() + " does not exist");
        }

        if (clientRepository.existsByPersonId(personId)) {
            throw new IllegalArgumentException("Client already exists for person with ID " + request.personId());
        }

        // Crear cliente con campos obligatorios
        Client client = Client.create(
                personId,
                request.type(),
                request.category(),
                request.status(),
                request.creditLimit(),
                request.currency(),
                request.notes());

        // Actualizar campos opcionales si están presentes
        if (request.notes() != null && !request.notes().trim().isEmpty()) {
            client.updateNotes(request.notes());
        }

        // Guardar cliente
        Client savedClient = clientRepository.save(client);

        return ClientResponse.from(savedClient);
    }
}


