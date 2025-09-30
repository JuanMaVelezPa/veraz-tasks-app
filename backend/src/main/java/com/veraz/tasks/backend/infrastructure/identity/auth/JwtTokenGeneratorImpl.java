package com.veraz.tasks.backend.infrastructure.identity.auth;

import com.veraz.tasks.backend.domain.identity.entities.User;
import com.veraz.tasks.backend.domain.identity.services.JwtTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenGeneratorImpl implements JwtTokenGenerator {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenGeneratorImpl.class);
    private static final int MIN_SECRET_LENGTH = 32;
    private static final int SECRET_PREVIEW_LENGTH = 10;
    private static final String DEFAULT_SECRET = "V3r4zT4sks@AppP@sS@pP@sS@w0Rd1o10sS@Secure@Key@2024@Production@Ready";
    private static final String DEVELOPMENT_KEYWORD = "development";
    private static final String DEV_KEYWORD = "dev";

    @Value("${veraz.app.jwtSecret}")
    private String jwtSecret;

    @Value("${veraz.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Override
    public String generateToken(User user) {
        logger.debug("Generating token for user: {}", user.getId().getValue());

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtExpirationMs);

        String token = Jwts.builder()
                .subject(user.getId().getValue().toString())
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();

        logger.debug("Token generated successfully for user: {}", user.getId().getValue());
        return token;
    }

    @Override
    public boolean validateToken(String token) {
        logger.debug("Validating token: {}...", token.substring(0, Math.min(20, token.length())));

        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            logger.debug("Token validation result: true");
            return true;
        } catch (JwtException e) {
            logger.debug("JWT token validation failed: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.debug("JWT claims string is empty: {}", e.getMessage());
        }
        logger.debug("Token validation result: false");
        return false;
    }

    @Override
    public String getUserIdFromToken(String token) {
        logger.debug("Extracting user ID from token: {}...", token.substring(0, Math.min(20, token.length())));

        String userId = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        logger.debug("Extracted user ID: {}", userId);
        return userId;
    }

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
        } else if (jwtSecret.contains(DEVELOPMENT_KEYWORD) || jwtSecret.contains(DEV_KEYWORD)) {
            logger.warn("Using development JWT secret! This is not secure for production.");
        } else {
            String secretPreview = jwtSecret.substring(0, Math.min(SECRET_PREVIEW_LENGTH, jwtSecret.length()));
            logger.debug("Using JWT secret: {}...", secretPreview);
        }
    }
}
