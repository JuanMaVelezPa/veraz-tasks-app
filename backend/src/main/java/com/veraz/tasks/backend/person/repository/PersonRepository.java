package com.veraz.tasks.backend.person.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.veraz.tasks.backend.auth.model.User;
import com.veraz.tasks.backend.person.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {

    Optional<Person> findByIdentTypeAndIdentNumber(String identType, String identNumber);

    Optional<Person> findByUser(User user);
}
