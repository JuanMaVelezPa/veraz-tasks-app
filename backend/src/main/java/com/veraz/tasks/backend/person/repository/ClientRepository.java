package com.veraz.tasks.backend.person.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.veraz.tasks.backend.person.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    Optional<Client> findByClientCode(String clientCode);

    Optional<Client> findByPersonId(UUID personId);

    @Query("SELECT c FROM Client c JOIN c.person p WHERE " +
           "LOWER(c.clientCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.companyName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.contactPerson) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Client> findByClientCodeOrCompanyNameOrContactPersonOrPersonNameContainingIgnoreCase(
            @Param("searchTerm") String searchTerm, Pageable pageable);

    // Additional useful methods
    boolean existsByClientCode(String clientCode);

    boolean existsByPersonId(UUID personId);

    List<Client> findByIsActive(Boolean isActive);

    List<Client> findByType(String type);

    List<Client> findByCategory(String category);

    List<Client> findByStatus(String status);

    List<Client> findByCity(String city);

    List<Client> findByCountry(String country);

    @Query("SELECT c FROM Client c WHERE c.rating >= :minRating")
    List<Client> findByRatingGreaterThanOrEqualTo(@Param("minRating") Integer minRating);

    @Query("SELECT c FROM Client c WHERE c.creditLimit >= :minCreditLimit")
    List<Client> findByCreditLimitGreaterThanOrEqualTo(@Param("minCreditLimit") java.math.BigDecimal minCreditLimit);

    long countByIsActive(Boolean isActive);

    long countByType(String type);

    long countByStatus(String status);

    long countByCategory(String category);

}
