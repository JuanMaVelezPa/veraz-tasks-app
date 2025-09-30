package com.veraz.tasks.backend.application.business.commands;

import com.veraz.tasks.backend.domain.business.repositories.ClientRepository;
import com.veraz.tasks.backend.domain.business.valueobjects.ClientId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class DeleteClientUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(DeleteClientUseCase.class);
    
    private final ClientRepository clientRepository;
    
    public DeleteClientUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    
    public void execute(String clientIdString) {
        logger.debug("Deleting client with ID: {}", clientIdString);
        
        try {
            ClientId clientId = ClientId.of(clientIdString);
            
            Optional<com.veraz.tasks.backend.domain.business.entities.Client> clientOpt = clientRepository.findById(clientId);
            if (clientOpt.isEmpty()) {
                throw new IllegalArgumentException("Client not found with ID: " + clientIdString);
            }
            
            clientRepository.deleteById(clientId);
            
            logger.debug("Successfully deleted client with ID: {}", clientIdString);
            
        } catch (Exception e) {
            logger.error("Error deleting client {}: {}", clientIdString, e.getMessage(), e);
            throw e;
        }
    }
}

