package com.veraz.tasks.backend.person.mapper;

import com.veraz.tasks.backend.person.dto.ClientDto;
import com.veraz.tasks.backend.person.dto.ClientRequestDto;
import com.veraz.tasks.backend.person.dto.PersonDto;
import com.veraz.tasks.backend.person.model.Client;
import com.veraz.tasks.backend.person.model.Person;

public class ClientMapper {
    public static ClientDto toDto(Client client) {
        if (client == null) return null;
        PersonDto personDto = PersonMapper.toDto(client.getPerson());
        return ClientDto.builder()
                .id(client.getId())
                .person(personDto)
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

    public static Client toEntity(ClientRequestDto dto, Person person) {
        if (dto == null || person == null) return null;
        return Client.builder()
                .person(person)
                .clientCode(dto.getClientCode())
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
                .currency(dto.getCurrency())
                .paymentTerms(dto.getPaymentTerms())
                .paymentMethod(dto.getPaymentMethod())
                .notes(dto.getNotes())
                .preferences(dto.getPreferences())
                .tags(dto.getTags())
                .rating(dto.getRating())
                .status(dto.getStatus())
                .build();
    }
} 