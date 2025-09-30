package com.veraz.tasks.backend.infrastructure.identity.config;

import com.veraz.tasks.backend.application.identity.commands.*;
import com.veraz.tasks.backend.application.identity.queries.*;
import com.veraz.tasks.backend.application.shared.services.UserResponseMapper;
import com.veraz.tasks.backend.domain.identity.repositories.UserRepository;
import com.veraz.tasks.backend.domain.business.repositories.PersonRepository;
import com.veraz.tasks.backend.domain.identity.repositories.RoleRepository;
import com.veraz.tasks.backend.domain.identity.services.PasswordEncoder;
import com.veraz.tasks.backend.domain.identity.services.JwtTokenGenerator;
import com.veraz.tasks.backend.infrastructure.identity.auth.DomainUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdentityConfig {

    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository userRepository,
            UserResponseMapper userResponseMapper,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository) {
        return new CreateUserUseCase(userRepository, userResponseMapper, passwordEncoder, roleRepository);
    }

    @Bean
    public FindUserByIdUseCase findUserByIdUseCase(UserRepository userRepository,
            UserResponseMapper userResponseMapper) {
        return new FindUserByIdUseCase(userRepository, userResponseMapper);
    }

    @Bean
    public FindAllUsersUseCase findAllUsersUseCase(UserRepository userRepository,
            UserResponseMapper userResponseMapper) {
        return new FindAllUsersUseCase(userRepository, userResponseMapper);
    }

    @Bean
    public FindAvailableUsersUseCase findAvailableUsersUseCase(UserRepository userRepository,
            UserResponseMapper userResponseMapper) {
        return new FindAvailableUsersUseCase(userRepository, userResponseMapper);
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase(UserRepository userRepository,
            UserResponseMapper userResponseMapper,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository) {
        return new UpdateUserUseCase(userRepository, userResponseMapper, passwordEncoder, roleRepository);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(UserRepository userRepository, PersonRepository personRepository) {
        return new DeleteUserUseCase(userRepository, personRepository);
    }

    @Bean
    public FindRoleByIdUseCase findRoleByIdUseCase(RoleRepository roleRepository) {
        return new FindRoleByIdUseCase(roleRepository);
    }

    @Bean
    public FindAllActiveRolesUseCase findAllActiveRolesUseCase(RoleRepository roleRepository) {
        return new FindAllActiveRolesUseCase(roleRepository);
    }

    @Bean
    public RemoveRoleFromUserUseCase removeRoleFromUserUseCase(UserRepository userRepository) {
        return new RemoveRoleFromUserUseCase(userRepository);
    }

    @Bean
    public LoginUseCase loginUseCase(UserRepository userRepository,
            UserResponseMapper userResponseMapper,
            PasswordEncoder passwordEncoder,
            JwtTokenGenerator jwtTokenGenerator) {
        return new LoginUseCase(userRepository, userResponseMapper, passwordEncoder, jwtTokenGenerator);
    }

    @Bean
    public ValidateTokenUseCase validateTokenUseCase(UserRepository userRepository,
            UserResponseMapper userResponseMapper,
            JwtTokenGenerator jwtTokenGenerator) {
        return new ValidateTokenUseCase(userRepository, userResponseMapper, jwtTokenGenerator);
    }

    @Bean
    public ValidateTokenHeaderUseCase validateTokenHeaderUseCase(ValidateTokenUseCase validateTokenUseCase) {
        return new ValidateTokenHeaderUseCase(validateTokenUseCase);
    }

    @Bean
    public UserResponseMapper userResponseMapper(RoleRepository roleRepository) {
        return new UserResponseMapper(roleRepository);
    }

    @Bean
    public DomainUserDetailsService domainUserDetailsService(UserRepository userRepository,
            RoleRepository roleRepository) {
        return new DomainUserDetailsService(userRepository, roleRepository);
    }
}
