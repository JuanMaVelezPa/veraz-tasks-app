package com.veraz.tasks.backend.infrastructure.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "veraz.clean-architecture")
public class CleanArchitectureProperties {

    private boolean enableDependencyValidation = true;
    private boolean enableBeanCreationLogging = false;
    private boolean enableBusinessRuleValidation = true;
    private int databaseTimeoutSeconds = 30;
    private boolean enableRepositoryCache = false;
    public boolean isEnableDependencyValidation() {
        return enableDependencyValidation;
    }

    public void setEnableDependencyValidation(boolean enableDependencyValidation) {
        this.enableDependencyValidation = enableDependencyValidation;
    }

    public boolean isEnableBeanCreationLogging() {
        return enableBeanCreationLogging;
    }

    public void setEnableBeanCreationLogging(boolean enableBeanCreationLogging) {
        this.enableBeanCreationLogging = enableBeanCreationLogging;
    }

    public boolean isEnableBusinessRuleValidation() {
        return enableBusinessRuleValidation;
    }

    public void setEnableBusinessRuleValidation(boolean enableBusinessRuleValidation) {
        this.enableBusinessRuleValidation = enableBusinessRuleValidation;
    }

    public int getDatabaseTimeoutSeconds() {
        return databaseTimeoutSeconds;
    }

    public void setDatabaseTimeoutSeconds(int databaseTimeoutSeconds) {
        this.databaseTimeoutSeconds = databaseTimeoutSeconds;
    }

    public boolean isEnableRepositoryCache() {
        return enableRepositoryCache;
    }

    public void setEnableRepositoryCache(boolean enableRepositoryCache) {
        this.enableRepositoryCache = enableRepositoryCache;
    }
}
