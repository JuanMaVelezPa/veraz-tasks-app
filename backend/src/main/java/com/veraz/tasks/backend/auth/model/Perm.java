package com.veraz.tasks.backend.auth.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ge_tperm")
@Data
@NoArgsConstructor
public class Perm {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "perm_perm")
    private UUID Id;

    @Column(name = "perm_name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "perm_description", length = 255)
    private String description;

    @Column(name = "perm_is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "perm_created_at")
    private LocalDateTime createdAt;

    @Column(name = "perm_updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
