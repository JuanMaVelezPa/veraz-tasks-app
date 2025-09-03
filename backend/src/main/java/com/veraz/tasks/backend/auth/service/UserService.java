package com.veraz.tasks.backend.auth.service;

import java.time.LocalDateTime;
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
import com.veraz.tasks.backend.auth.dto.UserCreateRequestDTO;
import com.veraz.tasks.backend.auth.dto.UserUpdateRequestDTO;
import com.veraz.tasks.backend.auth.mapper.UserMapper;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO;
import com.veraz.tasks.backend.shared.service.ServiceInterface;
import com.veraz.tasks.backend.auth.model.Role;
import com.veraz.tasks.backend.auth.model.User;
import com.veraz.tasks.backend.auth.repository.RoleRepository;
import com.veraz.tasks.backend.auth.repository.UserRepository;
import com.veraz.tasks.backend.person.repository.PersonRepository;
import com.veraz.tasks.backend.exception.DataConflictException;
import com.veraz.tasks.backend.exception.ResourceNotFoundException;
import com.veraz.tasks.backend.shared.constants.MessageKeys;
import com.veraz.tasks.backend.shared.util.MessageUtils;
import com.veraz.tasks.backend.shared.util.PaginationUtils;

@Service
public class UserService implements UserDetailsService,
        ServiceInterface<User, UUID, UserCreateRequestDTO, UserUpdateRequestDTO, UserResponseDTO> {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;

    public UserService(UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            PersonRepository personRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public PaginatedResponseDTO<UserResponseDTO> findAll(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return PaginationUtils.toPaginatedResponse(userPage, UserMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponseDTO> findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFoundMessage("User")));

        // Force lazy loading of roles
        user.getRoles().size();
        return Optional.of(UserMapper.toDto(user));
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findByUsernameOrEmail(String usernameOrEmail) {
        User user = userRepository.findByUsernameOrEmailAllIgnoreCase(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFoundMessage("User")));

        return UserMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public PaginatedResponseDTO<UserResponseDTO> findUsersWithoutPerson(Pageable pageable) {
        Page<User> userPage = userRepository.findUsersWithoutPerson(pageable);
        return PaginationUtils.toPaginatedResponse(userPage, UserMapper::toDto);
    }

    @Transactional(readOnly = true)
    public PaginatedResponseDTO<UserResponseDTO> findUsersWithoutPersonBySearch(String searchQuery, Pageable pageable) {
        Page<User> userPage = userRepository.findUsersWithoutPersonBySearch(searchQuery, pageable);
        return PaginationUtils.toPaginatedResponse(userPage, UserMapper::toDto);
    }

    @Transactional(readOnly = true)
    public PaginatedResponseDTO<UserResponseDTO> findBySearch(String searchQuery, Pageable pageable) {
        Page<User> userPage = userRepository.findByUsernameOrEmailOrRolesNameContainingIgnoreCase(searchQuery,
                pageable);
        return PaginationUtils.toPaginatedResponse(userPage, UserMapper::toDto);
    }

    @Transactional
    public UserResponseDTO create(UserCreateRequestDTO userRequestDTO) {
        // Validate username uniqueness
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new DataConflictException(MessageUtils.getEntityAlreadyExistsMessage("User"));
        }

        // Validate email uniqueness
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new DataConflictException(MessageUtils.getEntityAlreadyExistsMessage("User"));
        }

        // Validate roles exist
        Set<Role> roles = userRequestDTO.getRoles().stream()
                .map(roleName -> {
                    Role role = roleRepository.findByName(roleName);
                    if (role == null) {
                        throw new ResourceNotFoundException(MessageUtils.getEntityNotFoundMessage("Role"));
                    }
                    return role;
                })
                .collect(Collectors.toSet());

        String encodedPassword = passwordEncoder.encode(userRequestDTO.getPassword());
        Role defaultRole = roles.iterator().next(); // Use first role as default
        User newUser = UserMapper.toEntity(userRequestDTO, encodedPassword, defaultRole);
        newUser.setRoles(roles);
        userRepository.save(newUser);

        logger.info("User created successfully: {}", newUser.getUsername());

        return UserMapper.toDto(newUser);
    }

    @Transactional
    public UserResponseDTO update(UUID id, UserUpdateRequestDTO userRequestDTO) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFoundMessage("User")));

        // Update username if provided
        if (userRequestDTO.getUsername() != null && !userRequestDTO.getUsername().trim().isEmpty()) {
            String newUsername = userRequestDTO.getUsername().trim();
            if (!newUsername.equals(userToUpdate.getUsername())) {
                if (userRepository.existsByUsername(newUsername)) {
                    throw new DataConflictException(MessageUtils.getEntityAlreadyExistsMessage("User"));
                }
                userToUpdate.setUsername(newUsername);
            }
        }

        // Update email if provided
        if (userRequestDTO.getEmail() != null && !userRequestDTO.getEmail().trim().isEmpty()) {
            String newEmail = userRequestDTO.getEmail().trim();
            if (!newEmail.equalsIgnoreCase(userToUpdate.getEmail())) {
                if (userRepository.existsByEmail(newEmail)) {
                    throw new DataConflictException(MessageUtils.getEntityAlreadyExistsMessage("User"));
                }
                userToUpdate.setEmail(newEmail);
            }
        }

        // Update password if provided
        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().trim().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(userRequestDTO.getPassword().trim());
            userToUpdate.setPassword(encodedPassword);
        }

        // Update active status if provided
        if (userRequestDTO.getIsActive() != null) {
            userToUpdate.setIsActive(userRequestDTO.getIsActive());
        }

        // Update roles if provided
        if (userRequestDTO.getRoles() != null && !userRequestDTO.getRoles().isEmpty()) {
            Set<Role> newRoles = userRequestDTO.getRoles().stream()
                    .map(roleName -> {
                        Role role = roleRepository.findByName(roleName);
                        if (role == null) {
                            throw new ResourceNotFoundException(MessageUtils.getEntityNotFoundMessage("Role"));
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
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.getEntityNotFoundMessage("User")));

        if (personRepository.findByUser(user).isPresent()) {
            throw new DataConflictException(MessageUtils.getMessage(MessageKeys.BUSINESS_USER_HAS_PERSON_DELETE));
        }

        userRepository.delete(user);
        logger.info("User deleted successfully with ID: {}", id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameOrEmailAllIgnoreCase(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + username));
    }
}