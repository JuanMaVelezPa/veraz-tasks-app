package com.veraz.tasks.backend.infrastructure.business.persistence;

import com.veraz.tasks.backend.domain.business.entities.Client;
import com.veraz.tasks.backend.domain.business.repositories.ClientRepository;
import com.veraz.tasks.backend.domain.business.valueobjects.ClientId;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;
import com.veraz.tasks.backend.shared.util.PaginationUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Client repository implementation using JPA
 */
@Repository
public class ClientRepositoryImpl implements ClientRepository {
    
    private final ClientJpaRepository jpaRepository;
    
    public ClientRepositoryImpl(ClientJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    @Transactional
    public Client save(Client client) {
        ClientEntity entity = ClientEntity.from(client);
        ClientEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findById(ClientId id) {
        return jpaRepository.findById(id.getValue())
                .map(ClientEntity::toDomain);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findByPersonId(PersonId personId) {
        return jpaRepository.findByPersonId(personId.getValue())
                .map(ClientEntity::toDomain);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Client> findAll(PaginationRequestDTO paginationRequest) {
        var pageable = PaginationUtils.createPageable(paginationRequest);
        Page<ClientEntity> clientEntities;
        
        if (paginationRequest.hasSearch()) {
            clientEntities = jpaRepository.findBySearchTerm(paginationRequest.search(), pageable);
        } else {
            clientEntities = jpaRepository.findAll(pageable);
        }
        
        return clientEntities.map(ClientEntity::toDomain);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByPersonId(PersonId personId) {
        return jpaRepository.existsByPersonId(personId.getValue());
    }
    
    @Override
    @Transactional
    public void delete(Client client) {
        ClientEntity entity = ClientEntity.from(client);
        jpaRepository.delete(entity);
    }
    
    @Override
    @Transactional
    public void deleteById(ClientId id) {
        jpaRepository.deleteById(id.getValue());
    }
    
    @Override
    @Transactional(readOnly = true)
    public long count() {
        return jpaRepository.count();
    }
}

