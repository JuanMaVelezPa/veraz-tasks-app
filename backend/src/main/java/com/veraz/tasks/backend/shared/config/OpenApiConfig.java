package com.veraz.tasks.backend.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tasks Backend API")
                        .version("1.0.0")
                        .description("""
                                ## Tasks Backend API

                                API RESTful for the management of users and tasks with JWT authentication.

                                ### Características:
                                - 🔐 JWT Authentication
                                - 👥 User Management
                                - 🛡️ Spring Security
                                - 📝 Automatic Documentation

                                ### Endpoints Principales:
                                - **POST** `/auth/sign-in` - sign-in
                                - **POST** `/auth/sign-up` - sign-up
                                - **GET** `/auth/check-status` - Check authentication status

                                ### Autenticación:
                                For protected endpoints, include the JWT token in the header:
                                ```
                                Authorization: Bearer {your-jwt-token}
                                ```
                                """)
                        .termsOfService("https://github.com/juanmavelezpa/tasks-backend")
                        .contact(new Contact()
                                .name("Juan Manuel Velez Parra")
                                .url("https://www.linkedin.com/in/juanmavelezpa/")
                                .email("juanmavelezpa@gmail.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")
                                        .description(
                                                """
                                                        JWT Authorization header using the Bearer scheme.

                                                        **Formato:** `Authorization: Bearer {token}`

                                                        **Ejemplo:** `Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`

                                                        **Nota:** Obtén el token haciendo sign-in en `/auth/sign-in`
                                                        """)))
                .addServersItem(new Server()
                        .url("http://localhost:3000/api")
                        .description("Servidor de Desarrollo"))
                .addServersItem(new Server()
                        .url("https://api.tu-dominio.com")
                        .description("Servidor de Producción"));
    }
}