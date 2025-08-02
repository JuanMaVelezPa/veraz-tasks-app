package com.veraz.tasks.backend.person.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import com.veraz.tasks.backend.person.dto.ClientCreateRequestDTO;
import com.veraz.tasks.backend.person.dto.ClientUpdateRequestDTO;
import com.veraz.tasks.backend.person.dto.ClientResponseDTO;
import com.veraz.tasks.backend.person.model.Client;
import com.veraz.tasks.backend.person.model.Person;

public class ClientMapper {

    /**
     * Converts Client entity to ClientResponseDTO
     * @param client Client entity
     * @return ClientResponseDTO
     */
    public static ClientResponseDTO toDto(Client client) {
        if (client == null) return null;
        
        return ClientResponseDTO.builder()
                .id(client.getId())
                .personId(client.getPerson().getId())
                .clientCode(client.getClientCode())
                .type(client.getType())
                .category(client.getCategory())
                .source(client.getSource())
                .companyName(client.getCompanyName())
                .companyWebsite(client.getCompanyWebsite())
                .companyIndustry(client.getCompanyIndustry())
                .contactPerson(client.getContactPerson())
                .contactPosition(client.getContactPosition())
                .address(client.getAddress())
                .city(client.getCity())
                .country(client.getCountry())
                .postalCode(client.getPostalCode())
                .taxId(client.getTaxId())
                .creditLimit(client.getCreditLimit())
                .currency(client.getCurrency())
                .paymentTerms(client.getPaymentTerms())
                .paymentMethod(client.getPaymentMethod())
                .notes(client.getNotes())
                .preferences(client.getPreferences())
                .tags(client.getTags())
                .rating(client.getRating())
                .status(client.getStatus())
                .isActive(client.getIsActive())
                .createdAt(client.getCreatedAt())
                .updatedAt(client.getUpdatedAt())
                .build();
    }

    /**
     * Converts ClientCreateRequestDTO to Client entity for creation
     * @param clientRequest Create request DTO
     * @param person Person entity
     * @return Client entity
     */
    public static Client toEntity(ClientCreateRequestDTO clientRequest, Person person) {
        if (clientRequest == null) return null;
        
        return Client.builder()
                .person(person)
                .clientCode(clientRequest.getClientCode())
                .type(clientRequest.getType())
                .category(clientRequest.getCategory())
                .source(clientRequest.getSource())
                .companyName(clientRequest.getCompanyName())
                .companyWebsite(clientRequest.getCompanyWebsite())
                .companyIndustry(clientRequest.getCompanyIndustry())
                .contactPerson(clientRequest.getContactPerson())
                .contactPosition(clientRequest.getContactPosition())
                .address(clientRequest.getAddress())
                .city(clientRequest.getCity())
                .country(clientRequest.getCountry())
                .postalCode(clientRequest.getPostalCode())
                .taxId(clientRequest.getTaxId())
                .creditLimit(clientRequest.getCreditLimit())
                .currency(clientRequest.getCurrency())
                .paymentTerms(clientRequest.getPaymentTerms())
                .paymentMethod(clientRequest.getPaymentMethod())
                .notes(clientRequest.getNotes())
                .preferences(clientRequest.getPreferences())
                .tags(clientRequest.getTags())
                .rating(clientRequest.getRating())
                .status(clientRequest.getStatus())
                .isActive(clientRequest.getIsActive())
                .build();
    }

