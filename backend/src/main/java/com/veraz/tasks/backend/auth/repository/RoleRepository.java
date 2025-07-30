package com.veraz.tasks.backend.auth.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.veraz.tasks.backend.auth.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Role findByName(String name);

    @Query("SELECT r FROM Role r WHERE r.isActive = true")
    List<Role> findAllByIsActiveTrue();

}
