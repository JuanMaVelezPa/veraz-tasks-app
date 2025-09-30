package com.veraz.tasks.backend.application.business.queries;

import com.veraz.tasks.backend.application.business.dto.ClientResponse;
import com.veraz.tasks.backend.domain.business.entities.Client;
import com.veraz.tasks.backend.domain.business.repositories.ClientRepository;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FindClientByPersonIdUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(FindClientByPersonIdUseCase.class);
    
    private final ClientRepository clientRepository;
    
    public FindClientByPersonIdUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    
    @Transactional(readOnly = true)
    public ClientResponse execute(String personIdString) {
        try {
            PersonId personId = PersonId.of(personIdString);
            
            Optional<Client> clientOpt = clientRepository.findByPersonId(personId);
            if (clientOpt.isEmpty()) {
                return null; // Devolver null en lugar de lanzar excepción
            }
            
            Client client = clientOpt.get();
            return ClientResponse.from(client);
            
        } catch (Exception e) {
            logger.error("Error finding client by person ID {}: {}", personIdString, e.getMessage(), e);
            throw e;
        }
    }
}

