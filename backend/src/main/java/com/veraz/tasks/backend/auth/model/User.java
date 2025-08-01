package com.veraz.tasks.backend.auth.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.veraz.tasks.backend.person.model.Person;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ge_tuser")
@Data
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_user")
    private UUID id;

    @Column(name = "user_username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "user_email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "user_pwd", nullable = false, length = 255)
    @JsonIgnore
    private String password;

    @Column(name = "user_is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "user_created_at")
    private LocalDateTime createdAt;

    @Column(name = "user_updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ge_tusro", joinColumns = @JoinColumn(name = "usro_user"), inverseJoinColumns = @JoinColumn(name = "usro_role"))
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Person person;

    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles.add(role);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
        if (this.email != null) {
            this.email = this.email.toLowerCase().trim();
        }
        if (this.username != null) {
            this.username = this.username.toLowerCase().trim();
        }
        if (this.isActive == null) {
            this.isActive = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (this.email != null) {
            this.email = this.email.toLowerCase().trim();
        }
        if (this.username != null) {
            this.username = this.username.toLowerCase().trim();
        }
    }

    // UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }

        if (this.roles != null) {
            this.roles.forEach(role -> {
                if (role.getPerms() != null) {
                    role.getPerms().forEach(perm -> {
                        authorities.add(new SimpleGrantedAuthority("PERM_" + perm.getName()));
                    });
                }
            });
        }

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;
    }
}
