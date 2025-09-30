package com.veraz.tasks.backend.infrastructure.business.persistence;

import com.veraz.tasks.backend.domain.business.entities.Client;
import com.veraz.tasks.backend.domain.business.valueobjects.ClientId;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Client JPA entity
 * 
 * Maps Client domain entity to database table.
 * Part of the infrastructure layer in Clean Architecture.
 */
@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntity {
    
    @Id
    @Column(name = "clients_id", nullable = false)
    private UUID id;
    
    @Column(name = "persons_id", nullable = false)
    private UUID personId;
    
    @Column(name = "type", nullable = false, length = 20)
    private String type;
    
    @Column(name = "category", length = 50)
    private String category;
    
    @Column(name = "source", length = 50)
    private String source;
    
    @Column(name = "company_name", length = 200)
    private String companyName;
    
    @Column(name = "company_website", length = 255)
    private String companyWebsite;
    
    @Column(name = "company_industry", length = 100)
    private String companyIndustry;
    
    @Column(name = "contact_person", length = 200)
    private String contactPerson;
    
    @Column(name = "contact_position", length = 100)
    private String contactPosition;
    
    @Column(name = "address", length = 255)
    private String address;
    
    @Column(name = "city", length = 100)
    private String city;
    
    @Column(name = "country", length = 100)
    private String country;
    
    @Column(name = "postal_code", length = 20)
    private String postalCode;
    
    @Column(name = "tax_id", length = 50)
    private String taxId;
    
    @Column(name = "currency", length = 3)
    private String currency;
    
    @Column(name = "payment_terms", length = 50)
    private String paymentTerms;
    
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;
    
    @Column(name = "preferences", columnDefinition = "TEXT")
    private String preferences;
    
    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags;
    
    @Column(name = "rating")
    private Integer rating;
    
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    @Column(name = "credit_limit", precision = 15, scale = 2)
    private BigDecimal creditLimit;
    
    @Column(name = "current_balance", precision = 15, scale = 2)
    private BigDecimal currentBalance;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public static ClientEntity from(Client client) {
        ClientEntity entity = new ClientEntity();
        entity.id = client.getId().getValue();
        entity.personId = client.getPersonId().getValue();
        entity.type = client.getType();
        entity.category = client.getCategory();
        entity.source = client.getSource();
        entity.companyName = client.getCompanyName();
        entity.companyWebsite = client.getCompanyWebsite();
        entity.companyIndustry = client.getCompanyIndustry();
        entity.contactPerson = client.getContactPerson();
        entity.contactPosition = client.getContactPosition();
        entity.address = client.getAddress();
        entity.city = client.getCity();
        entity.country = client.getCountry();
        entity.postalCode = client.getPostalCode();
        entity.taxId = client.getTaxId();
        entity.creditLimit = client.getCreditLimit();
        entity.currency = client.getCurrency();
        entity.paymentTerms = client.getPaymentTerms();
        entity.paymentMethod = client.getPaymentMethod();
        entity.preferences = client.getPreferences();
        entity.tags = client.getTags();
        entity.rating = client.getRating();
        entity.status = client.getStatus();
        entity.notes = client.getNotes();
        entity.currentBalance = client.getCurrentBalance();
        entity.isActive = client.isActive();
        entity.createdAt = client.getCreatedAt();
        entity.updatedAt = client.getUpdatedAt();
        return entity;
    }
    
    public Client toDomain() {
        return Client.reconstruct(
            ClientId.of(this.id),
            PersonId.of(this.personId),
            this.type,
            this.category,
            this.source,
            this.companyName,
            this.companyWebsite,
            this.companyIndustry,
            this.contactPerson,
            this.contactPosition,
            this.address,
            this.city,
            this.country,
            this.postalCode,
            this.taxId,
            this.creditLimit,
            this.currency,
            this.paymentTerms,
            this.paymentMethod,
            this.preferences,
            this.tags,
            this.rating,
            this.status,
            this.notes,
            this.currentBalance,
            this.isActive,
            this.createdAt,
            this.updatedAt
        );
    }
}

