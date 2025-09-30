package com.veraz.tasks.backend.infrastructure.business.config;

import com.veraz.tasks.backend.application.business.commands.*;
import com.veraz.tasks.backend.application.business.queries.*;
import com.veraz.tasks.backend.domain.business.repositories.ClientRepository;
import com.veraz.tasks.backend.domain.business.repositories.EmployeeRepository;
import com.veraz.tasks.backend.domain.business.repositories.PersonRepository;
import com.veraz.tasks.backend.domain.identity.repositories.UserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BusinessConfig {

    @Bean
    public CreateClientUseCase createClientUseCase(ClientRepository clientRepository,
            PersonRepository personRepository) {
        return new CreateClientUseCase(clientRepository, personRepository);
    }

    @Bean
    public FindClientByIdUseCase findClientByIdUseCase(ClientRepository clientRepository) {
        return new FindClientByIdUseCase(clientRepository);
    }

    @Bean
    public FindClientByPersonIdUseCase findClientByPersonIdUseCase(ClientRepository clientRepository) {
        return new FindClientByPersonIdUseCase(clientRepository);
    }

    @Bean
    public FindAllClientsUseCase findAllClientsUseCase(ClientRepository clientRepository) {
        return new FindAllClientsUseCase(clientRepository);
    }

    @Bean
    public UpdateClientUseCase updateClientUseCase(ClientRepository clientRepository) {
        return new UpdateClientUseCase(clientRepository);
    }

    @Bean
    public DeleteClientUseCase deleteClientUseCase(ClientRepository clientRepository) {
        return new DeleteClientUseCase(clientRepository);
    }

    @Bean
    public CreateEmployeeUseCase createEmployeeUseCase(EmployeeRepository employeeRepository,
            PersonRepository personRepository) {
        return new CreateEmployeeUseCase(employeeRepository, personRepository);
    }

    @Bean
    public FindEmployeeByIdUseCase findEmployeeByIdUseCase(EmployeeRepository employeeRepository) {
        return new FindEmployeeByIdUseCase(employeeRepository);
    }

    @Bean
    public FindEmployeeByPersonIdUseCase findEmployeeByPersonIdUseCase(EmployeeRepository employeeRepository) {
        return new FindEmployeeByPersonIdUseCase(employeeRepository);
    }

    @Bean
    public FindAllEmployeesUseCase findAllEmployeesUseCase(EmployeeRepository employeeRepository) {
        return new FindAllEmployeesUseCase(employeeRepository);
    }

    @Bean
    public UpdateEmployeeUseCase updateEmployeeUseCase(EmployeeRepository employeeRepository) {
        return new UpdateEmployeeUseCase(employeeRepository);
    }

    @Bean
    public DeleteEmployeeUseCase deleteEmployeeUseCase(EmployeeRepository employeeRepository) {
        return new DeleteEmployeeUseCase(employeeRepository);
    }

    @Bean
    public CreatePersonUseCase createPersonUseCase(PersonRepository personRepository) {
        return new CreatePersonUseCase(personRepository);
    }

    @Bean
    public FindPersonByIdUseCase findPersonByIdUseCase(PersonRepository personRepository) {
        return new FindPersonByIdUseCase(personRepository);
    }

    @Bean
    public FindPersonByUserIdUseCase findPersonByUserIdUseCase(PersonRepository personRepository) {
        return new FindPersonByUserIdUseCase(personRepository);
    }

    @Bean
    public FindAllPersonsUseCase findAllPersonsUseCase(PersonRepository personRepository) {
        return new FindAllPersonsUseCase(personRepository);
    }

    @Bean
    public UpdatePersonUseCase updatePersonUseCase(PersonRepository personRepository) {
        return new UpdatePersonUseCase(personRepository);
    }

    @Bean
    public DeletePersonUseCase deletePersonUseCase(PersonRepository personRepository,
            ClientRepository clientRepository,
            EmployeeRepository employeeRepository) {
        return new DeletePersonUseCase(personRepository, clientRepository, employeeRepository);
    }

    @Bean
    public AssociateUserToPersonUseCase associateUserToPersonUseCase(PersonRepository personRepository,
            UserRepository userRepository) {
        return new AssociateUserToPersonUseCase(personRepository, userRepository);
    }

    @Bean
    public RemoveUserAssociationUseCase removeUserAssociationUseCase(PersonRepository personRepository) {
        return new RemoveUserAssociationUseCase(personRepository);
    }
}
