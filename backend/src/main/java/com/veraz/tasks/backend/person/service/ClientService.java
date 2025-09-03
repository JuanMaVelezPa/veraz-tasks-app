package com.veraz.tasks.backend.person.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veraz.tasks.backend.person.dto.ClientCreateRequestDTO;
import com.veraz.tasks.backend.person.dto.ClientUpdateRequestDTO;
import com.veraz.tasks.backend.person.dto.ClientResponseDTO;
import com.veraz.tasks.backend.person.mapper.ClientMapper;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.service.ServiceInterface;
import com.veraz.tasks.backend.person.model.Client;
import com.veraz.tasks.backend.person.model.Person;
import com.veraz.tasks.backend.person.repository.ClientRepository;
import com.veraz.tasks.backend.person.repository.PersonRepository;
import com.veraz.tasks.backend.exception.DataConflictException;
import com.veraz.tasks.backend.exception.ResourceNotFoundException;
import com.veraz.tasks.backend.shared.util.MessageUtils;
import com.veraz.tasks.backend.shared.util.PaginationUtils;

@Service
public class ClientService
        implements ServiceInterface<Client, UUID, ClientCreateRequestDTO, ClientUpdateRequestDTO, ClientResponseDTO> {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;
    private final PersonRepository personRepository;

    public ClientService(ClientRepository clientRepository, PersonRepository personRepository) {
        this.clientRepository = clientRepository;
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public PaginatedResponseDTO<ClientResponseDTO> findAll(Pageable pageable) {
        Page<Client> clientPage = clientRepository.findAll(pageable);
        return PaginationUtils.toPaginatedResponse(clientPage, ClientMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<ClientResponseDTO> findById(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFoundMessage("Client")));

        return Optional.of(ClientMapper.toDto(client));
    }

    @Transactional(readOnly = true)
    public ClientResponseDTO findByClientCode(String clientCode) {
        Client client = clientRepository.findByClientCode(clientCode)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFoundMessage("Client")));

        return ClientMapper.toDto(client);
    }

    @Transactional(readOnly = true)
    public ClientResponseDTO findByPersonId(UUID personId) {
        personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFoundMessage("Person")));

        Client client = clientRepository.findByPersonId(personId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFoundMessage("Client")));

        return ClientMapper.toDto(client);
    }

    @Transactional
    public ClientResponseDTO create(ClientCreateRequestDTO createRequest) {
        validateClientCodeUniqueness(createRequest);
        validatePersonUniqueness(createRequest);

        Person person = personRepository.findById(createRequest.getPersonId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFoundMessage("Person")));

        Client newClient = ClientMapper.toEntity(createRequest, person);
        clientRepository.save(newClient);

        logger.info("Client created successfully with ID: {}", newClient.getId());

        return ClientMapper.toDto(newClient);
    }

    @Transactional
    public ClientResponseDTO update(UUID id, ClientUpdateRequestDTO updateRequest) {
        Client clientToUpdate = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFoundMessage("Client")));

        validateClientCodeUniquenessForUpdate(updateRequest, clientToUpdate);

        ClientMapper.updateEntity(clientToUpdate, updateRequest);
        clientToUpdate.setUpdatedAt(LocalDateTime.now());
        clientRepository.save(clientToUpdate);

        logger.info("Client updated successfully with ID: {}", clientToUpdate.getId());

        return ClientMapper.toDto(clientToUpdate);
    }

    @Transactional
    public void deleteById(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFoundMessage("Client")));

        clientRepository.delete(client);
        logger.info("Client deleted successfully with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public List<ClientResponseDTO> findByIsActive(Boolean isActive) {
        return clientRepository.findByIsActive(isActive).stream()
                .map(ClientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClientResponseDTO> findByType(String type) {
        return clientRepository.findByType(type).stream()
                .map(ClientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClientResponseDTO> findByCategory(String category) {
        return clientRepository.findByCategory(category).stream()
                .map(ClientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClientResponseDTO> findByStatus(String status) {
        return clientRepository.findByStatus(status).stream()
                .map(ClientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClientResponseDTO> findByCity(String city) {
        return clientRepository.findByCity(city).stream()
                .map(ClientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClientResponseDTO> findByCountry(String country) {
        return clientRepository.findByCountry(country).stream()
                .map(ClientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClientResponseDTO> findByRatingGreaterThanOrEqualTo(Integer minRating) {
        return clientRepository.findByRatingGreaterThanOrEqualTo(minRating).stream()
                .map(ClientMapper::toDto)
                .collect(Collectors.toList());
    }

    private void validateClientCodeUniqueness(ClientCreateRequestDTO createRequest) {
        if (createRequest.getClientCode() != null && !createRequest.getClientCode().trim().isEmpty()) {
            if (clientRepository.existsByClientCode(createRequest.getClientCode().trim())) {
                throw new DataConflictException(MessageUtils.getEntityAlreadyExistsMessage("Client"));
            }
        }
    }

    private void validatePersonUniqueness(ClientCreateRequestDTO createRequest) {
        if (clientRepository.existsByPersonId(createRequest.getPersonId())) {
            throw new DataConflictException(MessageUtils.getEntityAlreadyExistsMessage("Client"));
        }
    }

    private void validateClientCodeUniquenessForUpdate(ClientUpdateRequestDTO updateRequest, Client existingClient) {
        if (updateRequest.getClientCode() != null && !updateRequest.getClientCode().trim().isEmpty()) {
            String newClientCode = updateRequest.getClientCode().trim();

            if (!newClientCode.equalsIgnoreCase(existingClient.getClientCode())) {
                if (clientRepository.existsByClientCode(newClientCode)) {
                    throw new DataConflictException(MessageUtils.getEntityAlreadyExistsMessage("Client"));
                }
            }
        }
    }
}