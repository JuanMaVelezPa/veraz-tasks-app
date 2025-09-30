package com.veraz.tasks.backend.domain.business.entities;

import com.veraz.tasks.backend.domain.business.valueobjects.ClientId;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;
import com.veraz.tasks.backend.domain.business.exceptions.InvalidClientDataException;
import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.util.MessageUtils;

import lombok.Getter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Client domain entity
 */
@Getter
@ToString(exclude = {"notes"})
@EqualsAndHashCode(of = "id")
public class Client {

    private final ClientId id;
    private final PersonId personId;
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
    private String preferences;
    private String tags;
    private Integer rating;
    private String status;
    private String notes;
    private BigDecimal currentBalance;
    private boolean isActive;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Client(ClientId id, PersonId personId, String type, String category, String source,
            String companyName, String companyWebsite, String companyIndustry, String contactPerson,
            String contactPosition, String address, String city, String country, String postalCode,
            String taxId, BigDecimal creditLimit, String currency, String paymentTerms,
            String paymentMethod, String preferences, String tags, Integer rating, String status,
            String notes, BigDecimal currentBalance, LocalDateTime createdAt) {
        this.id = id;
        this.personId = personId;
        this.type = type;
        this.category = category;
        this.source = source;
        this.companyName = companyName;
        this.companyWebsite = companyWebsite;
        this.companyIndustry = companyIndustry;
        this.contactPerson = contactPerson;
        this.contactPosition = contactPosition;
        this.address = address;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.taxId = taxId;
        this.creditLimit = creditLimit;
        this.currency = currency;
        this.paymentTerms = paymentTerms;
        this.paymentMethod = paymentMethod;
        this.preferences = preferences;
        this.tags = tags;
        this.rating = rating;
        this.status = status;
        this.notes = notes;
        this.currentBalance = currentBalance;
        this.isActive = true;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;

        validateBusinessRules();
    }

    public static Client create(PersonId personId, String type, String category, String status,
            BigDecimal creditLimit, String currency, String notes) {
        return new Client(
                ClientId.generate(),
                personId,
                type,
                category,
                null, // source
                null, // companyName
                null, // companyWebsite
                null, // companyIndustry
                null, // contactPerson
                null, // contactPosition
                null, // address
                null, // city
                null, // country
                null, // postalCode
                null, // taxId
                creditLimit,
                currency,
                null, // paymentTerms
                null, // paymentMethod
                null, // preferences
                null, // tags
                null, // rating
                status,
                notes, // notes
                BigDecimal.ZERO, // currentBalance
                LocalDateTime.now());
    }

    public static Client reconstruct(ClientId id, PersonId personId, String type, String category, String source,
            String companyName, String companyWebsite, String companyIndustry, String contactPerson,
            String contactPosition, String address, String city, String country, String postalCode,
            String taxId, BigDecimal creditLimit, String currency, String paymentTerms,
            String paymentMethod, String preferences, String tags, Integer rating, String status,
            String notes, BigDecimal currentBalance, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        Client client = new Client(
                id,
                personId,
                type,
                category,
                source,
                companyName,
                companyWebsite,
                companyIndustry,
                contactPerson,
                contactPosition,
                address,
                city,
                country,
                postalCode,
                taxId,
                creditLimit,
                currency,
                paymentTerms,
                paymentMethod,
                preferences,
                tags,
                rating,
                status,
                notes,
                currentBalance,
                createdAt);
        client.isActive = isActive;
        client.updatedAt = updatedAt;
        return client;
    }

    private void validateBusinessRules() {
        if (personId == null) {
            throw new InvalidClientDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_NULL));
        }
        if (type == null || type.trim().isEmpty()) {
            throw new InvalidClientDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        if (status == null || status.trim().isEmpty()) {
            throw new InvalidClientDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        if (creditLimit != null && creditLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidClientDataException(MessageUtils.getMessage(MessageKeys.VALIDATION_FIELD_DECIMAL_MIN, "Credit limit"));
        }
        if (rating != null && (rating < 1 || rating > 5)) {
            throw new InvalidClientDataException("Rating must be between 1 and 5");
        }
    }

    public void updateType(String newType) {
        if (newType == null || newType.trim().isEmpty()) {
            throw new InvalidClientDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        this.type = newType;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(String newStatus) {
        if (newStatus == null || newStatus.trim().isEmpty()) {
            throw new InvalidClientDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateCreditLimit(BigDecimal newCreditLimit) {
        if (newCreditLimit != null && newCreditLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidClientDataException(MessageUtils.getMessage(MessageKeys.VALIDATION_FIELD_DECIMAL_MIN, "Credit limit"));
        }
        this.creditLimit = newCreditLimit;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateNotes(String newNotes) {
        this.notes = newNotes;
        this.updatedAt = LocalDateTime.now();
    }

    public void addToBalance(BigDecimal amount) {
        if (amount == null) {
            throw new InvalidClientDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_NULL));
        }
        this.currentBalance = this.currentBalance.add(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public void updateCompanyInfo(String newCompanyName, String newCompanyWebsite, String newCompanyIndustry) {
        this.companyName = newCompanyName;
        this.companyWebsite = newCompanyWebsite;
        this.companyIndustry = newCompanyIndustry;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateContactInfo(String newContactPerson, String newContactPosition) {
        this.contactPerson = newContactPerson;
        this.contactPosition = newContactPosition;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateAddress(String newAddress, String newCity, String newCountry, String newPostalCode) {
        this.address = newAddress;
        this.city = newCity;
        this.country = newCountry;
        this.postalCode = newPostalCode;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateFinancialInfo(BigDecimal newCreditLimit, String newCurrency, String newPaymentTerms, String newPaymentMethod) {
        if (newCreditLimit != null && newCreditLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidClientDataException(MessageUtils.getMessage(MessageKeys.VALIDATION_FIELD_DECIMAL_MIN, "Credit limit"));
        }
        this.creditLimit = newCreditLimit;
        this.currency = newCurrency;
        this.paymentTerms = newPaymentTerms;
        this.paymentMethod = newPaymentMethod;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateRating(Integer newRating) {
        if (newRating != null && (newRating < 1 || newRating > 5)) {
            throw new InvalidClientDataException("Rating must be between 1 and 5");
        }
        this.rating = newRating;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updatePreferences(String newPreferences, String newTags) {
        this.preferences = newPreferences;
        this.tags = newTags;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateCategory(String newCategory) {
        this.category = newCategory;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateSource(String newSource) {
        this.source = newSource;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateTaxId(String newTaxId) {
        this.taxId = newTaxId;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

}

