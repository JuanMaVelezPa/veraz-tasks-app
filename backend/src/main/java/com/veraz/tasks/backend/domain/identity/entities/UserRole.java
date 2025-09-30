package com.veraz.tasks.backend.domain.identity.entities;

import com.veraz.tasks.backend.domain.identity.valueobjects.UserId;
import com.veraz.tasks.backend.domain.identity.valueobjects.RoleId;
import com.veraz.tasks.backend.domain.identity.exceptions.InvalidUserDataException;
import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.util.MessageUtils;

import lombok.Getter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * UserRole domain entity
 */
@Getter
@ToString
@EqualsAndHashCode(of = {"userId", "roleId"})
public class UserRole {
    
    private final UserId userId;
    private final RoleId roleId;
    private final LocalDateTime assignedAt;
    private LocalDateTime updatedAt;
    
    private UserRole(UserId userId, RoleId roleId, LocalDateTime assignedAt) {
        this.userId = userId;
        this.roleId = roleId;
        this.assignedAt = assignedAt;
        this.updatedAt = assignedAt;
        
        validateBusinessRules();
    }
    
    public static UserRole create(UserId userId, RoleId roleId) {
        return new UserRole(
            userId,
            roleId,
            LocalDateTime.now()
        );
    }
    
    public static UserRole reconstruct(UserId userId, RoleId roleId, 
                                     LocalDateTime assignedAt, LocalDateTime updatedAt) {
        UserRole userRole = new UserRole(
            userId,
            roleId,
            assignedAt
        );
        userRole.updatedAt = updatedAt;
        return userRole;
    }
    
    
    private void validateBusinessRules() {
        if (userId == null) {
            throw new InvalidUserDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_NULL));
        }
        if (roleId == null) {
            throw new InvalidUserDataException(MessageUtils.getMessage(MessageKeys.COMMON_NOT_NULL));
        }
    }
    
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
    
}

