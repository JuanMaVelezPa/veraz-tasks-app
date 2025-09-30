package com.veraz.tasks.backend.domain.identity.exceptions;

import com.veraz.tasks.backend.domain.shared.exceptions.DomainException;
import lombok.experimental.StandardException;

@StandardException
public class UserNotFoundException extends DomainException {
    
    public UserNotFoundException(String userId) {
        super("User not found with ID: " + userId);
    }
}
