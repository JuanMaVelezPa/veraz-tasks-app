package com.veraz.tasks.backend.domain.business.repositories;

import com.veraz.tasks.backend.domain.business.entities.Client;
import com.veraz.tasks.backend.domain.business.valueobjects.ClientId;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;

import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Client repository interface
 * 
 * Defines the contract for client data access.
 * Part of the domain layer in Clean Architecture.
 * Implementation details are in the infrastructure layer.
 */
public interface ClientRepository {
    
    Client save(Client client);
    Optional<Client> findById(ClientId id);
    Optional<Client> findByPersonId(PersonId personId);
    Page<Client> findAll(PaginationRequestDTO paginationRequest);
    boolean existsByPersonId(PersonId personId);
    void delete(Client client);
    void deleteById(ClientId id);
    long count();
}

