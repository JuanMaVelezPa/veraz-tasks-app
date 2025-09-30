package com.veraz.tasks.backend.application.business.dto;

import com.veraz.tasks.backend.domain.business.entities.Client;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for Client
 * 
 * A Response represents data returned to the client.
 * Contains only information necessary for presentation.
 * 
 * Using Record for immutability and conciseness (Java 14+)
 */
public record ClientResponse(
    String id,
    String personId,
    String type,
    String category,
    String source,
    String companyName,
    String companyWebsite,
    String companyIndustry,
    String contactPerson,
    String contactPosition,
    String address,
    String city,
    String country,
    String postalCode,
    String taxId,
    BigDecimal creditLimit,
    String currency,
    String paymentTerms,
    String paymentMethod,
    String preferences,
    String tags,
    Integer rating,
    String status,
    BigDecimal currentBalance,
    String notes,
    boolean isActive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    
    /**
     * Factory method to create a response from a domain entity
     */
    public static ClientResponse from(Client client) {
        return new ClientResponse(
            client.getId().getValueAsString(),
            client.getPersonId().getValueAsString(),
            client.getType(),
            client.getCategory(),
            client.getSource(),
            client.getCompanyName(),
            client.getCompanyWebsite(),
            client.getCompanyIndustry(),
            client.getContactPerson(),
            client.getContactPosition(),
            client.getAddress(),
            client.getCity(),
            client.getCountry(),
            client.getPostalCode(),
            client.getTaxId(),
            client.getCreditLimit(),
            client.getCurrency(),
            client.getPaymentTerms(),
            client.getPaymentMethod(),
            client.getPreferences(),
            client.getTags(),
            client.getRating(),
            client.getStatus(),
            client.getCurrentBalance(),
            client.getNotes(),
            client.isActive(),
            client.getCreatedAt(),
            client.getUpdatedAt()
        );
    }
}

