package com.veraz.tasks.backend.auth.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import com.veraz.tasks.backend.auth.dto.UserResponseDTO;
import com.veraz.tasks.backend.auth.dto.UserCreateRequestDTO;
import com.veraz.tasks.backend.auth.dto.UserUpdateRequestDTO;
import com.veraz.tasks.backend.auth.model.Perm;
import com.veraz.tasks.backend.auth.model.Role;
import com.veraz.tasks.backend.auth.model.User;

public class UserMapper {

    /**
     * Converts User entity to UserResponseDTO
     * @param user User entity
     * @return UserResponseDTO
     */
    public static UserResponseDTO toDto(User user) {
        if (user == null) return null;
        
        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        Set<String> perms = user.getRoles()
                .stream()
                .flatMap(role -> role.getPerms()
                        .stream())
                .map(Perm::getName)
                .collect(Collectors.toSet());

        UserResponseDTO userDto = new UserResponseDTO();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setIsActive(user.getIsActive());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());
        userDto.setRoles(roles);
        userDto.setPerms(perms);

        return userDto;
    }

    /**
     * Converts UserCreateRequestDTO to User entity for creation
     * @param userRequest Create request DTO
     * @param encodedPassword Encoded password
     * @param defaultRole Default role for the user
     * @return User entity
     */
    public static User toEntity(UserCreateRequestDTO userRequest, String encodedPassword, Role defaultRole) {
        if (userRequest == null) return null;
        
        return new User(
                userRequest.getUsername(),
                userRequest.getEmail(),
                encodedPassword,
                defaultRole);
    }

    /**
     * Updates an existing User entity with data from UserUpdateRequestDTO
     * @param user Existing user entity
     * @param userRequest Update request DTO
     * @param encodedPassword Encoded password (optional)
     * @return Updated User entity
     */
    public static User updateEntity(User user, UserUpdateRequestDTO userRequest, String encodedPassword) {
        if (user == null || userRequest == null) return user;
        
        // Update username if provided and not empty
        if (userRequest.getUsername() != null && !userRequest.getUsername().trim().isEmpty()) {
            user.setUsername(userRequest.getUsername().trim().toLowerCase());
        }
        
        // Update email if provided and not empty
        if (userRequest.getEmail() != null && !userRequest.getEmail().trim().isEmpty()) {
            user.setEmail(userRequest.getEmail().trim().toLowerCase());
        }
        
        // Update password if provided
        if (encodedPassword != null) {
            user.setPassword(encodedPassword);
        }
        
        // Update active status if provided
        if (userRequest.getIsActive() != null) {
            user.setIsActive(userRequest.getIsActive());
        }
        
        return user;
    }

    /**
     * Converts a set of Users to DTOs
     * @param users Set of users
     * @return Set of DTOs
     */
    public static Set<UserResponseDTO> toDtoSet(Set<User> users) {
        if (users == null) return Set.of();
        
        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toSet());
    }
}
