package com.veraz.tasks.backend.auth.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.veraz.tasks.backend.auth.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User findByEmail(String email);

    User findByUsername(String username);

    User findByUsernameOrEmailAllIgnoreCase(String username, String email);

}
