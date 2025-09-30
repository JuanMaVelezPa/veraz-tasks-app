package com.veraz.tasks.backend.application.business.queries;

import com.veraz.tasks.backend.application.business.dto.ClientResponse;
import com.veraz.tasks.backend.domain.business.entities.Client;
import com.veraz.tasks.backend.domain.business.repositories.ClientRepository;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;
import com.veraz.tasks.backend.shared.util.PaginationUtils;

import org.springframework.data.domain.Page;

public class FindAllClientsUseCase {
    
    private final ClientRepository clientRepository;
    
    public FindAllClientsUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    
    public PaginatedResponseDTO<ClientResponse> execute(PaginationRequestDTO paginationRequest) {
        Page<Client> clientsPage = clientRepository.findAll(paginationRequest);
        return PaginationUtils.toPaginatedResponse(clientsPage, ClientResponse::from);
    }
}

