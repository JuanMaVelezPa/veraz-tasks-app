package com.veraz.tasks.backend.infrastructure.config;

import com.veraz.tasks.backend.infrastructure.shared.config.CleanArchitectureProperties;
import com.veraz.tasks.backend.infrastructure.shared.config.SharedRepositoryConfig;
import com.veraz.tasks.backend.infrastructure.shared.config.ConditionalConfig;
import com.veraz.tasks.backend.infrastructure.shared.config.ComponentScanConfig;
import com.veraz.tasks.backend.infrastructure.identity.config.IdentityConfig;
import com.veraz.tasks.backend.infrastructure.business.config.BusinessConfig;
import com.veraz.tasks.backend.infrastructure.project.config.ProjectConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(CleanArchitectureProperties.class)
@Import({
        SharedRepositoryConfig.class,
        IdentityConfig.class,
        BusinessConfig.class,
        ProjectConfig.class,
        ConditionalConfig.class,
        ComponentScanConfig.class
})
public class CleanArchitectureConfig {
}
