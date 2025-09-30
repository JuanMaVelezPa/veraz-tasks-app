package com.veraz.tasks.backend.infrastructure.identity.persistence;

import com.veraz.tasks.backend.domain.identity.entities.UserRole;
import com.veraz.tasks.backend.domain.identity.valueobjects.UserId;
import com.veraz.tasks.backend.domain.identity.valueobjects.RoleId;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * UserRole JPA entity
 */
@Entity
@Table(name = "user_roles", 
       uniqueConstraints = @UniqueConstraint(name = "uq_user_roles_user_role", 
                                           columnNames = {"users_id", "roles_id"}))
@Data
@EqualsAndHashCode(exclude = {"user", "role"})
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleEntity {
    
    @Id
    @Column(name = "user_roles_id", nullable = false)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private UserEntity user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roles_id", nullable = false)
    private RoleEntity role;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public static UserRoleEntity from(UserRole userRole, UserEntity userEntity, RoleEntity roleEntity) {
        UserRoleEntity entity = new UserRoleEntity();
        entity.id = UUID.randomUUID();
        entity.user = userEntity;
        entity.role = roleEntity; // Use the complete role entity
        entity.createdAt = userRole.getAssignedAt();
        entity.updatedAt = userRole.getUpdatedAt();
        return entity;
    }
    
    public UserRole toDomain() {
        // Always use the simple reconstruct since we don't need role details for UserRole
        return UserRole.reconstruct(
            UserId.of(this.user.getId()),
            RoleId.of(this.role.getId()),
            this.createdAt,
            this.updatedAt
        );
    }
}

