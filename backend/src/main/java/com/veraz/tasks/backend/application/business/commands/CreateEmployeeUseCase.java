package com.veraz.tasks.backend.application.business.commands;

import com.veraz.tasks.backend.infrastructure.business.dto.CreateEmployeeRequest;
import com.veraz.tasks.backend.application.business.dto.EmployeeResponse;
import com.veraz.tasks.backend.domain.business.entities.Employee;
import com.veraz.tasks.backend.domain.business.repositories.EmployeeRepository;
import com.veraz.tasks.backend.domain.business.repositories.PersonRepository;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;

public class CreateEmployeeUseCase {

    private final EmployeeRepository employeeRepository;
    private final PersonRepository personRepository;

    public CreateEmployeeUseCase(EmployeeRepository employeeRepository, PersonRepository personRepository) {
        this.employeeRepository = employeeRepository;
        this.personRepository = personRepository;
    }

    public EmployeeResponse execute(CreateEmployeeRequest request) {
        // Validar que la persona existe
        PersonId personId = PersonId.of(request.personId());
        if (!personRepository.findById(personId).isPresent()) {
            throw new IllegalArgumentException("Person with ID " + request.personId() + " does not exist");
        }

        // Validar que no existe un empleado para esta persona
        if (employeeRepository.existsByPersonId(personId)) {
            throw new IllegalArgumentException("Employee already exists for person with ID " + request.personId());
        }

        // Crear empleado con campos obligatorios
        Employee employee = Employee.create(
                personId,
                request.hireDate(),
                request.position(),
                request.status(),
                request.salary(),
                request.currency(),
                request.salaryType());

        // Actualizar campos opcionales si están presentes
        if (request.department() != null && !request.department().trim().isEmpty()) {
            employee.updateDepartment(request.department());
        }

        if (request.employmentType() != null && !request.employmentType().trim().isEmpty()) {
            employee.updateEmploymentType(request.employmentType());
        }

        // Actualizar información de contacto laboral
        if (request.workEmail() != null || request.workPhone() != null) {
            employee.updateWorkContact(
                request.workEmail() != null ? request.workEmail() : null,
                request.workPhone() != null ? request.workPhone() : null
            );
        }

        // Actualizar información de ubicación y horario laboral
        if (request.workLocation() != null || request.workSchedule() != null || request.workShift() != null) {
            employee.updateWorkLocation(
                request.workLocation() != null ? request.workLocation() : null,
                request.workSchedule() != null ? request.workSchedule() : null,
                request.workShift() != null ? request.workShift() : null
            );
        }

        // Actualizar detalles del trabajo
        if (request.jobLevel() != null || request.costCenter() != null) {
            employee.updateJobDetails(
                request.jobLevel() != null ? request.jobLevel() : null,
                request.costCenter() != null ? request.costCenter() : null
            );
        }

        // Actualizar habilidades y educación
        if (request.skills() != null || request.certifications() != null || request.education() != null) {
            employee.updateSkillsAndEducation(
                request.skills() != null ? request.skills() : null,
                request.certifications() != null ? request.certifications() : null,
                request.education() != null ? request.education() : null
            );
        }

        if (request.benefits() != null && !request.benefits().trim().isEmpty()) {
            employee.updateBenefits(request.benefits());
        }

        if (request.notes() != null && !request.notes().trim().isEmpty()) {
            employee.updateNotes(request.notes());
        }

        // Guardar empleado
        Employee savedEmployee = employeeRepository.save(employee);

        return EmployeeResponse.from(savedEmployee);
    }
}
