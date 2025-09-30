package com.veraz.tasks.backend.domain.identity.exceptions;

import com.veraz.tasks.backend.domain.shared.exceptions.DomainException;
import lombok.experimental.StandardException;

@StandardException
public class RoleNotFoundException extends DomainException {
    
    public RoleNotFoundException(String roleName) {
        super("Role not found with name: " + roleName);
    }
}
