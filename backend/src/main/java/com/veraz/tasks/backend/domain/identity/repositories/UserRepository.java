package com.veraz.tasks.backend.domain.identity.repositories;

import com.veraz.tasks.backend.domain.identity.entities.User;
import com.veraz.tasks.backend.domain.identity.valueobjects.UserId;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;

import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserRepository {

    User save(User user);
    
    User updateBasicFields(User user);

    Optional<User> findById(UserId id);

    Optional<User> findByUsernameOrEmailForAuthentication(String usernameOrEmail);

    Optional<User> findByIdForAuthentication(UserId id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Page<User> findAll(PaginationRequestDTO paginationRequest);

    Page<User> findAvailableUsers(PaginationRequestDTO paginationRequest);

    void delete(User user);

    void deleteById(UserId id);

    long count();
}
