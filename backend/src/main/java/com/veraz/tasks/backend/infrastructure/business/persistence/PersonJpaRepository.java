package com.veraz.tasks.backend.infrastructure.business.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonJpaRepository extends JpaRepository<PersonEntity, UUID> {

    Optional<PersonEntity> findByIdentNumber(String identNumber);

    Optional<PersonEntity> findByEmail(String email);

    Optional<PersonEntity> findByUserId(UUID userId);

    Optional<PersonEntity> findByIdentNumberAndIdentType(String identNumber, String identType);

    boolean existsByIdentNumber(String identNumber);

    boolean existsByEmail(String email);

    boolean existsByIdentNumberAndIdentType(String identNumber, String identType);

    List<PersonEntity> findByIsActiveTrue();

    List<PersonEntity> findByIsActiveFalse();

    List<PersonEntity> findByGender(String gender);

    List<PersonEntity> findByNationality(String nationality);

    List<PersonEntity> findByCity(String city);

    List<PersonEntity> findByCountry(String country);

    long countByIsActiveTrue();

    long countByIsActiveFalse();

    long countByGender(String gender);

    long countByNationality(String nationality);

    @Query("SELECT p FROM PersonEntity p WHERE " +
            "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.identNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.mobile) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<PersonEntity> findBySearchTerm(@Param("searchTerm") String searchTerm);

    @Query("SELECT p FROM PersonEntity p WHERE " +
            "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.identNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.mobile) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<PersonEntity> findBySearchTermWithPagination(@Param("searchTerm") String searchTerm, Pageable pageable);
}
