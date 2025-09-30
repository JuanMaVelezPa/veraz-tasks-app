package com.veraz.tasks.backend.application.business.queries;

import com.veraz.tasks.backend.application.business.dto.ClientResponse;
import com.veraz.tasks.backend.domain.business.repositories.ClientRepository;
import com.veraz.tasks.backend.domain.business.valueobjects.ClientId;

import java.util.Optional;

public class FindClientByIdUseCase {
    
    private final ClientRepository clientRepository;
    
    public FindClientByIdUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    
    public Optional<ClientResponse> execute(String clientIdString) {
        return Optional.of(ClientId.of(clientIdString))
                .flatMap(clientRepository::findById)
                .map(ClientResponse::from);
    }
}

