package com.veraz.tasks.backend.infrastructure.identity.auth;

import com.veraz.tasks.backend.domain.identity.services.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderImpl implements PasswordEncoder {
    
    private final org.springframework.security.crypto.password.PasswordEncoder springPasswordEncoder;
    
    public PasswordEncoderImpl(org.springframework.security.crypto.password.PasswordEncoder springPasswordEncoder) {
        this.springPasswordEncoder = springPasswordEncoder;
    }
    
    @Override
    public String encode(String rawPassword) {
        return springPasswordEncoder.encode(rawPassword);
    }
    
    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return springPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}

