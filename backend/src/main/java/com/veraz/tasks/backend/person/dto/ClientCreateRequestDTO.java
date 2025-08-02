package com.veraz.tasks.backend.person.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for creating new clients
 * Contains all required fields with mandatory validations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientCreateRequestDTO {
    @NotNull(message = "{validation.field.required}")
    private UUID personId;

    @NotBlank(message = "{validation.field.required}")
    @Size(min = 3, max = 20, message = "{validation.field.size}")
    private String clientCode;

    @NotBlank(message = "{validation.field.required}")
    @Size(min = 3, max = 20, message = "{validation.field.size}")
    private String type;

    @Size(max = 50, message = "{validation.field.max.length}")
    private String category;

    @Size(max = 50, message = "{validation.field.max.length}")
    private String source;

    @Size(max = 200, message = "{validation.field.max.length}")
    private String companyName;

    @Size(max = 255, message = "{validation.field.max.length}")
    private String companyWebsite;

    @Size(max = 100, message = "{validation.field.max.length}")
    private String companyIndustry;

    @Size(max = 200, message = "{validation.field.max.length}")
    private String contactPerson;

    @Size(max = 100, message = "{validation.field.max.length}")
    private String contactPosition;

    @Size(max = 255, message = "{validation.field.max.length}")
    private String address;

    @Size(max = 100, message = "{validation.field.max.length}")
    private String city;

    @Size(max = 100, message = "{validation.field.max.length}")
    private String country;

    @Size(max = 20, message = "{validation.field.max.length}")
    private String postalCode;

    @Size(max = 50, message = "{validation.field.max.length}")
    private String taxId;

    private BigDecimal creditLimit;

    @Size(max = 3, message = "{validation.field.max.length}")
    private String currency;

    @Size(max = 50, message = "{validation.field.max.length}")
    private String paymentTerms;

    @Size(max = 50, message = "{validation.field.max.length}")
    private String paymentMethod;

    @Size(max = 500, message = "{validation.field.max.length}")
    private String notes;
    
    @Size(max = 500, message = "{validation.field.max.length}")
    private String preferences;
    
    @Size(max = 500, message = "{validation.field.max.length}")
    private String tags;
    
    private Integer rating;
    
    @Size(max = 20, message = "{validation.field.max.length}")
    private String status;

    private Boolean isActive;
} 