    /**
     * Updates an existing Client entity with data from ClientUpdateRequestDTO
     * @param client Existing client entity
     * @param clientRequest Update request DTO
     * @return Updated Client entity
     */
    public static Client updateEntity(Client client, ClientUpdateRequestDTO clientRequest) {
        if (client == null || clientRequest == null) return client;
        
        // Update client code if provided and not empty
        if (clientRequest.getClientCode() != null && !clientRequest.getClientCode().trim().isEmpty()) {
            client.setClientCode(clientRequest.getClientCode().trim());
        }
        
        // Update type if provided and not empty
        if (clientRequest.getType() != null && !clientRequest.getType().trim().isEmpty()) {
            client.setType(clientRequest.getType().trim());
        }
        
        // Update category if provided and not empty
        if (clientRequest.getCategory() != null && !clientRequest.getCategory().trim().isEmpty()) {
            client.setCategory(clientRequest.getCategory().trim());
        }
        
        // Update source if provided and not empty
        if (clientRequest.getSource() != null && !clientRequest.getSource().trim().isEmpty()) {
            client.setSource(clientRequest.getSource().trim());
        }
        
        // Update company name if provided and not empty
        if (clientRequest.getCompanyName() != null && !clientRequest.getCompanyName().trim().isEmpty()) {
            client.setCompanyName(clientRequest.getCompanyName().trim());
        }
        
        // Update company website if provided and not empty
        if (clientRequest.getCompanyWebsite() != null && !clientRequest.getCompanyWebsite().trim().isEmpty()) {
            client.setCompanyWebsite(clientRequest.getCompanyWebsite().trim());
        }
        
        // Update company industry if provided and not empty
        if (clientRequest.getCompanyIndustry() != null && !clientRequest.getCompanyIndustry().trim().isEmpty()) {
            client.setCompanyIndustry(clientRequest.getCompanyIndustry().trim());
        }
        
        // Update contact person if provided and not empty
        if (clientRequest.getContactPerson() != null && !clientRequest.getContactPerson().trim().isEmpty()) {
            client.setContactPerson(clientRequest.getContactPerson().trim());
        }
        
        // Update contact position if provided and not empty
        if (clientRequest.getContactPosition() != null && !clientRequest.getContactPosition().trim().isEmpty()) {
            client.setContactPosition(clientRequest.getContactPosition().trim());
        }
        
        // Update address if provided and not empty
        if (clientRequest.getAddress() != null && !clientRequest.getAddress().trim().isEmpty()) {
            client.setAddress(clientRequest.getAddress().trim());
        }
        
        // Update city if provided and not empty
        if (clientRequest.getCity() != null && !clientRequest.getCity().trim().isEmpty()) {
            client.setCity(clientRequest.getCity().trim());
        }
        
        // Update country if provided and not empty
        if (clientRequest.getCountry() != null && !clientRequest.getCountry().trim().isEmpty()) {
            client.setCountry(clientRequest.getCountry().trim());
        }
        
        // Update postal code if provided and not empty
        if (clientRequest.getPostalCode() != null && !clientRequest.getPostalCode().trim().isEmpty()) {
            client.setPostalCode(clientRequest.getPostalCode().trim());
        }
        
        // Update tax ID if provided and not empty
        if (clientRequest.getTaxId() != null && !clientRequest.getTaxId().trim().isEmpty()) {
            client.setTaxId(clientRequest.getTaxId().trim());
        }
        
        // Update credit limit if provided
        if (clientRequest.getCreditLimit() != null) {
            client.setCreditLimit(clientRequest.getCreditLimit());
        }
        
        // Update currency if provided and not empty
        if (clientRequest.getCurrency() != null && !clientRequest.getCurrency().trim().isEmpty()) {
            client.setCurrency(clientRequest.getCurrency().trim());
        }
        
        // Update payment terms if provided and not empty
        if (clientRequest.getPaymentTerms() != null && !clientRequest.getPaymentTerms().trim().isEmpty()) {
            client.setPaymentTerms(clientRequest.getPaymentTerms().trim());
        }
        
        // Update payment method if provided and not empty
        if (clientRequest.getPaymentMethod() != null && !clientRequest.getPaymentMethod().trim().isEmpty()) {
            client.setPaymentMethod(clientRequest.getPaymentMethod().trim());
        }
        
        // Update notes if provided and not empty
        if (clientRequest.getNotes() != null && !clientRequest.getNotes().trim().isEmpty()) {
            client.setNotes(clientRequest.getNotes().trim());
        }
        
        // Update preferences if provided and not empty
        if (clientRequest.getPreferences() != null && !clientRequest.getPreferences().trim().isEmpty()) {
            client.setPreferences(clientRequest.getPreferences().trim());
        }
        
        // Update tags if provided and not empty
        if (clientRequest.getTags() != null && !clientRequest.getTags().trim().isEmpty()) {
            client.setTags(clientRequest.getTags().trim());
        }
        
        // Update rating if provided
        if (clientRequest.getRating() != null) {
            client.setRating(clientRequest.getRating());
        }
        
        // Update status if provided and not empty
        if (clientRequest.getStatus() != null && !clientRequest.getStatus().trim().isEmpty()) {
            client.setStatus(clientRequest.getStatus().trim());
        }
        
        // Update active status if provided
        if (clientRequest.getIsActive() != null) {
            client.setIsActive(clientRequest.getIsActive());
        }
        
        return client;
    }

    /**
     * Converts a set of Clients to DTOs
     * @param clients Set of clients
     * @return Set of DTOs
     */
    public static Set<ClientResponseDTO> toDtoSet(Set<Client> clients) {
        if (clients == null) return Set.of();
        
        return clients.stream()
                .map(ClientMapper::toDto)
                .collect(Collectors.toSet());
    }
} 