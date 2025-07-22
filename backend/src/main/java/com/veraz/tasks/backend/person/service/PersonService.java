package com.veraz.tasks.backend.person.service;

import com.veraz.tasks.backend.auth.model.User;
import com.veraz.tasks.backend.auth.repository.UserRepository;
import com.veraz.tasks.backend.person.dto.PersonRequestDto;
import com.veraz.tasks.backend.person.dto.PersonResponseDto;
import com.veraz.tasks.backend.person.dto.UserSummaryDto;
import com.veraz.tasks.backend.person.mapper.PersonMapper;
import com.veraz.tasks.backend.person.model.Client;
import com.veraz.tasks.backend.person.model.Employee;
import com.veraz.tasks.backend.person.model.Person;
import com.veraz.tasks.backend.person.repository.ClientRepository;
import com.veraz.tasks.backend.person.repository.EmployeeRepository;
import com.veraz.tasks.backend.person.repository.PersonRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;
    private final MessageSource messageSource;

    public PersonService(PersonRepository personRepository, UserRepository userRepository,
            EmployeeRepository employeeRepository, ClientRepository clientRepository, MessageSource messageSource) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.clientRepository = clientRepository;
        this.messageSource = messageSource;
    }

    @Transactional(readOnly = true)
    public List<PersonResponseDto> getPersons() {
        List<Person> persons = personRepository.findAll();
        List<PersonResponseDto> personResponseDtos = new ArrayList<>();
        for (Person person : persons) {
            personResponseDtos.add(PersonResponseDto.builder()
                    .person(PersonMapper.toDto(person))
                    .message(null)
                    .status("OK")
                    .build());
        }
        return personResponseDtos;
    }

    @Transactional(readOnly = true)
    public PersonResponseDto getPersonById(UUID id) {
        Optional<Person> personOpt = personRepository.findById(id);
        if (personOpt.isEmpty()) {
            return PersonResponseDto.builder()
                    .person(null)
                    .message(messageSource.getMessage("person.not.found", null, LocaleContextHolder.getLocale()))
                    .status("NOT_FOUND")
                    .build();
        }

        return PersonResponseDto.builder()
                .person(PersonMapper.toDto(personOpt.get()))
                .message(null)
                .status("OK")
                .build();
    }

    @Transactional(readOnly = true)
    public List<UserSummaryDto> getAvailableUsers() {
        List<User> users = userRepository.findAll();
        List<UserSummaryDto> userSummaryDtos = new ArrayList<>();
        for (User user : users) {
            userSummaryDtos.add(UserSummaryDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .isActive(user.getIsActive())
                    .createdAt(user.getCreatedAt())
                    .build());
        }
        return userSummaryDtos;
    }

    @Transactional
    public PersonResponseDto createPerson(PersonRequestDto personRequestDto) {
        try {
            // Validar si se proporciona un usuario
            User user = null;
            if (personRequestDto.getUserId() != null) {
                Optional<User> userOpt = userRepository.findById(personRequestDto.getUserId());
                if (userOpt.isEmpty()) {
                    return PersonResponseDto.builder()
                            .person(null)
                            .message(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()))
                            .status("USER_NOT_FOUND")
                            .build();
                }
                user = userOpt.get();

                // Verificar si el usuario ya tiene una persona asociada
                Optional<Person> existingPerson = personRepository.findByUser(user);
                if (existingPerson.isPresent()) {
                    return PersonResponseDto.builder()
                            .person(null)
                            .message(messageSource.getMessage("user.already.has.person", null,
                                    LocaleContextHolder.getLocale()))
                            .status("USER_ALREADY_HAS_PERSON")
                            .build();
                }
            }

            // Crear la persona
            Person person = Person.builder()
                    .identType(personRequestDto.getIdentType())
                    .identNumber(personRequestDto.getIdentNumber())
                    .firstName(personRequestDto.getFirstName())
                    .lastName(personRequestDto.getLastName())
                    .birthDate(personRequestDto.getBirthDate())
                    .gender(personRequestDto.getGender())
                    .nationality(personRequestDto.getNationality())
                    .mobile(personRequestDto.getMobile())
                    .email(personRequestDto.getEmail())
                    .address(personRequestDto.getAddress())
                    .city(personRequestDto.getCity())
                    .country(personRequestDto.getCountry())
                    .postalCode(personRequestDto.getPostalCode())
                    .notes(personRequestDto.getNotes())
                    .user(user)
                    .build();

            person = personRepository.save(person);

            return PersonResponseDto.builder()
                    .person(PersonMapper.toDto(person))
                    .message(messageSource.getMessage("person.created.successfully", null,
                            LocaleContextHolder.getLocale()))
                    .status("CREATED")
                    .build();

        } catch (Exception e) {
            logger.error("Error creating person: " + e.getMessage());
            return PersonResponseDto.builder()
                    .person(null)
                    .message(messageSource.getMessage("person.error.creating", null, LocaleContextHolder.getLocale()))
                    .status("ERROR")
                    .build();
        }
    }

    @Transactional
    public PersonResponseDto updatePerson(UUID id, PersonRequestDto personRequestDto) {
        try {
            Optional<Person> personOpt = personRepository.findById(id);
            if (personOpt.isEmpty()) {
                return PersonResponseDto.builder()
                        .person(null)
                        .message(messageSource.getMessage("person.not.found", null, LocaleContextHolder.getLocale()))
                        .status("NOT_FOUND")
                        .build();
            }

            Person person = personOpt.get();

            // Validar si se está cambiando el usuario asociado
            if (personRequestDto.getUserId() != null) {
                Optional<User> userOpt = userRepository.findById(personRequestDto.getUserId());
                if (userOpt.isEmpty()) {
                    return PersonResponseDto.builder()
                            .person(null)
                            .message(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()))
                            .status("USER_NOT_FOUND")
                            .build();
                }

                User user = userOpt.get();

                // Verificar si el nuevo usuario es diferente al actual (manejar casos donde
                // person.getUser() es null)
                UUID currentUserId = person.getUser() != null ? person.getUser().getId() : null;
                if (!user.getId().equals(currentUserId)) {
                    Optional<Person> validUser = personRepository.findByUser(user);
                    if (validUser.isPresent()) {
                        return PersonResponseDto.builder()
                                .person(null)
                                .message(messageSource.getMessage("user.already.has.person", null,
                                        LocaleContextHolder.getLocale()))
                                .status("USER_ALREADY_HAS_PERSON")
                                .build();
                    }
                }

                person.setUser(user);
            }

            // Actualizar campos
            person.setIdentType(personRequestDto.getIdentType());
            person.setIdentNumber(personRequestDto.getIdentNumber());
            person.setFirstName(personRequestDto.getFirstName());
            person.setLastName(personRequestDto.getLastName());
            person.setBirthDate(personRequestDto.getBirthDate());
            person.setGender(personRequestDto.getGender());
            person.setNationality(personRequestDto.getNationality());
            person.setMobile(personRequestDto.getMobile());
            person.setEmail(personRequestDto.getEmail());
            person.setAddress(personRequestDto.getAddress());
            person.setCity(personRequestDto.getCity());
            person.setCountry(personRequestDto.getCountry());
            person.setPostalCode(personRequestDto.getPostalCode());
            person.setNotes(personRequestDto.getNotes());

            personRepository.save(person);

            return PersonResponseDto.builder()
                    .person(PersonMapper.toDto(person))
                    .message(messageSource.getMessage("person.updated.successfully", null,
                            LocaleContextHolder.getLocale()))
                    .status("UPDATED")
                    .build();
        } catch (Exception e) {
            logger.error("Error updating person: " + e.getMessage());
            return PersonResponseDto.builder()
                    .person(null)
                    .message(messageSource.getMessage("person.error.updating", null, LocaleContextHolder.getLocale()))
                    .status("ERROR")
                    .build();
        }
    }

    @Transactional
    public PersonResponseDto deletePerson(UUID id) {
        try {

            // Verificar si el usuario ya tiene una persona asociada
            Optional<Person> existingPerson = personRepository.findById(id);
            if (existingPerson.isPresent() && existingPerson.get().getUser() != null) {
                return PersonResponseDto.builder()
                        .person(null)
                        .message(messageSource.getMessage("person.has.user", null, LocaleContextHolder.getLocale()))
                        .status("PERSON_HAS_USER")
                        .build();
            }

            // Validar si tiene un empleado asociado
            Optional<Employee> employeeOpt = employeeRepository.findByPersonId(id);
            if (employeeOpt.isPresent()) {
                return PersonResponseDto.builder()
                        .person(null)
                        .message(messageSource.getMessage("person.has.employee", null, LocaleContextHolder.getLocale()))
                        .status("PERSON_HAS_EMPLOYEE")
                        .build();
            }

            // Validar si tiene un cliente
            Optional<Client> clientOpt = clientRepository.findByPersonId(id);
            if (clientOpt.isPresent()) {
                return PersonResponseDto.builder()
                        .person(null)
                        .message(messageSource.getMessage("person.has.client", null, LocaleContextHolder.getLocale()))
                        .status("PERSON_HAS_CLIENT")
                        .build();
            }

            Optional<Person> personOpt = personRepository.findById(id);
            if (personOpt.isEmpty()) {
                return PersonResponseDto.builder()
                        .person(null)
                        .message(messageSource.getMessage("person.not.found", null, LocaleContextHolder.getLocale()))
                        .status("NOT_FOUND")
                        .build();
            }
            personRepository.delete(personOpt.get());
            return PersonResponseDto.builder()
                    .person(null)
                    .message(messageSource.getMessage("person.deleted.successfully", null,
                            LocaleContextHolder.getLocale()))
                    .status("DELETED")
                    .build();
        } catch (Exception e) {
            logger.error("Error deleting person: " + e.getMessage());
            return PersonResponseDto.builder()
                    .person(null)
                    .message(messageSource.getMessage("person.error.deleting", null, LocaleContextHolder.getLocale()))
                    .status("ERROR")
                    .build();
        }
    }

}
