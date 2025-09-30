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
 * Client JPA repository
 * 
 * Spring Data JPA repository for ClientEntity.
 * Part of the infrastructure layer in Clean Architecture.
 */
@Repository
public interface ClientJpaRepository extends JpaRepository<ClientEntity, UUID> {
    
    Optional<ClientEntity> findByPersonId(UUID personId);
    boolean existsByPersonId(UUID personId);
    
    @Query("SELECT c FROM ClientEntity c WHERE " +
           "LOWER(c.companyName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.contactPerson) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.companyIndustry) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.taxId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.status) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<ClientEntity> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
}

