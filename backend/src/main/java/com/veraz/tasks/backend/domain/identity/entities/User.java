package com.veraz.tasks.backend.domain.identity.entities;

import com.veraz.tasks.backend.domain.identity.valueobjects.UserId;
import com.veraz.tasks.backend.domain.identity.valueobjects.RoleId;
import com.veraz.tasks.backend.domain.business.valueobjects.Email;
import com.veraz.tasks.backend.domain.identity.exceptions.InvalidUserDataException;
import com.veraz.tasks.backend.domain.identity.services.PasswordEncoder;
import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.util.MessageUtils;

import lombok.Getter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Getter
@ToString(exclude = { "password" })
@EqualsAndHashCode(of = "id")
public class User {

    private final UserId id;
    private String username;
    private String password;
    private Email email;
    private boolean isActive;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<UserRole> userRoles;

    private User(UserId id, String username, String password, Email email, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isActive = true;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        this.userRoles = new ArrayList<>();

        validateBusinessRules();
    }

    public static User create(String username, String password, String email, PasswordEncoder passwordEncoder) {
        if (passwordEncoder == null) {
            throw new InvalidUserDataException(MessageUtils.getMessage(MessageKeys.COMMON_REQUIRED));
        }

        return new User(
                UserId.generate(),
                username,
                passwordEncoder.encode(password),
                Email.of(email),
                LocalDateTime.now());
    }

    public static User reconstruct(UserId id, String username, String password,
            String email, boolean isActive,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        User user = new User(
                id,
                username,
                password,
                Email.of(email),
                createdAt);
        user.isActive = isActive;
        user.updatedAt = updatedAt;
        user.userRoles = new ArrayList<>();
        return user;
    }

    public static User reconstruct(UserId id, String username, String password,
            String email, boolean isActive,
            LocalDateTime createdAt, LocalDateTime updatedAt,
            List<UserRole> userRoles) {
        User user = new User(
                id,
                username,
                password,
                Email.of(email),
                createdAt);
        user.isActive = isActive;
        user.updatedAt = updatedAt;
        user.userRoles = userRoles != null ? userRoles : new ArrayList<>();
        return user;
    }

    private void validateBusinessRules() {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidUserDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidUserDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        if (username.length() < 3) {
            throw new InvalidUserDataException(
                    MessageUtils.getMessage(MessageKeys.VALIDATION_FIELD_MIN_LENGTH, "Username", 3));
        }
        if (password.length() < 6) {
            throw new InvalidUserDataException(
                    MessageUtils.getMessage(MessageKeys.VALIDATION_FIELD_MIN_LENGTH, "Password", 6));
        }
    }

    public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new InvalidUserDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        if (newPassword.length() < 6) {
            throw new InvalidUserDataException(
                    MessageUtils.getMessage(MessageKeys.VALIDATION_FIELD_MIN_LENGTH, "Password", 6));
        }
        if (passwordEncoder == null) {
            throw new InvalidUserDataException(MessageUtils.getMessage(MessageKeys.COMMON_REQUIRED));
        }
        this.password = passwordEncoder.encode(newPassword);
        this.updatedAt = LocalDateTime.now();
    }

    public void changeEmail(String newEmail) {
        if (newEmail == null || newEmail.trim().isEmpty()) {
            throw new InvalidUserDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        this.email = Email.of(newEmail);
        this.updatedAt = LocalDateTime.now();
    }

    public void changeUsername(String newUsername) {
        if (newUsername == null || newUsername.trim().isEmpty()) {
            throw new InvalidUserDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_BLANK));
        }
        if (newUsername.length() < 3) {
            throw new InvalidUserDataException(
                    MessageUtils.getMessage(MessageKeys.VALIDATION_FIELD_MIN_LENGTH, "Username", 3));
        }
        this.username = newUsername.trim();
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

    public void assignRole(RoleId roleId) {
        if (roleId == null) {
            throw new InvalidUserDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_NULL));
        }

        boolean alreadyAssigned = userRoles.stream()
                .anyMatch(userRole -> userRole.getRoleId().equals(roleId));

        if (!alreadyAssigned) {
            UserRole userRole = UserRole.create(this.id, roleId);
            userRoles.add(userRole);
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void removeRole(RoleId roleId) {
        if (roleId == null) {
            throw new InvalidUserDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_NULL));
        }

        userRoles.removeIf(userRole -> userRole.getRoleId().equals(roleId));
        this.updatedAt = LocalDateTime.now();
    }

    public boolean hasRole(RoleId roleId) {
        if (roleId == null) {
            return false;
        }

        return userRoles.stream()
                .anyMatch(userRole -> userRole.getRoleId().equals(roleId));
    }

    public List<RoleId> getRoleIds() {
        return userRoles.stream()
                .map(UserRole::getRoleId)
                .toList();
    }

    public void clearRoles() {
        userRoles.clear();
        this.updatedAt = LocalDateTime.now();
    }

}
