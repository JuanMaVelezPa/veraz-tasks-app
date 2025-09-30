package com.veraz.tasks.backend.infrastructure.business.persistence;

import com.veraz.tasks.backend.domain.business.entities.Employee;
import com.veraz.tasks.backend.domain.business.repositories.EmployeeRepository;
import com.veraz.tasks.backend.domain.business.valueobjects.EmployeeId;
import com.veraz.tasks.backend.domain.business.valueobjects.PersonId;
import com.veraz.tasks.backend.shared.dto.PaginationRequestDTO;
import com.veraz.tasks.backend.shared.util.PaginationUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Employee repository implementation using JPA
 */
@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {
    
    private final EmployeeJpaRepository jpaRepository;
    
    public EmployeeRepositoryImpl(EmployeeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    @Transactional
    public Employee save(Employee employee) {
        EmployeeEntity entity = EmployeeEntity.from(employee);
        EmployeeEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> findById(EmployeeId id) {
        return jpaRepository.findById(id.getValue())
                .map(EmployeeEntity::toDomain);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> findByPersonId(PersonId personId) {
        return jpaRepository.findByPersonId(personId.getValue())
                .map(EmployeeEntity::toDomain);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Employee> findAll(PaginationRequestDTO paginationRequest) {
        var pageable = PaginationUtils.createPageable(paginationRequest);
        Page<EmployeeEntity> employeeEntities;
        
        if (paginationRequest.hasSearch()) {
            employeeEntities = jpaRepository.findBySearchTerm(paginationRequest.search(), pageable);
        } else {
            employeeEntities = jpaRepository.findAll(pageable);
        }
        
        return employeeEntities.map(EmployeeEntity::toDomain);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByPersonId(PersonId personId) {
        return jpaRepository.existsByPersonId(personId.getValue());
    }
    
    @Override
    @Transactional
    public void delete(Employee employee) {
        EmployeeEntity entity = EmployeeEntity.from(employee);
        jpaRepository.delete(entity);
    }
    
    @Override
    @Transactional
    public void deleteById(EmployeeId id) {
        jpaRepository.deleteById(id.getValue());
    }
    
    @Override
    @Transactional(readOnly = true)
    public long count() {
        return jpaRepository.count();
    }
}

