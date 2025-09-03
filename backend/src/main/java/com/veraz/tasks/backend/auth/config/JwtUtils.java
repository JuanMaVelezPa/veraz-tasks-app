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
    private static final int MIN_SECRET_LENGTH = 32;
    private static final int SECRET_PREVIEW_LENGTH = 10;
    private static final String DEFAULT_SECRET = "V3r4zT4sks@AppP@sS@pP@sS@w0Rd1o10sS";
    private static final String DEVELOPMENT_KEYWORD = "development";
    private static final String DEV_KEYWORD = "dev";

    @Value("${veraz.app.jwtSecret}")
    private String jwtSecret;

    @Value("${veraz.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private SecretKey getSigningKey() {
        validateJwtSecret();
        logSecretUsage();
        
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private void validateJwtSecret() {
        if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
            logger.error("JWT secret is null or empty!");
            throw new IllegalArgumentException("JWT secret cannot be null or empty");
        }

        if (jwtSecret.length() < MIN_SECRET_LENGTH) {
            logger.error("JWT secret is too short! Must be at least {} characters for security.", MIN_SECRET_LENGTH);
            throw new IllegalArgumentException("JWT secret must be at least " + MIN_SECRET_LENGTH + " characters long");
        }
    }

    private void logSecretUsage() {
        if (jwtSecret.equals(DEFAULT_SECRET)) {
            logger.warn("Using default JWT secret! This is not secure for production.");
        }

        if (jwtSecret.contains(DEVELOPMENT_KEYWORD) || jwtSecret.contains(DEV_KEYWORD)) {
            logger.warn("Using development JWT secret! This is not secure for production.");
        }
        
        String secretPreview = jwtSecret.substring(0, Math.min(SECRET_PREVIEW_LENGTH, jwtSecret.length()));
        logger.debug("Using JWT secret: {}...", secretPreview);
    }

    public String generateJwtToken(User user) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtExpirationMs);
        
        return Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiration(expirationDate)
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