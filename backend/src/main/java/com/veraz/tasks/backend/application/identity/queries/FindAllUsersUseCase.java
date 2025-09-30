package com.veraz.tasks.backend.application.identity.queries;

import com.veraz.tasks.backend.application.business.dto.UserResponse;
import com.veraz.tasks.backend.application.shared.services.UserResponseMapper;
import com.veraz.tasks.backend.domain.identity.entities.User;
import com.veraz.tasks.backend.domain.identity.repositories.UserRepository;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;
import com.veraz.tasks.backend.shared.util.PaginationUtils;

import org.springframework.data.domain.Page;

public class FindAllUsersUseCase {

    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;

    public FindAllUsersUseCase(UserRepository userRepository, UserResponseMapper userResponseMapper) {
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
    }

    public PaginatedResponseDTO<UserResponse> execute(PaginationRequestDTO paginationRequest) {
        Page<User> usersPage = userRepository.findAll(paginationRequest);
        return PaginationUtils.toPaginatedResponse(usersPage, userResponseMapper::mapToResponse);
    }
}
