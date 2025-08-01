package com.veraz.tasks.backend.exception;

/**
 * Exception thrown when provided data is invalid
 */
public class InvalidDataException extends RuntimeException {
    
    public InvalidDataException(String message) {
        super(message);
    }
    
    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public InvalidDataException(String fieldName, String reason) {
        super(String.format("Field '%s' is invalid: %s", fieldName, reason));
    }
} 