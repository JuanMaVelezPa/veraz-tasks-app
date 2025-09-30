package com.veraz.tasks.backend.domain.business.exceptions;

import com.veraz.tasks.backend.domain.shared.exceptions.InvalidDataException;
import lombok.experimental.StandardException;

/**
 * Exception thrown when employee data is invalid
 * 
 * Domain-specific exception for business rule violations.
 * Follows Clean Architecture principles.
 */
@StandardException
public class InvalidEmployeeDataException extends InvalidDataException {
    // Lombok generates constructors automatically
}

