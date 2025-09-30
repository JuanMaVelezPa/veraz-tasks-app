package com.veraz.tasks.backend.application.identity.commands;

import com.veraz.tasks.backend.domain.identity.entities.User;
import com.veraz.tasks.backend.domain.identity.repositories.UserRepository;
import com.veraz.tasks.backend.domain.identity.valueobjects.UserId;
import com.veraz.tasks.backend.domain.identity.valueobjects.RoleId;
import com.veraz.tasks.backend.domain.identity.exceptions.InvalidUserDataException;
import org.springframework.stereotype.Service;

@Service
public class RemoveRoleFromUserUseCase {
    
    private final UserRepository userRepository;
    
    public RemoveRoleFromUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User execute(UserId userId, RoleId roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidUserDataException("User not found: " + userId.getValue()));
        
        if (!user.hasRole(roleId)) {
            throw new InvalidUserDataException("Role is not assigned to user");
        }
        
        user.removeRole(roleId);
        return userRepository.save(user);
    }
}

