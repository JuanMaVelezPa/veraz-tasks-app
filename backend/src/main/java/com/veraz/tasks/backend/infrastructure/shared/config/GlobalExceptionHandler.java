package com.veraz.tasks.backend.infrastructure.shared.config;

import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.constants.HttpConstants;
import com.veraz.tasks.backend.shared.dto.ErrorResponseDTO;
import com.veraz.tasks.backend.shared.util.MessageUtils;
import com.veraz.tasks.backend.domain.shared.exceptions.DomainException;
import com.veraz.tasks.backend.domain.shared.exceptions.InvalidDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((err) -> {
            String fieldName = ((FieldError) err).getField();
            String errorMessage = err.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                HttpConstants.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                MessageUtils.getMessage(MessageKeys.EXCEPTION_VALIDATION_FAILED),
                null,
                getCurrentRequestPath(),
                fieldErrors
        );

        logger.warn("Validation failed: {}", fieldErrors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(EntityNotFoundException ex) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                HttpConstants.NOT_FOUND,
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                null,
                getCurrentRequestPath()
        );

        logger.warn("Entity not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<ErrorResponseDTO> handleAuthenticationException(Exception ex) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                HttpConstants.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                MessageUtils.getMessage(MessageKeys.AUTH_INVALID_CREDENTIALS),
                null,
                getCurrentRequestPath()
        );

        logger.warn("Authentication failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                HttpConstants.FORBIDDEN,
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                MessageUtils.getMessage(MessageKeys.EXCEPTION_ACCESS_DENIED),
                null,
                getCurrentRequestPath()
        );

        logger.warn("Access denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                HttpConstants.CONFLICT,
                HttpStatus.CONFLICT.getReasonPhrase(),
                MessageUtils.getMessage(MessageKeys.EXCEPTION_DATA_INTEGRITY_VIOLATION),
                null,
                getCurrentRequestPath()
        );

        logger.error("Data integrity violation: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                HttpConstants.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                MessageUtils.getMessage(MessageKeys.EXCEPTION_CONSTRAINT_VIOLATION),
                null,
                getCurrentRequestPath(),
                fieldErrors
        );

        logger.warn("Constraint violation: {}", fieldErrors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                HttpConstants.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                MessageUtils.getMessage(MessageKeys.EXCEPTION_INVALID_REQUEST_BODY),
                null,
                getCurrentRequestPath()
        );

        logger.warn("Invalid request body: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                HttpConstants.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                MessageUtils.getMessage(MessageKeys.EXCEPTION_MISSING_PARAMETER, ex.getParameterName()),
                null,
                getCurrentRequestPath()
        );

        logger.warn("Missing parameter: {}", ex.getParameterName());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        
        String errorMessage;
        if (ex.getRequiredType() == UUID.class) {
            errorMessage = String.format("El parámetro '%s' debe ser un UUID válido. Valor recibido: '%s'", 
                ex.getName(), ex.getValue());
        } else {
            errorMessage = MessageUtils.getMessage(MessageKeys.EXCEPTION_INVALID_PARAMETER_TYPE, ex.getName(), ex.getValue());
        }
        
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                HttpConstants.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errorMessage,
                null,
                getCurrentRequestPath()
        );

        logger.warn("Invalid parameter type: {} = {} (required type: {})", ex.getName(), ex.getValue(), ex.getRequiredType());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                HttpConstants.NOT_FOUND,
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                MessageUtils.getMessage(MessageKeys.EXCEPTION_ENDPOINT_NOT_FOUND),
                null,
                getCurrentRequestPath()
        );

        logger.warn("Endpoint not found: {} {}", ex.getHttpMethod(), ex.getRequestURL());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoResourceFoundException(NoResourceFoundException ex) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                HttpConstants.NOT_FOUND,
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                "Recurso no encontrado: " + ex.getResourcePath(),
                null,
                getCurrentRequestPath()
        );

        logger.warn("Static resource not found: {}", ex.getResourcePath());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // Domain-specific exception handlers for Clean Architecture
    // Optimized: Single handler for all invalid data exceptions using base class
    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidDataExceptions(InvalidDataException ex) {
        String exceptionType = ex.getClass().getSimpleName();
        logger.warn("Invalid data exception [{}]: {}", exceptionType, ex.getMessage());

        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
            HttpConstants.BAD_REQUEST,
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getMessage(),
            null,
            getCurrentRequestPath()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Generic domain exception handler
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponseDTO> handleDomainExceptions(DomainException ex) {
        String exceptionType = ex.getClass().getSimpleName();
        logger.warn("Domain exception [{}]: {}", exceptionType, ex.getMessage());

        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
            HttpConstants.BAD_REQUEST,
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getMessage(),
            null,
            getCurrentRequestPath()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        String errorId = UUID.randomUUID().toString();
        
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
            HttpConstants.INTERNAL_SERVER_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            MessageUtils.getMessage(MessageKeys.EXCEPTION_INTERNAL_SERVER_ERROR),
            errorId,
            getCurrentRequestPath()
        );

        logger.error("Unexpected error occurred [ID: {}]: {}", errorId, ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private String getCurrentRequestPath() {
        try {
            Object attribute = org.springframework.web.context.request.RequestContextHolder
                    .currentRequestAttributes()
                    .getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingPattern", 0);
            return attribute != null ? attribute.toString() : "unknown";
        } catch (Exception e) {
            return "unknown";
        }
    }
}
