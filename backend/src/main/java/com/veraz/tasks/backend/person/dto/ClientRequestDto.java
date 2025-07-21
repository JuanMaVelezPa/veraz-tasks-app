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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientRequestDto {
    @NotNull(message = "{NotNull.client.personId}")
    private UUID personId;

    @NotBlank(message = "{NotBlank.client.clientCode}")
    @Size(min = 3, max = 20, message = "{Size.client.clientCode.min} {Size.client.clientCode.max}")
    private String clientCode;

    @NotBlank(message = "{NotBlank.client.type}")
    @Size(min = 3, max = 20, message = "{Size.client.type.min} {Size.client.type.max}")
    private String type;

    @Size(max = 50, message = "{Size.client.category}")
    private String category;

    @Size(max = 50, message = "{Size.client.source}")
    private String source;

    @Size(max = 200, message = "{Size.client.companyName}")
    private String companyName;

    @Size(max = 255, message = "{Size.client.companyWebsite}")
    private String companyWebsite;

    @Size(max = 100, message = "{Size.client.companyIndustry}")
    private String companyIndustry;

    @Size(max = 200, message = "{Size.client.contactPerson}")
    private String contactPerson;

    @Size(max = 100, message = "{Size.client.contactPosition}")
    private String contactPosition;

    @Size(max = 255, message = "{Size.client.address}")
    private String address;

    @Size(max = 100, message = "{Size.client.city}")
    private String city;

    @Size(max = 100, message = "{Size.client.country}")
    private String country;

    @Size(max = 20, message = "{Size.client.postalCode}")
    private String postalCode;

    @Size(max = 50, message = "{Size.client.taxId}")
    private String taxId;

    private BigDecimal creditLimit;

    @Size(max = 3, message = "{Size.client.currency}")
    private String currency;

    @Size(max = 50, message = "{Size.client.paymentTerms}")
    private String paymentTerms;

    @Size(max = 50, message = "{Size.client.paymentMethod}")
    private String paymentMethod;

    @Size(max = 500, message = "{Size.client.notes}")
    private String notes;
    
    @Size(max = 500, message = "{Size.client.preferences}")
    private String preferences;
    
    @Size(max = 500, message = "{Size.client.tags}")
    private String tags;
    
    private Integer rating;
    
    @Size(max = 20, message = "{Size.client.status}")
    private String status;
}