package com.veraz.tasks.backend.auth.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import com.veraz.tasks.backend.auth.dto.UserResponseDTO;
import com.veraz.tasks.backend.auth.dto.UserRequestDTO;
import com.veraz.tasks.backend.auth.model.Perm;
import com.veraz.tasks.backend.auth.model.Role;
import com.veraz.tasks.backend.auth.model.User;

public class UserMapper {

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

    public static User toEntity(UserRequestDTO userRequest, String encodedPassword, Role defaultRole) {
        if (userRequest == null) return null;
        
        return new User(
                userRequest.getUsername(),
                userRequest.getEmail(),
                encodedPassword,
                defaultRole);
    }

    /**
     * Actualiza una entidad User existente con datos del DTO
     * @param user Entidad existente
     * @param userRequest DTO con datos a actualizar
     * @param encodedPassword Contraseña codificada (opcional)
     * @return User actualizado
     */
    public static User updateEntity(User user, UserRequestDTO userRequest, String encodedPassword) {
        if (user == null || userRequest == null) return user;
        
        if (userRequest.getUsername() != null && !userRequest.getUsername().trim().isEmpty()) {
            user.setUsername(userRequest.getUsername().trim().toLowerCase());
        }
        
        if (userRequest.getEmail() != null && !userRequest.getEmail().trim().isEmpty()) {
            user.setEmail(userRequest.getEmail().trim().toLowerCase());
        }
        
        if (encodedPassword != null) {
            user.setPassword(encodedPassword);
        }
        
        if (userRequest.getIsActive() != null) {
            user.setIsActive(userRequest.getIsActive());
        }
        
        return user;
    }

    /**
     * Convierte una lista de Users a DTOs
     * @param users Lista de usuarios
     * @return Lista de DTOs
     */
    public static Set<UserResponseDTO> toDtoSet(Set<User> users) {
        if (users == null) return Set.of();
        
        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toSet());
    }

}
