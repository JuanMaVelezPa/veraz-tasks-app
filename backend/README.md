# Backend - Tasks App

REST API for task management built with Spring Boot 3.5.4.

## 🚀 Quick Start

### Prerequisites
- Java 21
- Maven 3.6+

### Installation
```bash
# Install dependencies
mvn clean install

# Run application
./mvnw spring-boot:run
```

### Verification
- **API:** http://localhost:3000/api
- **Swagger:** http://localhost:3000/api/swagger-ui.html

## 🔧 Configuration

### Database
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

## 📁 Project Structure

```
src/main/java/com/veraz/tasks/backend/
├── auth/           # Authentication and authorization
│   ├── config/     # Security configuration
│   ├── controller/ # Auth endpoints
│   ├── dto/        # Data transfer objects
│   ├── model/      # User entities
│   ├── repository/ # Data access layer
│   └── service/    # Business logic
├── person/         # Person management
│   ├── controller/ # Person endpoints
│   ├── dto/        # Person DTOs
│   ├── mapper/     # Object mappers
│   ├── model/      # Person entities
│   ├── repository/ # Data access
│   └── service/    # Business logic
├── project/        # Project management (in development)
├── task/           # Task management (in development)
├── exception/      # Global exception handling
└── shared/         # Shared configurations
    ├── config/     # Application configs
    └── util/       # Utility classes
```

## 🔐 Authentication

- **Sign-in endpoint:** `/api/auth/sign-in`
- **Sign-up endpoint:** `/api/auth/sign-up`
- **Default user:** `admin_user` / `Abc123456*`
- **JWT Token:** Required for protected endpoints

### Protected Endpoints
All endpoints except `/auth/sign-in`, `/auth/sign-up`, and Swagger documentation require authentication.

## 📝 Logging

- **File:** `./logs/tasks-backend.log`
- **Level:** DEBUG for development
- **Console:** Formatted output with timestamps

## 🛠️ Development

```bash
# Run tests
mvn test

# Clean and compile
mvn clean compile

# Run with development profile
./mvnw spring-boot:run -Dspring.profiles.active=dev

# Build JAR file
mvn clean package
```

## 🔧 Key Features

- **JWT Authentication:** Secure token-based authentication
- **Spring Security:** Role-based access control
- **OpenAPI Documentation:** Auto-generated API docs
- **Global Exception Handling:** Centralized error management
- **Database Integration:** PostgreSQL with JPA/Hibernate
- **CORS Configuration:** Cross-origin resource sharing enabled

## 📊 API Endpoints

### Authentication
- `POST /api/auth/sign-in` - User login
- `POST /api/auth/sign-up` - User registration
- `GET /api/auth/check-status` - Check authentication status

### Person Management
- `GET /api/person` - Get all persons
- `POST /api/person` - Create person
- `PUT /api/person/{id}` - Update person
- `DELETE /api/person/{id}` - Delete person

### Employee Management
- `GET /api/employee` - Get all employees
- `POST /api/employee` - Create employee
- `PUT /api/employee/{id}` - Update employee
- `DELETE /api/employee/{id}` - Delete employee

### Client Management
- `GET /api/client` - Get all clients
- `POST /api/client` - Create client
- `PUT /api/client/{id}` - Update client
- `DELETE /api/client/{id}` - Delete client

## 🚀 Deployment

```bash
# Build for production
mvn clean package -Pprod

# Run JAR file
java -jar target/backend-0.0.1-SNAPSHOT.jar
``` 