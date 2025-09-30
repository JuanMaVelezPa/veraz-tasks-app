package com.veraz.tasks.backend.shared.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ApiResponseDTO<T>(
        boolean success,
        HttpStatus status,
        String message,
        T data,
        List<String> errors) {

    public static <T> ApiResponseDTO<T> success(T data, String message) {
        return new ApiResponseDTO<>(true, HttpStatus.OK, message, data, null);
    }

    public static <T> ApiResponseDTO<T> success(HttpStatus status, T data, String message) {
        return new ApiResponseDTO<>(true, status, message, data, null);
    }

    public static <T> ApiResponseDTO<T> error(HttpStatus status, String message) {
        return new ApiResponseDTO<>(false, status, message, null, null);
    }

    public static <T> ApiResponseDTO<T> error(HttpStatus status, String message, List<String> errors) {
        return new ApiResponseDTO<>(false, status, message, null, errors);
    }
}
