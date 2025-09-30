package com.veraz.tasks.backend.application.business.commands;

import com.veraz.tasks.backend.application.business.dto.EmployeeResponse;
import com.veraz.tasks.backend.domain.business.entities.Employee;
import com.veraz.tasks.backend.domain.business.repositories.EmployeeRepository;
import com.veraz.tasks.backend.domain.business.valueobjects.EmployeeId;
import com.veraz.tasks.backend.infrastructure.business.dto.UpdateEmployeeRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class UpdateEmployeeUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UpdateEmployeeUseCase.class);

    private final EmployeeRepository employeeRepository;

    public UpdateEmployeeUseCase(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeResponse execute(String employeeIdString, UpdateEmployeeRequest request) {
        logger.debug("Updating employee with ID: {}", employeeIdString);

        try {
            EmployeeId employeeId = EmployeeId.of(employeeIdString);

            Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
            if (employeeOpt.isEmpty()) {
                throw new IllegalArgumentException("Employee not found with ID: " + employeeIdString);
            }

            Employee employee = employeeOpt.get();

            if (request.position() != null) {
                employee.updatePosition(request.position());
            }
            if (request.department() != null) {
                employee.updateDepartment(request.department());
            }
            if (request.employmentType() != null) {
                employee.updateEmploymentType(request.employmentType());
            }
            if (request.status() != null) {
                employee.updateStatus(request.status());
            }
            if (request.hireDate() != null) {
                employee.updateHireDate(request.hireDate());
            }
            if (request.terminationDate() != null) {
                employee.updateTerminationDate(request.terminationDate());
            }
            if (request.probationEndDate() != null) {
                employee.updateProbationEndDate(request.probationEndDate());
            }
            if (request.salary() != null || request.currency() != null || request.salaryType() != null) {
                employee.updateSalary(
                        request.salary() != null ? request.salary() : employee.getSalary(),
                        request.currency() != null ? request.currency() : employee.getCurrency(),
                        request.salaryType() != null ? request.salaryType() : employee.getSalaryType());
            }
            if (request.workEmail() != null || request.workPhone() != null) {
                employee.updateWorkContact(
                        request.workEmail() != null ? request.workEmail() : employee.getWorkEmail(),
                        request.workPhone() != null ? request.workPhone() : employee.getWorkPhone());
            }
            if (request.workLocation() != null || request.workSchedule() != null || request.workShift() != null) {
                employee.updateWorkLocation(
                        request.workLocation() != null ? request.workLocation() : employee.getWorkLocation(),
                        request.workSchedule() != null ? request.workSchedule() : employee.getWorkSchedule(),
                        request.workShift() != null ? request.workShift() : employee.getWorkShift());
            }
            if (request.jobLevel() != null || request.costCenter() != null) {
                employee.updateJobDetails(
                        request.jobLevel() != null ? request.jobLevel() : employee.getJobLevel(),
                        request.costCenter() != null ? request.costCenter() : employee.getCostCenter());
            }
            if (request.skills() != null || request.certifications() != null || request.education() != null) {
                employee.updateSkillsAndEducation(
                        request.skills() != null ? request.skills() : employee.getSkills(),
                        request.certifications() != null ? request.certifications() : employee.getCertifications(),
                        request.education() != null ? request.education() : employee.getEducation());
            }
            if (request.benefits() != null) {
                employee.updateBenefits(request.benefits());
            }
            if (request.notes() != null) {
                employee.updateNotes(request.notes());
            }
            if (request.isActive() != null) {
                if (request.isActive()) {
                    employee.activate();
                } else {
                    employee.deactivate();
                }
            }

            Employee updatedEmployee = employeeRepository.save(employee);

            logger.debug("Successfully updated employee with ID: {}", employeeIdString);
            return EmployeeResponse.from(updatedEmployee);

        } catch (Exception e) {
            logger.error("Error updating employee {}: {}", employeeIdString, e.getMessage(), e);
            throw e;
        }
    }
}
