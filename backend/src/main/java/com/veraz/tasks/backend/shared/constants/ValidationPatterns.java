package com.veraz.tasks.backend.shared.constants;

/**
 * Validation patterns used in the application
 * 
 * Applied principles:
 * - Only patterns that are actually used in validations
 * - Avoid duplication with validation messages
 * - Maintain specific and reusable patterns
 */
public final class ValidationPatterns {

    private ValidationPatterns() {
    }

    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    public static final String PHONE_PATTERN = "^[+]?[0-9]{7,15}$";
    public static final String UUID_PATTERN = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";
}