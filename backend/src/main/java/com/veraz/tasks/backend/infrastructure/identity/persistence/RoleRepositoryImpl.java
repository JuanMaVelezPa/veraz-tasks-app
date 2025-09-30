package com.veraz.tasks.backend.infrastructure.identity.persistence;

import com.veraz.tasks.backend.domain.identity.entities.Role;
import com.veraz.tasks.backend.domain.identity.repositories.RoleRepository;
import com.veraz.tasks.backend.domain.identity.valueobjects.RoleId;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository jpaRepository;

    public RoleRepositoryImpl(RoleJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public Role save(Role role) {
        RoleEntity entity = RoleEntity.from(role);
        RoleEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findById(RoleId id) {
        return jpaRepository.findById(id.getValue())
                .map(RoleEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findByName(String name) {
        return jpaRepository.findByName(name)
                .map(RoleEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return jpaRepository.findAll().stream()
                .map(RoleEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAllActive() {
        return jpaRepository.findByIsActiveTrue().stream()
                .map(RoleEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    @Transactional
    public void delete(Role role) {
        RoleEntity entity = RoleEntity.from(role);
        jpaRepository.delete(entity);
    }

    @Override
    @Transactional
    public void deleteById(RoleId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return jpaRepository.count();
    }
}
