package com.veraz.tasks.backend.domain.identity.services;

/**
 * Domain service for password encoding and verification
 * 
 * This interface defines the contract for password operations.
 * Implementation details are provided by the infrastructure layer.
 * 
 * Part of the domain layer in Clean Architecture.
 */
public interface PasswordEncoder {
    
    /**
     * Encodes a raw password
     * 
     * @param rawPassword the raw password to encode
     * @return the encoded password
     */
    String encode(String rawPassword);
    
    /**
     * Verifies a raw password against an encoded password
     * 
     * @param rawPassword the raw password to verify
     * @param encodedPassword the encoded password to verify against
     * @return true if the passwords match, false otherwise
     */
    boolean matches(String rawPassword, String encodedPassword);
}

