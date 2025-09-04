package com.veraz.tasks.backend.person.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.veraz.tasks.backend.person.dto.ClientCreateRequestDTO;
import com.veraz.tasks.backend.person.dto.ClientUpdateRequestDTO;
import com.veraz.tasks.backend.person.dto.ClientResponseDTO;
import com.veraz.tasks.backend.person.model.Client;
import com.veraz.tasks.backend.person.model.Person;

@Component
public class ClientMapper {

    public ClientResponseDTO toResponseDTO(Client client) {
        if (client == null)
            return null;

        return ClientResponseDTO.builder()
                .id(client.getId())
                .personId(client.getPerson().getId())
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

    public Client toEntity(ClientCreateRequestDTO dto, Person person) {
        if (dto == null)
            return null;

        return Client.builder()
                .person(person)
                .type(dto.getType())
                .category(dto.getCategory())
                .source(dto.getSource())
                .companyName(dto.getCompanyName())
                .companyWebsite(dto.getCompanyWebsite())
                .companyIndustry(dto.getCompanyIndustry())
                .contactPerson(dto.getContactPerson())
                .contactPosition(dto.getContactPosition())
                .address(dto.getAddress())
                .city(dto.getCity())
                .country(dto.getCountry())
                .postalCode(dto.getPostalCode())
                .taxId(dto.getTaxId())
                .creditLimit(dto.getCreditLimit())
                .currency(dto.getCurrency() != null ? dto.getCurrency() : "USD")
                .paymentTerms(dto.getPaymentTerms())
                .paymentMethod(dto.getPaymentMethod())
                .notes(dto.getNotes())
                .preferences(dto.getPreferences())
                .tags(dto.getTags())
                .rating(dto.getRating())
                .status(dto.getStatus() != null ? dto.getStatus() : "ACTIVE")
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();
    }

    public Client updateEntity(Client entity, ClientUpdateRequestDTO dto) {

        if (dto.getType() != null) {
            entity.setType(dto.getType());
        }
        if (dto.getCategory() != null) {
            entity.setCategory(dto.getCategory());
        }
        if (dto.getSource() != null) {
            entity.setSource(dto.getSource());
        }
        if (dto.getCompanyName() != null) {
            entity.setCompanyName(dto.getCompanyName());
        }
        if (dto.getCompanyWebsite() != null) {
            entity.setCompanyWebsite(dto.getCompanyWebsite());
        }
        if (dto.getCompanyIndustry() != null) {
            entity.setCompanyIndustry(dto.getCompanyIndustry());
        }
        if (dto.getContactPerson() != null) {
            entity.setContactPerson(dto.getContactPerson());
        }
        if (dto.getContactPosition() != null) {
            entity.setContactPosition(dto.getContactPosition());
        }
        if (dto.getAddress() != null) {
            entity.setAddress(dto.getAddress());
        }
        if (dto.getCity() != null) {
            entity.setCity(dto.getCity());
        }
        if (dto.getCountry() != null) {
            entity.setCountry(dto.getCountry());
        }
        if (dto.getPostalCode() != null) {
            entity.setPostalCode(dto.getPostalCode());
        }
        if (dto.getTaxId() != null) {
            entity.setTaxId(dto.getTaxId());
        }
        if (dto.getCreditLimit() != null) {
            entity.setCreditLimit(dto.getCreditLimit());
        }
        if (dto.getCurrency() != null) {
            entity.setCurrency(dto.getCurrency());
        }
        if (dto.getPaymentTerms() != null) {
            entity.setPaymentTerms(dto.getPaymentTerms());
        }
        if (dto.getPaymentMethod() != null) {
            entity.setPaymentMethod(dto.getPaymentMethod());
        }
        if (dto.getNotes() != null) {
            entity.setNotes(dto.getNotes());
        }
        if (dto.getPreferences() != null) {
            entity.setPreferences(dto.getPreferences());
        }
        if (dto.getTags() != null) {
            entity.setTags(dto.getTags());
        }
        if (dto.getRating() != null) {
            entity.setRating(dto.getRating());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        if (dto.getIsActive() != null) {
            entity.setIsActive(dto.getIsActive());
        }
        return entity;
    }

    public Set<ClientResponseDTO> toDtoSet(Set<Client> clients) {
        if (clients == null)
            return Set.of();

        return clients.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toSet());
    }
}