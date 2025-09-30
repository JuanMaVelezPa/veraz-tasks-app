package com.veraz.tasks.backend.domain.identity.services;

import com.veraz.tasks.backend.domain.identity.entities.User;

/**
 * Domain service for JWT token generation and validation
 * 
 * This interface defines the contract for JWT operations.
 * Implementation details are provided by the infrastructure layer.
 * 
 * Part of the domain layer in Clean Architecture.
 */
public interface JwtTokenGenerator {

    String generateToken(User user);
    boolean validateToken(String token);
    String getUserIdFromToken(String token);
}

