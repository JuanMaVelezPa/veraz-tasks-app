package com.veraz.tasks.backend.person.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ge_tclie")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "clie_clie", nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "clie_pers", referencedColumnName = "pers_pers")
    private Person person;

    @Column(name = "clie_type", nullable = false, length = 20)
    private String type;

    @Column(name = "clie_category", length = 50)
    private String category;

    @Column(name = "clie_source", length = 50)
    private String source;

    @Column(name = "clie_company_name", length = 200)
    private String companyName;

    @Column(name = "clie_company_website", length = 255)
    private String companyWebsite;

    @Column(name = "clie_company_industry", length = 100)
    private String companyIndustry;

    @Column(name = "clie_contact_person", length = 200)
    private String contactPerson;

    @Column(name = "clie_contact_position", length = 100)
    private String contactPosition;

    @Column(name = "clie_address", length = 255)
    private String address;

    @Column(name = "clie_city", length = 100)
    private String city;

    @Column(name = "clie_country", length = 100)
    private String country;

    @Column(name = "clie_postal_code", length = 20)
    private String postalCode;

    @Column(name = "clie_tax_id", length = 50)
    private String taxId;

    @Column(name = "clie_credit_limit")
    private BigDecimal creditLimit;

    @Column(name = "clie_currency", length = 3)
    private String currency;

    @Column(name = "clie_payment_terms", length = 50)
    private String paymentTerms;

    @Column(name = "clie_payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "clie_notes")
    private String notes;

    @Column(name = "clie_preferences")
    private String preferences;

    @Column(name = "clie_tags")
    private String tags;

    @Column(name = "clie_rating")
    private Integer rating;

    @Column(name = "clie_status", nullable = false, length = 20)
    private String status;

    @Column(name = "clie_is_active")
    private Boolean isActive;

    @Column(name = "clie_created_at")
    private LocalDateTime createdAt;

    @Column(name = "clie_updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
        this.isActive = true;
        this.status = "ACTIVE";
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}