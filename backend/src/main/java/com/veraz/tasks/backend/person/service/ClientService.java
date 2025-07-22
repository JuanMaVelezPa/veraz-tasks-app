package com.veraz.tasks.backend.person.service;

import com.veraz.tasks.backend.person.dto.ClientRequestDto;
import com.veraz.tasks.backend.person.dto.ClientResponseDto;
import com.veraz.tasks.backend.person.mapper.ClientMapper;
import com.veraz.tasks.backend.person.model.Client;
import com.veraz.tasks.backend.person.model.Person;
import com.veraz.tasks.backend.person.repository.ClientRepository;
import com.veraz.tasks.backend.person.repository.PersonRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository clientRepository;
    private final PersonRepository personRepository;
    private final MessageSource messageSource;

    public ClientService(ClientRepository clientRepository, PersonRepository personRepository,
            MessageSource messageSource) {
        this.clientRepository = clientRepository;
        this.personRepository = personRepository;
        this.messageSource = messageSource;
    }

    @Transactional(readOnly = true)
    public List<ClientResponseDto> getClients() {
        List<Client> clients = clientRepository.findAll();
        List<ClientResponseDto> clientResponseDtos = new ArrayList<>();
        for (Client client : clients) {
            clientResponseDtos.add(ClientResponseDto.builder()
                    .client(ClientMapper.toDto(client))
                    .message(null)
                    .status("OK")
                    .build());
        }
        return clientResponseDtos;
    }

    @Transactional(readOnly = true)
    public ClientResponseDto getClientById(UUID id) {
        Optional<Client> clientOpt = clientRepository.findById(id);
        if (clientOpt.isEmpty()) {
            return ClientResponseDto.builder()
                    .client(null)
                    .message(messageSource.getMessage("client.not.found", null, LocaleContextHolder.getLocale()))
                    .status("NOT_FOUND")
                    .build();
        }
        return ClientResponseDto.builder()
                .client(ClientMapper.toDto(clientOpt.get()))
                .message(null)
                .status("OK")
                .build();
    }

    @Transactional
    public ClientResponseDto createClient(ClientRequestDto dto) {
        try {
            Optional<Person> personOpt = personRepository.findById(dto.getPersonId());
            if (personOpt.isEmpty()) {
                return ClientResponseDto.builder()
                        .client(null)
                        .message(messageSource.getMessage("person.not.found", null, LocaleContextHolder.getLocale()))
                        .status("NOT_FOUND")
                        .build();
            }
            Client client = ClientMapper.toEntity(dto, personOpt.get());
            client = clientRepository.save(client);
            return ClientResponseDto.builder()
                    .client(ClientMapper.toDto(client))
                    .message(messageSource.getMessage("client.created.successfully", null,
                            LocaleContextHolder.getLocale()))
                    .status("CREATED")
                    .build();
        } catch (Exception e) {
            logger.error("Error creating client: " + e.getMessage());
            return ClientResponseDto.builder()
                    .client(null)
                    .message(messageSource.getMessage("client.error.creating", null, LocaleContextHolder.getLocale()))
                    .status("ERROR")
                    .build();
        }
    }

    @Transactional
    public ClientResponseDto updateClient(UUID id, ClientRequestDto dto) {
        try {
            Optional<Client> clientOpt = clientRepository.findById(id);
            if (clientOpt.isEmpty()) {
                return ClientResponseDto.builder()
                        .client(null)
                        .message(messageSource.getMessage("client.not.found", null, LocaleContextHolder.getLocale()))
                        .status("NOT_FOUND")
                        .build();
            }
            Optional<Person> personOpt = personRepository.findById(dto.getPersonId());
            if (personOpt.isEmpty()) {
                return ClientResponseDto.builder()
                        .client(null)
                        .message(messageSource.getMessage("person.not.found", null, LocaleContextHolder.getLocale()))
                        .status("NOT_FOUND")
                        .build();
            }
            Client client = clientOpt.get();
            // Actualizar campos
            client.setPerson(personOpt.get());
            client.setClientCode(dto.getClientCode());
            client.setType(dto.getType());
            client.setCategory(dto.getCategory());
            client.setSource(dto.getSource());
            client.setCompanyName(dto.getCompanyName());
            client.setCompanyWebsite(dto.getCompanyWebsite());
            client.setCompanyIndustry(dto.getCompanyIndustry());
            client.setContactPerson(dto.getContactPerson());
            client.setContactPosition(dto.getContactPosition());
            client.setAddress(dto.getAddress());
            client.setCity(dto.getCity());
            client.setCountry(dto.getCountry());
            client.setPostalCode(dto.getPostalCode());
            client.setTaxId(dto.getTaxId());
            client.setCreditLimit(dto.getCreditLimit());
            client.setCurrency(dto.getCurrency());
            client.setPaymentTerms(dto.getPaymentTerms());
            client.setPaymentMethod(dto.getPaymentMethod());
            client.setNotes(dto.getNotes());
            client.setPreferences(dto.getPreferences());
            client.setTags(dto.getTags());
            client.setRating(dto.getRating());
            client.setStatus(dto.getStatus());
            client = clientRepository.save(client);
            return ClientResponseDto.builder()
                    .client(ClientMapper.toDto(client))
                    .message(messageSource.getMessage("client.updated.successfully", null,
                            LocaleContextHolder.getLocale()))
                    .status("UPDATED")
                    .build();
        } catch (Exception e) {
            logger.error("Error updating client: " + e.getMessage());
            return ClientResponseDto.builder()
                    .client(null)
                    .message(messageSource.getMessage("client.error.updating", null, LocaleContextHolder.getLocale()))
                    .status("ERROR")
                    .build();
        }
    }

    @Transactional
    public ClientResponseDto deleteClient(UUID id) {
        try {
            Optional<Client> clientOpt = clientRepository.findById(id);
            if (clientOpt.isEmpty()) {
                return ClientResponseDto.builder()
                        .client(null)
                        .message(messageSource.getMessage("client.not.found", null, LocaleContextHolder.getLocale()))
                        .status("NOT_FOUND")
                        .build();
            }
            clientRepository.delete(clientOpt.get());
            return ClientResponseDto.builder()
                    .client(null)
                    .message(messageSource.getMessage("client.deleted.successfully", null,
                            LocaleContextHolder.getLocale()))
                    .status("DELETED")
                    .build();
        } catch (Exception e) {
            logger.error("Error deleting client: " + e.getMessage());
            return ClientResponseDto.builder()
                    .client(null)
                    .message(messageSource.getMessage("client.error.deleting", null, LocaleContextHolder.getLocale()))
                    .status("ERROR")
                    .build();
        }
    }
}