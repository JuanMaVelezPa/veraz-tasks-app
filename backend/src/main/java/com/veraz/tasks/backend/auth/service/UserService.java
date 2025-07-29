package com.veraz.tasks.backend.auth.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veraz.tasks.backend.auth.dto.UserDetailDto;
import com.veraz.tasks.backend.auth.dto.UserRequestDTO;
import com.veraz.tasks.backend.auth.dto.UserResponseDTO;
import com.veraz.tasks.backend.auth.dto.UserUpdateDTO;
import com.veraz.tasks.backend.auth.dto.UsersResponseDTO;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;
import com.veraz.tasks.backend.shared.dto.PaginatedResponseDTO.PaginationInfo;
import com.veraz.tasks.backend.shared.util.PaginationUtils;
import com.veraz.tasks.backend.auth.model.Perm;
import com.veraz.tasks.backend.auth.model.Role;
import com.veraz.tasks.backend.auth.model.User;
import com.veraz.tasks.backend.auth.repository.RoleRepository;
import com.veraz.tasks.backend.auth.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final MessageSource messageSource;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(MessageSource messageSource,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.messageSource = messageSource;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequest) {
        try {
            if (userRepository.findByUsernameOrEmailAllIgnoreCase(userRequest.getUsername(),
                    userRequest.getEmail()).isPresent()) {
                return new UserResponseDTO(null, null,
                        messageSource.getMessage("user.already.exists",
                                new Object[] { userRequest.getUsername() + " or " + userRequest.getEmail() },
                                LocaleContextHolder.getLocale()));
            }

            Role defaultRole = roleRepository.findByName("USER");
            String encodedPassword = passwordEncoder.encode(userRequest.getPassword());

            User newUser = toUser(userRequest, encodedPassword, defaultRole);
            userRepository.save(newUser);

            logger.info("User created successfully: {}", newUser.getUsername());

            return new UserResponseDTO(
                    toUserDetailDto(newUser),
                    null, // No JWT token for user creation
                    messageSource.getMessage("user.created.successfully", null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            logger.error("Error creating user: " + e.getMessage());
            return new UserResponseDTO(null, null,
                    messageSource.getMessage("user.error.creating", null, LocaleContextHolder.getLocale()));
        }
    }

    @Transactional(readOnly = true)
    public UserDetailDto getUserByID(UUID id) {
        logger.info("Getting user by id: {}", id);
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        user.getRoles().size();
        return toUserDetailDto(user);
    }

    @Transactional(readOnly = true)
    public UsersResponseDTO getAllUsers(PaginationRequestDTO paginationRequest) {
        logger.info("Getting all users with pagination - page: {}, size: {}, search: {}",
                paginationRequest.getPage(), paginationRequest.getSize(),
                paginationRequest.getSearch());

        paginationRequest.validateAndNormalize();

        Pageable pageable = PaginationUtils.createPageable(paginationRequest);
        Page<User> userPage;

        if (paginationRequest.hasSearch()) {
            String searchTerm = paginationRequest.getSearch().trim();
            logger.info("Searching users with term: '{}' in username, email and roles", searchTerm);
            userPage = userRepository.findByUsernameOrEmailOrRolesNameContainingIgnoreCase(searchTerm, pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        List<UserDetailDto> userDetailDtos = userPage.getContent().stream()
                .map(this::toUserDetailDto)
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

        logger.info("Found {} users out of {} total", userPage.getContent().size(), userPage.getTotalElements());

        return UsersResponseDTO.builder()
                .users(userDetailDtos)
                .pagination(paginationInfo)
                .build();
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmailOrUsername(String query) {
        logger.info("Searching user by email or username: {}", query);
        return userRepository.findByUsernameOrEmailAllIgnoreCase(query, query)
                .map(user -> new UserResponseDTO(toUserDetailDto(user), null, null))
                .orElse(new UserResponseDTO(null, null,
                        messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale())));
    }

    @Transactional
    public UserResponseDTO patchUser(UUID id, UserUpdateDTO userUpdateDTO) {
        logger.info("Patching user with ID: {}", id);
        if (id == null) {
            throw new RuntimeException("User ID is required");
        }

        User userToUpdate = userRepository.findById(id).orElse(null);
        if (userToUpdate == null) {
            return new UserResponseDTO(null, null,
                    messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()));
        }

        // Validar y actualizar username
        if (userUpdateDTO.getUsername() != null && !userUpdateDTO.getUsername().trim().isEmpty()) {
            String newUsername = userUpdateDTO.getUsername().trim();
            logger.info("Updating username from '{}' to '{}'", userToUpdate.getUsername(), newUsername);

            // Solo validar si el username es diferente al actual
            if (!newUsername.equalsIgnoreCase(userToUpdate.getUsername())) {
                userRepository.findByUsername(newUsername)
                        .filter(existingUser -> !existingUser.getId().equals(id))
                        .ifPresent(existingUser -> {
                            logger.warn("Username '{}' already exists for user ID: {}", newUsername,
                                    existingUser.getId());
                            throw new RuntimeException(messageSource.getMessage("user.username.already.exists", null,
                                    LocaleContextHolder.getLocale()));
                        });
            }

            userToUpdate.setUsername(newUsername);
            logger.info("Username set to: '{}'", userToUpdate.getUsername());
        }

        // Validar y actualizar email
        if (userUpdateDTO.getEmail() != null && !userUpdateDTO.getEmail().trim().isEmpty()) {
            String newEmail = userUpdateDTO.getEmail().trim();
            logger.info("Updating email from '{}' to '{}'", userToUpdate.getEmail(), newEmail);

            // Solo validar si el email es diferente al actual
            if (!newEmail.equalsIgnoreCase(userToUpdate.getEmail())) {
                userRepository.findByEmail(newEmail)
                        .filter(existingUser -> !existingUser.getId().equals(id))
                        .ifPresent(existingUser -> {
                            logger.warn("Email '{}' already exists for user ID: {}", newEmail, existingUser.getId());
                            throw new RuntimeException(messageSource.getMessage("user.email.already.exists", null,
                                    LocaleContextHolder.getLocale()));
                        });
            }

            userToUpdate.setEmail(newEmail);
            logger.info("Email set to: '{}'", userToUpdate.getEmail());
        }

        // Actualizar contraseña si se proporciona
        if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().trim().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(userUpdateDTO.getPassword());
            userToUpdate.setPassword(encodedPassword);
        }

        // Actualizar otros campos
        if (userUpdateDTO.getIsActive() != null) {
            userToUpdate.setIsActive(userUpdateDTO.getIsActive());
        }

        logger.info("Updating user with ID: {}", userToUpdate.getId());
        logger.info("Before save - Email: '{}', Username: '{}'", userToUpdate.getEmail(), userToUpdate.getUsername());

        userRepository.save(userToUpdate);

        logger.info("After save - Email: '{}', Username: '{}'", userToUpdate.getEmail(), userToUpdate.getUsername());

        return new UserResponseDTO(toUserDetailDto(userToUpdate), null,
                messageSource.getMessage("user.updated.successfully", null, LocaleContextHolder.getLocale()));
    }

    @Transactional
    public UserResponseDTO deleteUser(UUID id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return new UserResponseDTO(null, null,
                    messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()));
        }
        userRepository.delete(user);
        return new UserResponseDTO(null, null,
                messageSource.getMessage("user.deleted.successfully", null, LocaleContextHolder.getLocale()));
    }

    // Mapper
    public UserDetailDto toUserDetailDto(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        Set<String> perms = user.getRoles()
                .stream()
                .flatMap(role -> role.getPerms()
                        .stream())
                .map(Perm::getName)
                .collect(Collectors.toSet());

        UserDetailDto userDto = new UserDetailDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setIsActive(user.getIsActive());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setRoles(roles);
        userDto.setPerms(perms);

        return userDto;
    }

    public User toUser(UserRequestDTO userRequest, String encodedPassword, Role defaultRole) {
        return new User(
                userRequest.getUsername(),
                userRequest.getEmail(),
                encodedPassword,
                defaultRole);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameOrEmailAllIgnoreCase(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + username));
    }
}