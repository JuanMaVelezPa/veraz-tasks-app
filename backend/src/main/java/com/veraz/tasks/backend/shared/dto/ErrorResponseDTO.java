package com.veraz.tasks.backend.shared.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponseDTO(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String errorId,
    String path,
    Map<String, String> fieldErrors
) {
    
    public static ErrorResponseDTO of(int status, String error, String message) {
        return new ErrorResponseDTO(
            LocalDateTime.now(),
            status,
            error,
            message,
            null,
            null,
            null
        );
    }
    
    public static ErrorResponseDTO of(int status, String error, String message, String errorId) {
        return new ErrorResponseDTO(
            LocalDateTime.now(),
            status,
            error,
            message,
            errorId,
            null,
            null
        );
    }
    
    public static ErrorResponseDTO of(int status, String error, String message, String errorId, String path) {
        return new ErrorResponseDTO(
            LocalDateTime.now(),
            status,
            error,
            message,
            errorId,
            path,
            null
        );
    }
    
    public static ErrorResponseDTO of(int status, String error, String message, String errorId, String path, Map<String, String> fieldErrors) {
        return new ErrorResponseDTO(
            LocalDateTime.now(),
            status,
            error,
            message,
            errorId,
            path,
            fieldErrors
        );
    }
}