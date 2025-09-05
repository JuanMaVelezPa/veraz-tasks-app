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
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "clients_id", nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "persons_id", referencedColumnName = "persons_id")
    private Person person;

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

    @Column(name = "credit_limit", precision = 20, scale = 2)
    private BigDecimal creditLimit;

    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "payment_terms", length = 50)
    private String paymentTerms;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "preferences", columnDefinition = "TEXT")
    private String preferences;

    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
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