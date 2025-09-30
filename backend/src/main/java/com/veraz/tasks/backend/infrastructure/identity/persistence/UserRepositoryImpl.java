package com.veraz.tasks.backend.infrastructure.identity.persistence;

import com.veraz.tasks.backend.domain.identity.entities.User;
import com.veraz.tasks.backend.domain.identity.repositories.UserRepository;
import com.veraz.tasks.backend.domain.identity.valueobjects.UserId;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;
import com.veraz.tasks.backend.shared.util.PaginationUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final RoleJpaRepository roleJpaRepository;

    public UserRepositoryImpl(UserJpaRepository jpaRepository, RoleJpaRepository roleJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.roleJpaRepository = roleJpaRepository;
    }

    @Override
    @Transactional
    public User save(User user) {

        Optional<UserEntity> existingEntityOpt = jpaRepository.findByIdWithRoles(user.getId().getValue());

        if (existingEntityOpt.isPresent()) {
            UserEntity existingEntity = existingEntityOpt.get();

            existingEntity.setUsername(user.getUsername());
            existingEntity.setPassword(user.getPassword());
            existingEntity.setEmail(user.getEmail() != null ? user.getEmail().getValue() : null);
            existingEntity.setIsActive(user.isActive());
            existingEntity.setUpdatedAt(user.getUpdatedAt());

            updateUserRoles(existingEntity, user.getUserRoles());

            UserEntity savedEntity = jpaRepository.save(existingEntity);
            return savedEntity.toDomain();
        } else {
            UserEntity entity = createNewUserEntity(user);
            UserEntity savedEntity = jpaRepository.save(entity);
            return savedEntity.toDomain();
        }
    }

    @Override
    @Transactional
    public User updateBasicFields(User user) {
        Optional<UserEntity> existingEntityOpt = jpaRepository.findByIdWithRoles(user.getId().getValue());

        if (existingEntityOpt.isPresent()) {
            UserEntity existingEntity = existingEntityOpt.get();

            existingEntity.setUsername(user.getUsername());
            existingEntity.setPassword(user.getPassword());
            existingEntity.setEmail(user.getEmail() != null ? user.getEmail().getValue() : null);
            existingEntity.setIsActive(user.isActive());
            existingEntity.setUpdatedAt(user.getUpdatedAt());

            UserEntity savedEntity = jpaRepository.save(existingEntity);
            return savedEntity.toDomain();
        } else {
            throw new IllegalArgumentException("User not found with ID: " + user.getId().getValue());
        }
    }

    private UserEntity createNewUserEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId().getValue());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setEmail(user.getEmail() != null ? user.getEmail().getValue() : null);
        entity.setIsActive(user.isActive());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        entity.setUserRoles(new java.util.HashSet<>());

        user.getUserRoles().forEach(userRole -> {
            Optional<RoleEntity> roleEntityOpt = roleJpaRepository.findById(userRole.getRoleId().getValue());
            if (roleEntityOpt.isPresent()) {
                UserRoleEntity userRoleEntity = UserRoleEntity.from(userRole, entity, roleEntityOpt.get());
                entity.getUserRoles().add(userRoleEntity);
            }
        });

        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UserId id) {
        return jpaRepository.findByIdWithRoles(id.getValue())
                .map(UserEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsernameOrEmailForAuthentication(String usernameOrEmail) {
        return jpaRepository.findByUsernameOrEmailWithRoles(usernameOrEmail)
                .map(UserEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByIdForAuthentication(UserId id) {
        return jpaRepository.findByIdWithRoles(id.getValue())
                .map(UserEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(PaginationRequestDTO paginationRequest) {
        var pageable = PaginationUtils.createPageable(paginationRequest);
        Page<UserEntity> userEntities;

        if (paginationRequest.hasSearch()) {
            userEntities = jpaRepository.findBySearchTerm(paginationRequest.search(), pageable);
        } else {
            userEntities = jpaRepository.findAllWithRoles(pageable);
        }

        userEntities.getContent().forEach(userEntity -> {
            if (userEntity.getUserRoles() != null) {
                userEntity.getUserRoles().size();
            }
        });

        return userEntities.map(UserEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAvailableUsers(PaginationRequestDTO paginationRequest) {
        var pageable = PaginationUtils.createPageable(paginationRequest);
        Page<UserEntity> userEntities;

        if (paginationRequest.hasSearch()) {
            userEntities = jpaRepository.findAvailableUsersBySearchTerm(paginationRequest.search(), pageable);
        } else {
            userEntities = jpaRepository.findAvailableUsers(pageable);
        }

        userEntities.getContent().forEach(userEntity -> {
            if (userEntity.getUserRoles() != null) {
                userEntity.getUserRoles().size();
            }
        });

        return userEntities.map(UserEntity::toDomain);
    }

    @Override
    @Transactional
    public void delete(User user) {
        UserEntity entity = UserEntity.from(user);
        jpaRepository.delete(entity);
    }

    @Override
    @Transactional
    public void deleteById(UserId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return jpaRepository.count();
    }

    private void updateUserRoles(UserEntity entity,
            List<com.veraz.tasks.backend.domain.identity.entities.UserRole> userRoles) {

        try {
            jpaRepository.deleteUserRolesByUserIdNative(entity.getId());
        } catch (Exception e) {
            // Continuar con el proceso aunque haya error en la eliminación
        }

        entity.getUserRoles().clear();

        if (userRoles != null && !userRoles.isEmpty()) {
            userRoles.forEach(userRole -> {
                roleJpaRepository.findById(userRole.getRoleId().getValue())
                        .ifPresent(roleEntity -> {
                            UserRoleEntity userRoleEntity = UserRoleEntity.from(userRole, entity, roleEntity);
                            entity.getUserRoles().add(userRoleEntity);
                        });
            });
        }
    }
}
