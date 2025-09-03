package com.veraz.tasks.backend.auth.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationEntryPoint.class);
    private static final String ERROR_KEY = "error";
    private static final String MESSAGE_KEY = "message";
    private static final String PATH_KEY = "path";
    private static final String STATUS_KEY = "status";
    private static final String UNAUTHORIZED_ERROR = "Unauthorized";
    private static final String ACCESS_DENIED_MESSAGE = "Access denied. Please provide valid authentication credentials.";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        logger.error("Unauthorized error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, Object> errorResponse = buildErrorResponse(request);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }

    private Map<String, Object> buildErrorResponse(HttpServletRequest request) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(STATUS_KEY, HttpServletResponse.SC_UNAUTHORIZED);
        errorResponse.put(ERROR_KEY, UNAUTHORIZED_ERROR);
        errorResponse.put(MESSAGE_KEY, ACCESS_DENIED_MESSAGE);
        errorResponse.put(PATH_KEY, request.getServletPath());
        return errorResponse;
    }
}