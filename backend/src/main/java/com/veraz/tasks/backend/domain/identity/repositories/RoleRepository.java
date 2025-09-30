package com.veraz.tasks.backend.domain.identity.repositories;

import com.veraz.tasks.backend.domain.identity.entities.Role;
import com.veraz.tasks.backend.domain.identity.valueobjects.RoleId;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {

    Role save(Role role);

    Optional<Role> findById(RoleId id);

    Optional<Role> findByName(String name);

    List<Role> findAll();

    List<Role> findAllActive();

    boolean existsByName(String name);

    void delete(Role role);

    void deleteById(RoleId id);

    long count();
}
