package com.veraz.tasks.backend.auth.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veraz.tasks.backend.auth.dto.UserResponseDTO;
import com.veraz.tasks.backend.auth.dto.UserRequestDTO;
import com.veraz.tasks.backend.auth.mapper.UserMapper;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO.PaginationInfo;
import com.veraz.tasks.backend.shared.service.ServiceInterface;
import com.veraz.tasks.backend.auth.model.Role;
import com.veraz.tasks.backend.auth.model.User;
import com.veraz.tasks.backend.auth.repository.RoleRepository;
import com.veraz.tasks.backend.auth.repository.UserRepository;
import com.veraz.tasks.backend.exception.DataConflictException;
import com.veraz.tasks.backend.exception.ResourceNotFoundException;
import com.veraz.tasks.backend.shared.util.MessageUtils;

@Service
public class UserService implements UserDetailsService, ServiceInterface<User, UUID, UserRequestDTO, UserResponseDTO> {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public PaginatedResponseDTO<UserResponseDTO> findAll(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponseDTO> userDtos = userPage.getContent().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());

        PaginationInfo paginationInfo = PaginationInfo
                .builder()
                .currentPage(userPage.getNumber())
                .totalPages(userPage.getTotalPages())
                .totalElements(userPage.getTotalElements())
                .pageSize(userPage.getSize())
                .hasNext(userPage.hasNext())
                .hasPrevious(userPage.hasPrevious())
                .isFirst(userPage.isFirst())
                .isLast(userPage.isLast())
                .build();

        return PaginatedResponseDTO.<UserResponseDTO>builder()
                .data(userDtos)
                .pagination(paginationInfo)
                .build();
    }

    @Transactional(readOnly = true)
    public Optional<UserResponseDTO> findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("User")));
        
        // Force lazy loading of roles
        user.getRoles().size();
        return Optional.of(UserMapper.toDto(user));
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findByEmailOrUsername(String query) {
        User user = userRepository.findByUsernameOrEmailAllIgnoreCase(query, query)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("User")));
        
        return UserMapper.toDto(user);
    }

    @Transactional
    public UserResponseDTO create(UserRequestDTO userRequest) {
        // Check if user already exists
        if (userRepository.findByUsernameOrEmailAllIgnoreCase(userRequest.getUsername(), userRequest.getEmail()).isPresent()) {
            throw new DataConflictException(MessageUtils.getEntityAlreadyExists("User"));
        }

        Role defaultRole = roleRepository.findByName("USER");
        if (defaultRole == null) {
            throw new ResourceNotFoundException(MessageUtils.getEntityNotFound("Role"));
        }

        String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
        User newUser = UserMapper.toEntity(userRequest, encodedPassword, defaultRole);
        userRepository.save(newUser);

        logger.info("User created successfully: {} with createdAt: {} and updatedAt: {}",
                newUser.getUsername(), newUser.getCreatedAt(), newUser.getUpdatedAt());

        return UserMapper.toDto(newUser);
    }

    @Transactional
    public UserResponseDTO update(UUID id, UserRequestDTO userRequestDTO) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("User")));

        // Update username if provided
        if (userRequestDTO.getUsername() != null && !userRequestDTO.getUsername().trim().isEmpty()) {
            String newUsername = userRequestDTO.getUsername().trim();

            if (!newUsername.equalsIgnoreCase(userToUpdate.getUsername())) {
                if (userRepository.findByUsername(newUsername).isPresent()) {
                    throw new DataConflictException(MessageUtils.getEntityAlreadyExists("User"));
                }
            }
            userToUpdate.setUsername(newUsername.toLowerCase());
        }

        // Update email if provided
        if (userRequestDTO.getEmail() != null && !userRequestDTO.getEmail().trim().isEmpty()) {
            String newEmail = userRequestDTO.getEmail().trim();

            if (!newEmail.equalsIgnoreCase(userToUpdate.getEmail())) {
                if (userRepository.findByEmail(newEmail).isPresent()) {
                    throw new DataConflictException(MessageUtils.getEntityAlreadyExists("User"));
                }
            }
            userToUpdate.setEmail(newEmail.toLowerCase());
        }

        // Update password if provided
        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().trim().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(userRequestDTO.getPassword());
            userToUpdate.setPassword(encodedPassword);
        }

        // Update active status if provided
        if (userRequestDTO.getIsActive() != null) {
            userToUpdate.setIsActive(userRequestDTO.getIsActive());
        }

        // Update roles if provided
        if (userRequestDTO.getRoles() != null) {
            Set<Role> newRoles = userRequestDTO.getRoles().stream()
                    .map(roleName -> {
                        Role role = roleRepository.findByName(roleName);
                        if (role == null) {
                            throw new ResourceNotFoundException(MessageUtils.getEntityNotFound("Role"));
                        }
                        return role;
                    })
                    .collect(Collectors.toSet());
            userToUpdate.setRoles(newRoles);
        }

        userToUpdate.setUpdatedAt(LocalDateTime.now());
        userRepository.save(userToUpdate);

        logger.info("User updated successfully with ID: {}", userToUpdate.getId());

        return UserMapper.toDto(userToUpdate);
    }

    @Transactional
    public void deleteById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFound("User")));
        
        userRepository.delete(user);
        logger.info("User deleted successfully with ID: {}", id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameOrEmailAllIgnoreCase(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + username));
    }

}