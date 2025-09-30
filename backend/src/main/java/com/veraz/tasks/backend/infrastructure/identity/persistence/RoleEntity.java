package com.veraz.tasks.backend.infrastructure.identity.persistence;

import com.veraz.tasks.backend.domain.identity.entities.Role;
import com.veraz.tasks.backend.domain.identity.valueobjects.RoleId;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {

    @Id
    @Column(name = "roles_id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static RoleEntity from(Role role) {
        RoleEntity entity = new RoleEntity();
        entity.id = role.getId().getValue();
        entity.name = role.getName();
        entity.description = role.getDescription();
        entity.isActive = role.isActive();
        entity.createdAt = role.getCreatedAt();
        entity.updatedAt = role.getUpdatedAt();
        return entity;
    }

    public Role toDomain() {
        return Role.reconstruct(
                RoleId.of(this.id),
                this.name,
                this.description,
                this.isActive,
                this.createdAt,
                this.updatedAt);
    }
}
