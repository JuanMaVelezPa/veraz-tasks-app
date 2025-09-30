package com.veraz.tasks.backend.infrastructure.identity.auth;

import com.veraz.tasks.backend.domain.identity.services.JwtTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.UUID;

@Component
public class CleanAuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(CleanAuthTokenFilter.class);

    private final JwtTokenGenerator jwtTokenGenerator;
    private final DomainUserDetailsService userDetailsService;

    public CleanAuthTokenFilter(JwtTokenGenerator jwtTokenGenerator,
            DomainUserDetailsService userDetailsService) {
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwtToken = extractJwtToken(request);
            processJwtToken(jwtToken);
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    private void processJwtToken(String jwtToken) {
        if (jwtToken == null) {
            logger.debug("No JWT token found in request");
            return;
        }

        if (!jwtTokenGenerator.validateToken(jwtToken)) {
            logger.warn("Invalid JWT token provided");
            return;
        }

        try {
            String userIdString = jwtTokenGenerator.getUserIdFromToken(jwtToken);
            UUID userId = UUID.fromString(userIdString);
            UserDetails userDetails = userDetailsService.loadUserById(userId);

            if (userDetails != null) {
                setAuthentication(userDetails);
            } else {
                logger.warn("User not found for ID: {}", userIdString);
            }
        } catch (Exception e) {
            logger.error("Error processing JWT token: {}", e.getMessage(), e);
        }
    }

    private void setAuthentication(UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String extractJwtToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
