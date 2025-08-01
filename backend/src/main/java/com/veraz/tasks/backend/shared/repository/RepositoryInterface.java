package com.veraz.tasks.backend.shared.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base interface for repositories with generic CRUD operations
 * 
 * @param <T>  Type of the entity
 * @param <ID> Type of the entity ID
 */
@NoRepositoryBean
public interface RepositoryInterface<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    // This interface inherits all basic CRUD operations from JpaRepository
    // and adds support for specifications with JpaSpecificationExecutor
}