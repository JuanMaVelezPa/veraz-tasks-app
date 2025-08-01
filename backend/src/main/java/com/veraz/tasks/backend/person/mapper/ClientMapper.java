package com.veraz.tasks.backend.person.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import com.veraz.tasks.backend.person.dto.ClientRequestDTO;
import com.veraz.tasks.backend.person.dto.ClientResponseDTO;
import com.veraz.tasks.backend.person.model.Client;
import com.veraz.tasks.backend.person.model.Person;

public class ClientMapper {

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

    public static Client toEntity(ClientRequestDTO clientRequest, Person person) {
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
     * Actualiza una entidad Client existente con datos del DTO
     * @param client Entidad existente
     * @param clientRequest DTO con datos a actualizar
     * @return Client actualizado
     */
    public static Client updateEntity(Client client, ClientRequestDTO clientRequest) {
        if (client == null || clientRequest == null) return client;
        
        if (clientRequest.getClientCode() != null) {
            client.setClientCode(clientRequest.getClientCode().trim());
        }
        
        if (clientRequest.getType() != null) {
            client.setType(clientRequest.getType());
        }
        
        if (clientRequest.getCategory() != null) {
            client.setCategory(clientRequest.getCategory());
        }
        
        if (clientRequest.getSource() != null) {
            client.setSource(clientRequest.getSource());
        }
        
        if (clientRequest.getCompanyName() != null) {
            client.setCompanyName(clientRequest.getCompanyName().trim());
        }
        
        if (clientRequest.getCompanyWebsite() != null) {
            client.setCompanyWebsite(clientRequest.getCompanyWebsite().trim());
        }
        
        if (clientRequest.getCompanyIndustry() != null) {
            client.setCompanyIndustry(clientRequest.getCompanyIndustry());
        }
        
        if (clientRequest.getContactPerson() != null) {
            client.setContactPerson(clientRequest.getContactPerson().trim());
        }
        
        if (clientRequest.getContactPosition() != null) {
            client.setContactPosition(clientRequest.getContactPosition());
        }
        
        if (clientRequest.getAddress() != null) {
            client.setAddress(clientRequest.getAddress().trim());
        }
        
        if (clientRequest.getCity() != null) {
            client.setCity(clientRequest.getCity());
        }
        
        if (clientRequest.getCountry() != null) {
            client.setCountry(clientRequest.getCountry());
        }
        
        if (clientRequest.getPostalCode() != null) {
            client.setPostalCode(clientRequest.getPostalCode());
        }
        
        if (clientRequest.getTaxId() != null) {
            client.setTaxId(clientRequest.getTaxId().trim());
        }
        
        if (clientRequest.getCreditLimit() != null) {
            client.setCreditLimit(clientRequest.getCreditLimit());
        }
        
        if (clientRequest.getCurrency() != null) {
            client.setCurrency(clientRequest.getCurrency());
        }
        
        if (clientRequest.getPaymentTerms() != null) {
            client.setPaymentTerms(clientRequest.getPaymentTerms());
        }
        
        if (clientRequest.getPaymentMethod() != null) {
            client.setPaymentMethod(clientRequest.getPaymentMethod());
        }
        
        if (clientRequest.getNotes() != null) {
            client.setNotes(clientRequest.getNotes().trim());
        }
        
        if (clientRequest.getPreferences() != null) {
            client.setPreferences(clientRequest.getPreferences().trim());
        }
        
        if (clientRequest.getTags() != null) {
            client.setTags(clientRequest.getTags().trim());
        }
        
        if (clientRequest.getRating() != null) {
            client.setRating(clientRequest.getRating());
        }
        
        if (clientRequest.getStatus() != null) {
            client.setStatus(clientRequest.getStatus());
        }
        
        if (clientRequest.getIsActive() != null) {
            client.setIsActive(clientRequest.getIsActive());
        }
        
        return client;
    }

    /**
     * Convierte una lista de Clients a DTOs
     * @param clients Lista de clientes
     * @return Lista de DTOs
     */
    public static Set<ClientResponseDTO> toDtoSet(Set<Client> clients) {
        if (clients == null) return Set.of();
        
        return clients.stream()
                .map(ClientMapper::toDto)
                .collect(Collectors.toSet());
    }

} 