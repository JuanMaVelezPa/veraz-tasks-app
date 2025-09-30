package com.veraz.tasks.backend.infrastructure.identity.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT DISTINCT u FROM UserEntity u " +
            "INNER JOIN FETCH u.userRoles ur " +
            "INNER JOIN FETCH ur.role r " +
            "WHERE u.username = :username OR u.email = :username")
    Optional<UserEntity> findByUsernameOrEmailWithRoles(@Param("username") String username);

    @Query("SELECT DISTINCT u FROM UserEntity u " +
            "LEFT JOIN FETCH u.userRoles ur " +
            "LEFT JOIN FETCH ur.role r " +
            "WHERE u.id = :id")
    Optional<UserEntity> findByIdWithRoles(@Param("id") UUID id);

    @Query("SELECT DISTINCT u FROM UserEntity u LEFT JOIN FETCH u.userRoles ur LEFT JOIN FETCH ur.role r WHERE " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<UserEntity> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT DISTINCT u FROM UserEntity u LEFT JOIN FETCH u.userRoles ur LEFT JOIN FETCH ur.role r")
    Page<UserEntity> findAllWithRoles(Pageable pageable);

    @Query("SELECT u FROM UserEntity u LEFT JOIN PersonEntity p ON u.id = p.userId WHERE p.userId IS NULL")
    Page<UserEntity> findAvailableUsers(Pageable pageable);

    @Query("SELECT u FROM UserEntity u LEFT JOIN PersonEntity p ON u.id = p.userId WHERE p.userId IS NULL AND " +
            "(LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<UserEntity> findAvailableUsersBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Modifying
    @Query(value = "DELETE FROM user_roles WHERE users_id = :userId", nativeQuery = true)
    int deleteUserRolesByUserIdNative(@Param("userId") UUID userId);
}
