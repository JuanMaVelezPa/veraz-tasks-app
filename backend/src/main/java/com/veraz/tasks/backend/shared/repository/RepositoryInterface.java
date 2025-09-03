package com.veraz.tasks.backend.shared.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface RepositoryInterface<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}