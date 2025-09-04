package com.veraz.tasks.backend.person.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientResponseDTO {
    private UUID id;
    private UUID personId;
    private String type;
    private String category;
    private String source;
    private String companyName;
    private String companyWebsite;
    private String companyIndustry;
    private String contactPerson;
    private String contactPosition;
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private String taxId;
    private BigDecimal creditLimit;
    private String currency;
    private String paymentTerms;
    private String paymentMethod;
    private String notes;
    private String preferences;
    private String tags;
    private Integer rating;
    private String status;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}