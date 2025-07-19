package com.veraz.tasks.backend.auth.config;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.veraz.tasks.backend.auth.model.User;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${veraz.app.jwtSecret}")
    private String jwtSecret;

    @Value("${veraz.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private SecretKey getSigningKey() {
        if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
            logger.error("JWT secret is null or empty!");
            throw new IllegalStateException("JWT secret is not configured");
        }
        
        // Validación de seguridad: el secreto debe tener al menos 32 caracteres (256 bits)
        if (jwtSecret.length() < 32) {
            logger.error("JWT secret is too short! Must be at least 32 characters for security.");
            throw new IllegalStateException("JWT secret must be at least 32 characters long");
        }
        
        // Validación adicional: no usar el valor por defecto en producción
        if (jwtSecret.equals("V3r4zT4sks@AppP@sS@pP@sS@w0Rd1o10sS")) {
            logger.warn("Using default JWT secret! This is not secure for production.");
        }
        
        // Validación adicional: no usar el valor de desarrollo en producción
        if (jwtSecret.equals("dev-jwt-secret-for-development-only-32-chars")) {
            logger.warn("Using development JWT secret! This is not secure for production.");
        }
        
        logger.debug("Using JWT secret: {}...", jwtSecret.substring(0, Math.min(10, jwtSecret.length())));
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    public String getUserIDFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (JwtException e) {
            logger.debug("JWT token validation failed: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.debug("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

}