# Backend - Tasks App

API REST para gestión de tareas desarrollada con Spring Boot 3.5.3.

## 🚀 Inicio Rápido

### Prerrequisitos
- Java 21
- Maven 3.6+

### Instalación
```bash
# Instalar dependencias
mvn clean install

# Ejecutar aplicación
./mvnw spring-boot:run
```

### Verificación
- **API:** http://localhost:3000/api
- **Swagger:** http://localhost:3000/api/swagger-ui.html

## 🔧 Configuración

### Base de Datos
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tasks_app_db
spring.datasource.username=tasks_app_user
spring.datasource.password=tasks_app_user
```

### JWT
```properties
veraz.app.jwtSecret=V3r4zT4sks@AppP@sS@pP@sS@w0Rd1o10sS
veraz.app.jwtExpirationMs=86400000
```

## 📁 Estructura

```
src/main/java/com/veraz/tasks/backend/
├── auth/           # Autenticación y autorización
├── exception/      # Manejo de excepciones
└── shared/         # Configuraciones compartidas
```

## 🔐 Autenticación

- **Endpoint:** `/api/auth/signIn`
- **Usuario por defecto:** `admin` / `Abc123456*`
- **JWT Token:** Requerido para endpoints protegidos

## 📝 Logs

- **Archivo:** `./logs/tasks-backend.log`
- **Nivel:** DEBUG para desarrollo

## 🛠️ Desarrollo

```bash
# Ejecutar tests
mvn test

# Limpiar y compilar
mvn clean compile

# Ejecutar con perfil de desarrollo
./mvnw spring-boot:run -Dspring.profiles.active=dev
``` 