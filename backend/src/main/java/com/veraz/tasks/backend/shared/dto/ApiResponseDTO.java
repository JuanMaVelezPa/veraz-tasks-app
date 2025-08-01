package com.veraz.tasks.backend.shared.dto;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseDTO<T> {

    private boolean success;
    private HttpStatus status;
    private String message;
    private T data;
    private List<String> errors;

}
