package com.veraz.tasks.backend.infrastructure.identity.auth;

import com.veraz.tasks.backend.domain.identity.entities.User;
import com.veraz.tasks.backend.domain.identity.repositories.UserRepository;
import com.veraz.tasks.backend.domain.identity.repositories.RoleRepository;
import com.veraz.tasks.backend.domain.identity.valueobjects.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DomainUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public DomainUserDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmailForAuthentication(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return createUserDetails(user);
    }

    public UserDetails loadUserById(UUID userId) {
        User user = userRepository.findByIdForAuthentication(UserId.of(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

        return createUserDetails(user);
    }

    private UserDetails createUserDetails(User user) {
        logger.debug("Creating UserDetails for user: {} with {} user roles",
                user.getUsername(), user.getUserRoles().size());

        List<SimpleGrantedAuthority> authorities = user.getUserRoles().stream()
                .map(userRole -> {
                    String roleName = roleRepository.findById(userRole.getRoleId())
                            .map(role -> role.getName())
                            .orElse("UNKNOWN");

                    String authority = "ROLE_" + roleName.toUpperCase();
                    logger.debug("Adding role authority: {} for role ID: {} (role name: {})",
                            authority, userRole.getRoleId().getValue(), roleName);
                    return new SimpleGrantedAuthority(authority);
                })
                .collect(Collectors.toList());

        if (authorities.isEmpty()) {
            logger.debug("No roles found for user: {}, assigning default ROLE_USER", user.getUsername());
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        logger.debug("Final authorities for user {}: {}", user.getUsername(), authorities);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.isActive())
                .build();
    }
}
