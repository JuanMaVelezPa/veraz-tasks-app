package com.veraz.tasks.backend.domain.shared.exceptions;

public abstract class InvalidDataException extends DomainException {
    
    protected InvalidDataException(String message) {
        super(message);
    }
    
    protected InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
