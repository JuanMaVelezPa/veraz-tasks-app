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

import com.veraz.tasks.backend.person.dto.ClientRequestDTO;
import com.veraz.tasks.backend.person.dto.ClientResponseDTO;
import com.veraz.tasks.backend.person.mapper.ClientMapper;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO.PaginationInfo;
import com.veraz.tasks.backend.shared.service.ServiceInterface;
import com.veraz.tasks.backend.person.model.Client;
import com.veraz.tasks.backend.person.model.Person;
import com.veraz.tasks.backend.person.repository.ClientRepository;
import com.veraz.tasks.backend.person.repository.PersonRepository;
import com.veraz.tasks.backend.exception.DataConflictException;
import com.veraz.tasks.backend.exception.ResourceNotFoundException;
import com.veraz.tasks.backend.shared.util.MessageUtils;

@Service
public class ClientService implements ServiceInterface<Client, UUID, ClientRequestDTO, ClientResponseDTO> {

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

        List<ClientResponseDTO> clientDtos = clientPage.getContent().stream()
                .map(ClientMapper::toDto)
                .collect(Collectors.toList());

        PaginationInfo paginationInfo = PaginationInfo
                .builder()
                .currentPage(clientPage.getNumber())
                .totalPages(clientPage.getTotalPages())
                .totalElements(clientPage.getTotalElements())
                .pageSize(clientPage.getSize())
                .hasNext(clientPage.hasNext())
                .hasPrevious(clientPage.hasPrevious())
                .isFirst(clientPage.isFirst())
                .isLast(clientPage.isLast())
                .build();

        return PaginatedResponseDTO.<ClientResponseDTO>builder()
                .data(clientDtos)
                .pagination(paginationInfo)
                .build();
    }

    @Transactional(readOnly = true)
    public Optional<ClientResponseDTO> findById(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("Client")));
        
        return Optional.of(ClientMapper.toDto(client));
    }

    @Transactional(readOnly = true)
    public ClientResponseDTO findByClientCode(String clientCode) {
        Client client = clientRepository.findByClientCode(clientCode)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("Client")));
        
        return ClientMapper.toDto(client);
    }

    @Transactional(readOnly = true)
    public ClientResponseDTO findByPersonId(UUID personId) {
        Client client = clientRepository.findByPersonId(personId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("Client")));
        
        return ClientMapper.toDto(client);
    }

    @Transactional
    public ClientResponseDTO create(ClientRequestDTO clientRequest) {
        // Check if client already exists with same client code
        if (clientRepository.existsByClientCode(clientRequest.getClientCode())) {
            throw new DataConflictException(MessageUtils.getEntityAlreadyExists("Client"));
        }

        // Check if person already has a client record
        if (clientRepository.existsByPersonId(clientRequest.getPersonId())) {
            throw new DataConflictException(MessageUtils.getEntityAlreadyExists("Client"));
        }

        // Get the person
        Person person = personRepository.findById(clientRequest.getPersonId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("Person")));

        Client newClient = ClientMapper.toEntity(clientRequest, person);
        clientRepository.save(newClient);

        logger.info("Client created successfully: {} with createdAt: {} and updatedAt: {}",
                newClient.getClientCode(), newClient.getCreatedAt(), newClient.getUpdatedAt());

        return ClientMapper.toDto(newClient);
    }

    @Transactional
    public ClientResponseDTO update(UUID id, ClientRequestDTO clientRequestDTO) {
        Client clientToUpdate = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("Client")));

        // Update client code if provided
        if (clientRequestDTO.getClientCode() != null && !clientRequestDTO.getClientCode().trim().isEmpty()) {
            String newClientCode = clientRequestDTO.getClientCode().trim();
            
            if (!newClientCode.equalsIgnoreCase(clientToUpdate.getClientCode())) {
                if (clientRepository.existsByClientCode(newClientCode)) {
                    throw new DataConflictException(MessageUtils.getEntityAlreadyExists("Client"));
                }
            }
        }

        // Update client using mapper
        ClientMapper.updateEntity(clientToUpdate, clientRequestDTO);
        clientToUpdate.setUpdatedAt(LocalDateTime.now());
        clientRepository.save(clientToUpdate);

        logger.info("Client updated successfully with ID: {}", clientToUpdate.getId());

        return ClientMapper.toDto(clientToUpdate);
    }

    @Transactional
    public void deleteById(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("Client")));
        
        clientRepository.delete(client);
        logger.info("Client deleted successfully with ID: {}", id);
    }

    // Additional useful methods
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

}