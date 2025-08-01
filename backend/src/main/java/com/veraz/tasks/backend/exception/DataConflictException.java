package com.veraz.tasks.backend.exception;

/**
 * Excepción lanzada cuando hay un conflicto de datos
 */
public class DataConflictException extends RuntimeException {
    
    public DataConflictException(String message) {
        super(message);
    }
    
    public DataConflictException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DataConflictException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s ya existe con %s: '%s'", resourceName, fieldName, fieldValue));
    }
} 