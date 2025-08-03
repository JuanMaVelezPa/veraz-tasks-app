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

import com.veraz.tasks.backend.auth.model.User;
import com.veraz.tasks.backend.person.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {

    Optional<Person> findByUser(User user);

    Optional<Person> findByEmail(String email);

    Optional<Person> findByIdentNumber(String identNumber);

    Optional<Person> findByIdentNumberAndIdentType(String identNumber, String identType);

    @Query("SELECT p FROM Person p WHERE " +
           "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.identNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.mobile) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Person> findByFirstNameOrLastNameOrEmailOrIdentNumberOrMobileContainingIgnoreCase(
            @Param("searchTerm") String searchTerm, Pageable pageable);

    // Additional useful methods
    boolean existsByEmail(String email);

    boolean existsByIdentNumber(String identNumber);

    boolean existsByIdentNumberAndIdentType(String identNumber, String identType);

    List<Person> findByIsActive(Boolean isActive);

    List<Person> findByGender(String gender);

    List<Person> findByNationality(String nationality);

    List<Person> findByCity(String city);

    List<Person> findByCountry(String country);

    @Query("SELECT p FROM Person p WHERE p.birthDate BETWEEN :startDate AND :endDate")
    List<Person> findByBirthDateBetween(@Param("startDate") java.time.LocalDate startDate, 
                                       @Param("endDate") java.time.LocalDate endDate);

    long countByIsActive(Boolean isActive);

    long countByGender(String gender);

    long countByNationality(String nationality);

}
