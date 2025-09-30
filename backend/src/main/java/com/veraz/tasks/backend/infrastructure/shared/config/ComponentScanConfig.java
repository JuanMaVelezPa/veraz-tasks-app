package com.veraz.tasks.backend.infrastructure.shared.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
    basePackages = {
        "com.veraz.tasks.backend.application.usecases",
        "com.veraz.tasks.backend.application.services",
        "com.veraz.tasks.backend.domain.services",
        "com.veraz.tasks.backend.infrastructure.persistence.jpa",
        "com.veraz.tasks.backend.infrastructure.identity.controllers"
    },
    includeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*UseCase"),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*Service"),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*RepositoryImpl"),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*Mapper"),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*Controller")
    },
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*Entity")
    }
)
public class ComponentScanConfig {
}
