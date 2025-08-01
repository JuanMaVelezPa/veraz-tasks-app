package com.veraz.tasks.backend.auth.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.veraz.tasks.backend.auth.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrEmailAllIgnoreCase(String username, String email);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN u.roles r WHERE " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(r.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findByUsernameOrEmailOrRolesNameContainingIgnoreCase(@Param("searchTerm") String searchTerm,
            Pageable pageable);

    // Additional useful methods
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByUsernameOrEmail(String username, String email);

    List<User> findByIsActive(Boolean isActive);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName AND u.isActive = :isActive")
    List<User> findByRoleNameAndIsActive(@Param("roleName") String roleName, @Param("isActive") Boolean isActive);

    long countByIsActive(Boolean isActive);

    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.name = :roleName")
    long countByRoleName(@Param("roleName") String roleName);

}
