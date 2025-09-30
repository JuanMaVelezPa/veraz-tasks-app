package com.veraz.tasks.backend.infrastructure.identity.persistence;

import com.veraz.tasks.backend.domain.identity.entities.User;
import com.veraz.tasks.backend.domain.identity.valueobjects.UserId;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(exclude = { "userRoles" })
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @Column(name = "users_id", nullable = false)
    private UUID id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = false)
    private Set<UserRoleEntity> userRoles;

    public static UserEntity from(User user) {
        UserEntity entity = new UserEntity();
        entity.id = user.getId().getValue();
        entity.username = user.getUsername();
        entity.password = user.getPassword();
        entity.email = user.getEmail() != null ? user.getEmail().getValue() : null;
        entity.isActive = user.isActive();
        entity.createdAt = user.getCreatedAt();
        entity.updatedAt = user.getUpdatedAt();
        entity.userRoles = new java.util.HashSet<>();

        return entity;
    }

    public User toDomain() {
        List<com.veraz.tasks.backend.domain.identity.entities.UserRole> domainUserRoles = this.userRoles != null
                ? this.userRoles.stream()
                        .map(UserRoleEntity::toDomain)
                        .collect(Collectors.toList())
                : List.of();

        return User.reconstruct(
                UserId.of(this.id),
                this.username,
                this.password,
                this.email,
                this.isActive,
                this.createdAt,
                this.updatedAt,
                domainUserRoles);
    }
}
