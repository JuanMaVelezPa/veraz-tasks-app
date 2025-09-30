package com.veraz.tasks.backend.infrastructure.business.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Employee JPA repository
 * 
 * Spring Data JPA repository for EmployeeEntity.
 * Part of the infrastructure layer in Clean Architecture.
 */
@Repository
public interface EmployeeJpaRepository extends JpaRepository<EmployeeEntity, UUID> {
    
    Optional<EmployeeEntity> findByPersonId(UUID personId);
    boolean existsByPersonId(UUID personId);
    
    @Query("SELECT e FROM EmployeeEntity e WHERE " +
           "LOWER(e.position) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.department) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.workEmail) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.workLocation) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.status) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<EmployeeEntity> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
}

