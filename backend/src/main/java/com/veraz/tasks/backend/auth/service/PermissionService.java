package com.veraz.tasks.backend.auth.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.veraz.tasks.backend.auth.model.User;
import com.veraz.tasks.backend.person.model.Employee;
import com.veraz.tasks.backend.person.repository.EmployeeRepository;
import com.veraz.tasks.backend.shared.constants.AppConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * Service for role-based access control and resource ownership validation.
 */
@Service
@Slf4j
public class PermissionService {

    private final EmployeeRepository employeeRepository;

    private static final String[] WRITE_ROLES = {
            "ROLE_" + AppConstants.ROLE_ADMIN,
            "ROLE_" + AppConstants.ROLE_MANAGER
    };
    private static final String[] READ_ROLES = {
            "ROLE_" + AppConstants.ROLE_ADMIN,
            "ROLE_" + AppConstants.ROLE_MANAGER,
            "ROLE_" + AppConstants.ROLE_SUPERVISOR
    };

    public PermissionService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return (User) authentication.getPrincipal();
    }

    private boolean hasAnyRole(String... roles) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        return currentUser.getAuthorities().stream()
                .anyMatch(authority -> {
                    String userRole = authority.getAuthority();
                    for (String role : roles) {
                        if (userRole.equals(role)) {
                            return true;
                        }
                    }
                    return false;
                });
    }

    public boolean hasAdminAccess() {
        return hasAnyRole(WRITE_ROLES);
    }

    public boolean canWriteResources() {
        return hasAnyRole(WRITE_ROLES);
    }

    public boolean canReadResources() {
        return hasAnyRole(READ_ROLES);
    }

    public boolean canAccessOwnProfile() {
        User currentUser = getCurrentUser();
        return currentUser != null && currentUser.getPerson() != null;
    }

    public boolean canAccessResource(UUID resourceId, String resourceType) {
        if (canReadResources()) {
            return true;
        }

        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        switch (resourceType.toUpperCase()) {
            case "PERSON":
                return currentUser.getPerson() != null &&
                        currentUser.getPerson().getId().equals(resourceId);
            case "USER":
                return currentUser.getId().equals(resourceId);
            case "EMPLOYEE":
                return isEmployeeOwner(resourceId);
            case "CLIENT":
                return isClientOwner(resourceId);
            default:
                log.warn("Unknown resource type: {}", resourceType);
                return false;
        }
    }

    public boolean isResourceOwner(UUID resourceId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        return currentUser.getId().equals(resourceId);
    }

    /**
     * Checks if current user owns the person or has admin access.
     */
    public boolean isPersonOwner(UUID personId) {
        if (canWriteResources()) {
            return true;
        }

        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        return currentUser.getPerson() != null &&
                currentUser.getPerson().getId().equals(personId);
    }

    /**
     * Checks if current user owns the employee or has admin access.
     * Relationship: User -> Person -> Employee
     */
    public boolean isEmployeeOwner(UUID employeeId) {
        if (canWriteResources()) {
            return true;
        }

        User currentUser = getCurrentUser();
        if (currentUser == null || currentUser.getPerson() == null) {
            return false;
        }

        try {
            Optional<Employee> employee = employeeRepository.findById(employeeId);
            if (employee.isPresent()) {
                return employee.get().getPerson().getId().equals(currentUser.getPerson().getId());
            }
            return false;
        } catch (Exception e) {
            log.error("Error checking employee ownership for employeeId: {}", employeeId, e);
            return false;
        }
    }

    public boolean canCreatePerson(UUID personUserId) {
        if (canWriteResources()) {
            return true;
        }

        User currentUser = getCurrentUser();
        return currentUser != null && currentUser.getId().equals(personUserId);
    }

    public boolean canCreateEmployee(UUID personId) {
        if (canWriteResources()) {
            return true;
        }

        User currentUser = getCurrentUser();
        return currentUser != null &&
                currentUser.getPerson() != null &&
                currentUser.getPerson().getId().equals(personId);
    }

    public boolean canCreateClient(UUID personId) {
        if (canWriteResources()) {
            return true;
        }

        User currentUser = getCurrentUser();
        return currentUser != null &&
                currentUser.getPerson() != null &&
                currentUser.getPerson().getId().equals(personId);
    }

    private boolean isClientOwner(UUID clientId) {
        User currentUser = getCurrentUser();
        if (currentUser == null || currentUser.getPerson() == null) {
            return false;
        }

        return true;
    }
}
