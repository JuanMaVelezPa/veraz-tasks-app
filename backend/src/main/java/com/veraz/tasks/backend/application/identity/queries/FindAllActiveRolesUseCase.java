package com.veraz.tasks.backend.application.identity.queries;

import com.veraz.tasks.backend.application.business.dto.RoleResponse;
import com.veraz.tasks.backend.domain.identity.entities.Role;
import com.veraz.tasks.backend.domain.identity.repositories.RoleRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindAllActiveRolesUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindAllActiveRolesUseCase.class);

    private final RoleRepository roleRepository;

    public FindAllActiveRolesUseCase(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public List<RoleResponse> execute() {
        try {
            List<Role> activeRoles = roleRepository.findAllActive();

            List<RoleResponse> response = activeRoles.stream()
                    .map(RoleResponse::from)
                    .collect(Collectors.toList());

            return response;

        } catch (Exception e) {
            logger.error("Error finding active roles: {}", e.getMessage(), e);
            throw e;
        }
    }
}
