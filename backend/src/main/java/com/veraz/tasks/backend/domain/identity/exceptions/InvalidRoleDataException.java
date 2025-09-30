package com.veraz.tasks.backend.domain.identity.exceptions;

import com.veraz.tasks.backend.domain.shared.exceptions.InvalidDataException;
import lombok.experimental.StandardException;

/**
 * Exception thrown when role data is invalid
 * 
 * Domain-specific exception for business rule violations.
 * Follows Clean Architecture principles.
 */
@StandardException
public class InvalidRoleDataException extends InvalidDataException {
    // Lombok generates constructors automatically
}

