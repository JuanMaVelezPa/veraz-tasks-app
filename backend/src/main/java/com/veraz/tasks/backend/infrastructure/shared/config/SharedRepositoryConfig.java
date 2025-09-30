package com.veraz.tasks.backend.infrastructure.shared.config;

import com.veraz.tasks.backend.domain.identity.repositories.UserRepository;
import com.veraz.tasks.backend.domain.identity.repositories.RoleRepository;
import com.veraz.tasks.backend.domain.business.repositories.ClientRepository;
import com.veraz.tasks.backend.domain.business.repositories.EmployeeRepository;
import com.veraz.tasks.backend.domain.business.repositories.PersonRepository;
import com.veraz.tasks.backend.infrastructure.identity.persistence.UserRepositoryImpl;
import com.veraz.tasks.backend.infrastructure.identity.persistence.RoleRepositoryImpl;
import com.veraz.tasks.backend.infrastructure.business.persistence.ClientRepositoryImpl;
import com.veraz.tasks.backend.infrastructure.business.persistence.EmployeeRepositoryImpl;
import com.veraz.tasks.backend.infrastructure.business.persistence.PersonRepositoryImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SharedRepositoryConfig {

    @Bean
    public PersonRepository personRepository(PersonRepositoryImpl personRepositoryImpl) {
        return personRepositoryImpl;
    }

    @Bean
    public UserRepository userRepository(UserRepositoryImpl userRepositoryImpl) {
        return userRepositoryImpl;
    }

    @Bean
    public RoleRepository roleRepository(RoleRepositoryImpl roleRepositoryImpl) {
        return roleRepositoryImpl;
    }

    @Bean
    public ClientRepository clientRepository(ClientRepositoryImpl clientRepositoryImpl) {
        return clientRepositoryImpl;
    }

    @Bean
    public EmployeeRepository employeeRepository(EmployeeRepositoryImpl employeeRepositoryImpl) {
        return employeeRepositoryImpl;
    }

}
