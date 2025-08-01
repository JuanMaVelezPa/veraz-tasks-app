package com.veraz.tasks.backend.shared.constants;

import org.springframework.http.HttpStatus;

/**
 * Standardized HTTP constants for the application
 * 
 * Applied principles:
 * - Centralize commonly used HTTP status codes
 * - Facilitate consistent usage in controllers
 * - Improve code readability
 */
public final class HttpConstants {

    private HttpConstants() {
        // Private constructor to prevent instantiation
    }

    // ===========================================
    // HTTP STATUS CODES (2xx - Success)
    // ===========================================

    public static final int OK = HttpStatus.OK.value(); // 200
    public static final int CREATED = HttpStatus.CREATED.value(); // 201
    public static final int NO_CONTENT = HttpStatus.NO_CONTENT.value(); // 204

    // ===========================================
    // HTTP STATUS CODES (4xx - Client Errors)
    // ===========================================

    public static final int BAD_REQUEST = HttpStatus.BAD_REQUEST.value(); // 400
    public static final int UNAUTHORIZED = HttpStatus.UNAUTHORIZED.value(); // 401
    public static final int FORBIDDEN = HttpStatus.FORBIDDEN.value(); // 403
    public static final int NOT_FOUND = HttpStatus.NOT_FOUND.value(); // 404
    public static final int CONFLICT = HttpStatus.CONFLICT.value(); // 409
    public static final int UNPROCESSABLE_ENTITY = HttpStatus.UNPROCESSABLE_ENTITY.value(); // 422

    // ===========================================
    // HTTP STATUS CODES (5xx - Server Errors)
    // ===========================================

    public static final int INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.value(); // 500
    public static final int SERVICE_UNAVAILABLE = HttpStatus.SERVICE_UNAVAILABLE.value(); // 503

    // ===========================================
    // COMMON HTTP HEADERS
    // ===========================================

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

    // ===========================================
    // HEADER VALUES
    // ===========================================

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_XML = "application/xml";
    public static final String CACHE_NO_CACHE = "no-cache";
    public static final String CACHE_PRIVATE = "private";
    public static final String CACHE_PUBLIC = "public";

    // ===========================================
    // HTTP METHODS
    // ===========================================

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_PATCH = "PATCH";
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_OPTIONS = "OPTIONS";

    // ===========================================
    // UTILITY METHODS
    // ===========================================

    /**
     * Checks if the status code is a client error (4xx)
     */
    public static boolean isClientError(int statusCode) {
        return statusCode >= 400 && statusCode < 500;
    }

    /**
     * Checks if the status code is a server error (5xx)
     */
    public static boolean isServerError(int statusCode) {
        return statusCode >= 500 && statusCode < 600;
    }

    /**
     * Checks if the status code is successful (2xx)
     */
    public static boolean isSuccess(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }

    /**
     * Checks if the status code is a redirection (3xx)
     */
    public static boolean isRedirect(int statusCode) {
        return statusCode >= 300 && statusCode < 400;
    }
}