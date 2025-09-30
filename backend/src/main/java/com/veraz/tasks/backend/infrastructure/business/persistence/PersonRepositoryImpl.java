package com.veraz.tasks.backend.infrastructure.business.persistence;

import com.veraz.tasks.backend.domain.business.entities.Person;
import com.veraz.tasks.backend.domain.business.repositories.PersonRepository;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;
import com.veraz.tasks.backend.domain.identity.valueobjects.UserId;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;
import com.veraz.tasks.backend.shared.util.PaginationUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Person repository implementation using JPA
 */
@Repository
public class PersonRepositoryImpl implements PersonRepository {

    private final PersonJpaRepository jpaRepository;

    public PersonRepositoryImpl(PersonJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public Person save(Person person) {
        PersonEntity entity = PersonEntity.from(person);
        PersonEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Person> findById(PersonId id) {
        return jpaRepository.findById(id.getValue())
                .map(PersonEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Person> findByIdentNumber(String identNumber) {
        return jpaRepository.findByIdentNumber(identNumber)
                .map(PersonEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Person> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(PersonEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Person> findByUserId(UserId userId) {
        return jpaRepository.findByUserId(userId.getValue())
                .map(PersonEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> findAllActive() {
        return jpaRepository.findByIsActiveTrue()
                .stream()
                .map(PersonEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(PersonEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Person> findAll(PaginationRequestDTO paginationRequest) {
        var pageable = PaginationUtils.createPageable(paginationRequest);
        Page<PersonEntity> personEntities;

        if (paginationRequest.hasSearch()) {
            personEntities = jpaRepository.findBySearchTermWithPagination(paginationRequest.search(), pageable);
        } else {
            personEntities = jpaRepository.findAll(pageable);
        }

        return personEntities.map(PersonEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(PersonId id) {
        return jpaRepository.existsById(id.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByIdentNumber(String identNumber) {
        return jpaRepository.existsByIdentNumber(identNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void delete(Person person) {
        PersonEntity entity = PersonEntity.from(person);
        jpaRepository.delete(entity);
    }

    @Override
    @Transactional
    public void deleteById(PersonId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return jpaRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countActive() {
        return jpaRepository.countByIsActiveTrue();
    }
}
