package com.veraz.tasks.backend.application.business.commands;

import com.veraz.tasks.backend.application.business.dto.ClientResponse;
import com.veraz.tasks.backend.domain.business.entities.Client;
import com.veraz.tasks.backend.domain.business.repositories.ClientRepository;
import com.veraz.tasks.backend.domain.business.valueobjects.ClientId;
import com.veraz.tasks.backend.infrastructure.business.dto.UpdateClientRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class UpdateClientUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(UpdateClientUseCase.class);
    
    private final ClientRepository clientRepository;
    
    public UpdateClientUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    
    public ClientResponse execute(String clientIdString, UpdateClientRequest request) {
        logger.debug("Updating client with ID: {}", clientIdString);
        
        try {
            ClientId clientId = ClientId.of(clientIdString);

            Optional<Client> clientOpt = clientRepository.findById(clientId);
            if (clientOpt.isEmpty()) {
                throw new IllegalArgumentException("Client not found with ID: " + clientIdString);
            }
            
            Client client = clientOpt.get();
            
            if (request.type() != null) {
                client.updateType(request.type());
            }
            if (request.category() != null) {
                client.updateCategory(request.category());
            }
            if (request.source() != null) {
                client.updateSource(request.source());
            }
            if (request.companyName() != null || request.companyWebsite() != null || request.companyIndustry() != null) {
                client.updateCompanyInfo(
                    request.companyName() != null ? request.companyName() : client.getCompanyName(),
                    request.companyWebsite() != null ? request.companyWebsite() : client.getCompanyWebsite(),
                    request.companyIndustry() != null ? request.companyIndustry() : client.getCompanyIndustry()
                );
            }
            if (request.contactPerson() != null || request.contactPosition() != null) {
                client.updateContactInfo(
                    request.contactPerson() != null ? request.contactPerson() : client.getContactPerson(),
                    request.contactPosition() != null ? request.contactPosition() : client.getContactPosition()
                );
            }
            if (request.address() != null || request.city() != null || request.country() != null || request.postalCode() != null) {
                client.updateAddress(
                    request.address() != null ? request.address() : client.getAddress(),
                    request.city() != null ? request.city() : client.getCity(),
                    request.country() != null ? request.country() : client.getCountry(),
                    request.postalCode() != null ? request.postalCode() : client.getPostalCode()
                );
            }
            if (request.taxId() != null) {
                client.updateTaxId(request.taxId());
            }
            if (request.creditLimit() != null || request.currency() != null || request.paymentTerms() != null || request.paymentMethod() != null) {
                client.updateFinancialInfo(
                    request.creditLimit() != null ? request.creditLimit() : client.getCreditLimit(),
                    request.currency() != null ? request.currency() : client.getCurrency(),
                    request.paymentTerms() != null ? request.paymentTerms() : client.getPaymentTerms(),
                    request.paymentMethod() != null ? request.paymentMethod() : client.getPaymentMethod()
                );
            }
            if (request.preferences() != null || request.tags() != null) {
                client.updatePreferences(
                    request.preferences() != null ? request.preferences() : client.getPreferences(),
                    request.tags() != null ? request.tags() : client.getTags()
                );
            }
            if (request.rating() != null) {
                client.updateRating(request.rating());
            }
            if (request.status() != null) {
                client.updateStatus(request.status());
            }
            if (request.notes() != null) {
                client.updateNotes(request.notes());
            }
            if (request.isActive() != null) {
                if (request.isActive()) {
                    client.activate();
                } else {
                    client.deactivate();
                }
            }

            Client updatedClient = clientRepository.save(client);
            
            logger.debug("Successfully updated client with ID: {}", clientIdString);
            return ClientResponse.from(updatedClient);
            
        } catch (Exception e) {
            logger.error("Error updating client {}: {}", clientIdString, e.getMessage(), e);
            throw e;
        }
    }
}


