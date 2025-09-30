# Tasks Backend API

REST API for task management built with Spring Boot 3.5.4, featuring Clean Architecture and comprehensive internationalization.

## 🚀 Quick Start

### Prerequisites
- Java 21
- Maven 3.6+
- PostgreSQL 14+

### Installation
```bash
# Install dependencies
mvn clean install

# Run application
./mvnw spring-boot:run
```

### Verification
- **API:** http://localhost:8080/api
- **Swagger:** http://localhost:8080/api/swagger-ui.html

## 🏗️ Architecture

This project follows **Clean Architecture** principles:

```
📁 Clean Architecture
├── 🎯 Domain Layer (Business Logic)
│   ├── entities/          # Core business entities
│   ├── valueobjects/      # Domain value objects
│   ├── repositories/      # Repository interfaces
│   └── services/          # Domain services
├── 🔧 Application Layer (Use Cases)
│   ├── commands/          # Command handlers (CQRS)
│   ├── queries/           # Query handlers (CQRS)
│   └── handlers/          # Command/Query handlers
├── 🌐 Infrastructure Layer (External Concerns)
│   ├── controllers/       # REST controllers
│   ├── persistence/       # JPA repositories
│   └── config/            # Configuration classes
└── 🔄 Shared Layer (Cross-cutting)
    ├── constants/         # Message keys and constants
    ├── util/              # Utility classes
    └── dto/               # Shared DTOs
```

## 🌍 Internationalization

Comprehensive i18n support with optimized message management:

- **Centralized Messages:** All messages in `messages.properties` (EN) and `messages_es.properties` (ES)
- **MessageUtils:** Utility class for easy message retrieval
- **MessageKeys:** Centralized constants for all message keys
- **Optimized Structure:** 95+ messages organized by functionality

### Usage Example
```java
// Basic message retrieval
String message = MessageUtils.getMessage(MessageKeys.CONTROLLER_CREATED_SUCCESS, "User");

// Localized entity names
String entityName = MessageUtils.getLocalizedEntityName("user"); // "Usuario" in Spanish
```

## 🔐 Authentication

- **Sign-in:** `/auth/sign-in`
- **Sign-up:** `/auth/sign-up`
- **Default user:** `admin` / `admin123`
- **JWT Token:** Required for protected endpoints

## 📊 API Endpoints

### Authentication
- `POST /auth/sign-in` - User login
- `POST /auth/sign-up` - User registration

### User Management
- `GET /users` - Get all users (paginated)
- `GET /users/{id}` - Get user by ID
- `POST /users` - Create user
- `PATCH /users/{id}` - Update user
- `DELETE /users/{id}` - Delete user

### Person Management
- `GET /persons` - Get all persons (paginated)
- `GET /persons/{id}` - Get person by ID
- `POST /persons` - Create person
- `PATCH /persons/{id}` - Update person
- `DELETE /persons/{id}` - Delete person

### Employee Management
- `GET /employees` - Get all employees (paginated)
- `GET /employees/{id}` - Get employee by ID
- `POST /employees` - Create employee
- `PATCH /employees/{id}` - Update employee
- `DELETE /employees/{id}` - Delete employee

### Client Management
- `GET /clients` - Get all clients (paginated)
- `GET /clients/{id}` - Get client by ID
- `POST /clients` - Create client
- `PATCH /clients/{id}` - Update client
- `DELETE /clients/{id}` - Delete client

## 🔧 Key Features

- **JWT Authentication:** Secure token-based authentication
- **Spring Security:** Role-based access control
- **OpenAPI Documentation:** Auto-generated API docs
- **Global Exception Handling:** Centralized error management
- **Message Internationalization:** Multi-language support (EN/ES)
- **Clean Architecture:** Domain-driven design
- **CQRS Pattern:** Command and Query separation
- **Validation Framework:** Comprehensive input validation

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

## 📋 Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `JWT_SECRET` | JWT signing secret | Development secret |
| `JWT_EXPIRATION_MS` | JWT expiration time | 86400000 (24h) |
| `DB_URL` | Database connection URL | jdbc:postgresql://localhost:5432/tasks_app_db |
| `DB_USERNAME` | Database username | postgres |
| `DB_PASSWORD` | Database password | - |

## 🔍 API Response Format

```json
{
  "success": true,
  "status": "OK",
  "message": "Operation completed successfully",
  "data": {
    // Response data
  },
  "errors": null
}
```

## 🚀 Deployment

```bash
# Build for production
mvn clean package -Pprod

# Run JAR file
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

## 📚 Documentation

- **Swagger UI:** http://localhost:8080/api/swagger-ui.html
- **Postman Collection:** Available in `src/test/postman/`
- **Technical Debt:** Tracked in `TECHNICAL_DEBT.md`

## 🤝 Contributing

1. Follow Clean Architecture principles
2. Use MessageUtils for all user-facing messages
3. Add tests for new features
4. Update documentation as needed
5. Use meaningful commit messages