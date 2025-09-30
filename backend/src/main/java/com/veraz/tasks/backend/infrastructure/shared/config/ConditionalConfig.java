package com.veraz.tasks.backend.infrastructure.shared.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ConditionalConfig {

    @Bean
    @Profile("dev")
    @ConditionalOnProperty(name = "veraz.clean-architecture.enable-dependency-validation", havingValue = "true", matchIfMissing = true)
    public DependencyValidator dependencyValidator() {
        return new DependencyValidator();
    }

    @Bean
    @ConditionalOnProperty(name = "veraz.clean-architecture.enable-bean-creation-logging", havingValue = "true")
    public BeanCreationLogger beanCreationLogger() {
        return new BeanCreationLogger();
    }

    @Bean
    @Profile("prod")
    @ConditionalOnProperty(name = "veraz.clean-architecture.enable-repository-cache", havingValue = "true")
    public RepositoryCacheManager repositoryCacheManager() {
        return new RepositoryCacheManager();
    }

    public static class DependencyValidator {
        public void validateDependencies() {
            System.out.println("Validating Clean Architecture dependencies...");
        }
    }

    public static class BeanCreationLogger {
        public void logBeanCreation(String beanName) {
            System.out.println("Bean created: " + beanName);
        }
    }

    public static class RepositoryCacheManager {
        public void initializeCache() {
            System.out.println("Initializing repository cache...");
        }
    }
}
