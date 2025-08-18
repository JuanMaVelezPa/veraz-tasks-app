package com.veraz.tasks.backend.person.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.veraz.tasks.backend.auth.model.User;

@Entity
@Table(name = "ge_tpers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pers_pers", nullable = false)
    private UUID id;

    @Column(name = "pers_ident_type", nullable = false, length = 20)
    private String identType;

    @Column(name = "pers_ident_number", nullable = false, length = 20)
    private String identNumber;

    @Column(name = "pers_first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "pers_last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "pers_birth_date")
    private LocalDate birthDate;

    @Column(name = "pers_gender", length = 3)
    private String gender;

    @Column(name = "pers_nationality", length = 50)
    private String nationality;

    @Column(name = "pers_mobile", length = 20)
    private String mobile;

    @Column(name = "pers_email", length = 100)
    private String email;

    @Column(name = "pers_address", length = 255)
    private String address;

    @Column(name = "pers_city", length = 100)
    private String city;

    @Column(name = "pers_country", length = 100)
    private String country;

    @Column(name = "pers_postal_code", length = 20)
    private String postalCode;

    @Column(name = "pers_notes", length = 255)
    private String notes;

    @Column(name = "pers_is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "pers_created_at")
    private LocalDateTime createdAt;

    @Column(name = "pers_updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pers_user", referencedColumnName = "user_user")
    private User user;
    
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
        if (this.gender == null) {
            this.gender = "O";
        }
        this.isActive = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (this.email != null) {
            this.email = this.email.toLowerCase().trim();
        }
        if (this.mobile != null) {
            this.mobile = this.mobile.trim();
        }
        if (this.address != null) {
            this.address = this.address.trim();
        }
        if (this.notes != null) {
            this.notes = this.notes.trim();
        }
        if (this.firstName != null) {
            this.firstName = this.firstName.trim();
        }
        if (this.lastName != null) {
            this.lastName = this.lastName.trim();
        }
    }

}
