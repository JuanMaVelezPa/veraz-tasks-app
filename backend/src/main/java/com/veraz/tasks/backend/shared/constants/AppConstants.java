package com.veraz.tasks.backend.shared.constants;

/**
 * Essential application constants
 * 
 * Applied principles:
 * - Only constants that are actually used in multiple places
 * - Values that do not change and are critical for the application
 * - Avoid duplication with validations and messages
 */
public final class AppConstants {

    private AppConstants() {
        // Private constructor to prevent instantiation
    }

    // ===========================================
    // PAGINATION CONSTANTS (used in PaginationRequestDTO)
    // ===========================================

    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MIN_PAGE_SIZE = 1;
    public static final int MAX_PAGE_SIZE = 100;
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final String DEFAULT_SORT_BY = "createdAt";
    public static final String DEFAULT_SORT_DIRECTION = "desc";

    // ===========================================
    // ROLES CONSTANTS (used in SecurityConfig)
    // ===========================================

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_SUPERVISOR = "SUPERVISOR";
    public static final String ROLE_USER = "USER";

    // ===========================================
    // STATUS CONSTANTS (used in entities)
    // ===========================================

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";

    // ===========================================
    // HTTP CONSTANTS (used in GlobalExceptionHandler)
    // ===========================================

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    // ===========================================
    // CACHE CONSTANTS (used in services)
    // ===========================================

    public static final String CACHE_USERS = "users";
    public static final String CACHE_PERSONS = "persons";
    public static final int CACHE_TTL_DEFAULT = 3600; // 1 hora en segundos

    // ===========================================
    // LOGGING CONSTANTS (used in services)
    // ===========================================

    public static final String LOG_PREFIX_API = "[API]";
    public static final String LOG_PREFIX_SERVICE = "[SERVICE]";
    public static final String LOG_PREFIX_REPOSITORY = "[REPOSITORY]";
    public static final String LOG_PREFIX_SECURITY = "[SECURITY]";
}