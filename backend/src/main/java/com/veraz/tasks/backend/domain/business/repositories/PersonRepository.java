package com.veraz.tasks.backend.domain.business.repositories;

import com.veraz.tasks.backend.domain.business.entities.Person;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;
import com.veraz.tasks.backend.domain.identity.valueobjects.UserId;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * Person repository interface
 * 
 * Defines the contract for person data access.
 * Part of the domain layer in Clean Architecture.
 * Implementation details are in the infrastructure layer.
 */
public interface PersonRepository {
    
    Person save(Person person);
    Optional<Person> findById(PersonId id);
    Optional<Person> findByIdentNumber(String identNumber);
    Optional<Person> findByEmail(String email);
    Optional<Person> findByUserId(UserId userId);
    List<Person> findAllActive();
    List<Person> findAll();
    Page<Person> findAll(PaginationRequestDTO paginationRequest);
    boolean existsById(PersonId id);
    boolean existsByIdentNumber(String identNumber);
    boolean existsByEmail(String email);
    void delete(Person person);
    void deleteById(PersonId id);
    long count();
    long countActive();
}

