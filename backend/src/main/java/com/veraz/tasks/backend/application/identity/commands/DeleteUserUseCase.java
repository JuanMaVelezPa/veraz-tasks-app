package com.veraz.tasks.backend.application.identity.commands;

import com.veraz.tasks.backend.domain.business.entities.Person;
import com.veraz.tasks.backend.domain.business.repositories.PersonRepository;
import com.veraz.tasks.backend.domain.identity.entities.User;
import com.veraz.tasks.backend.domain.identity.repositories.UserRepository;
import com.veraz.tasks.backend.domain.identity.valueobjects.UserId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class DeleteUserUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DeleteUserUseCase.class);

    private final UserRepository userRepository;
    private final PersonRepository personRepository;

    public DeleteUserUseCase(UserRepository userRepository, PersonRepository personRepository) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
    }

    public void execute(String userIdString) {
        logger.debug("Deleting user with ID: {}", userIdString);

        try {
            UserId userId = UserId.of(userIdString);

            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                throw new IllegalArgumentException("User not found with ID: " + userIdString);
            }

            Optional<Person> personOpt = personRepository.findByUserId(userId);
            if (personOpt.isPresent()) {
                throw new IllegalArgumentException("Cannot delete user. User is associated with person: " +
                        personOpt.get().getFirstName() + " " + personOpt.get().getLastName() +
                        ". Please disassociate the user from the person first.");
            }

            userRepository.deleteById(userId);

            logger.debug("Successfully deleted user with ID: {}", userIdString);

        } catch (Exception e) {
            logger.error("Error deleting user {}: {}", userIdString, e.getMessage(), e);
            throw e;
        }
    }
}
