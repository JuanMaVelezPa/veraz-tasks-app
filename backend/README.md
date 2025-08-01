# Tasks Backend API

REST API for task management built with Spring Boot 3.5.4.

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

## 🔧 Configuration

### Database
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tasks_app_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### JWT
```properties
veraz.app.jwtSecret=your_secure_jwt_secret_min_32_chars
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
├── exception/      # Global exception handling
└── shared/         # Shared configurations
    ├── config/     # Application configs
    ├── constants/  # Message keys and constants
    ├── dto/        # Shared DTOs
    └── util/       # Utility classes
```

## 🔐 Authentication

- **Sign-in endpoint:** `/auth/sign-in`
- **Sign-up endpoint:** `/auth/sign-up`
- **Default user:** `admin` / `admin123`
- **JWT Token:** Required for protected endpoints

### Protected Endpoints
All endpoints except `/auth/sign-in`, `/auth/sign-up`, and Swagger documentation require authentication.

## 🆕 Recent Features & Improvements

### 👥 Enhanced User Management
- **User Deletion:** Complete user deletion with proper response handling
- **Response Optimization:** Improved HTTP status codes for better frontend integration
- **Message Localization:** Enhanced message handling with i18n support
- **Error Handling:** Comprehensive error responses with descriptive messages

### 🔧 API Improvements
- **Consistent Responses:** Standardized response format across all endpoints
- **Status Code Optimization:** Proper HTTP status codes (200, 404, 409, 400)
- **Message Prioritization:** Backend messages take priority over frontend defaults
- **Validation Enhancement:** Improved input validation with descriptive error messages

### 🛡️ Security Enhancements
- **Role-based Access:** Enhanced permission system for user operations
- **Input Validation:** Comprehensive validation for all user inputs
- **Error Security:** Secure error messages that don't expose sensitive information
- **Transaction Management:** Proper transaction handling for data consistency

## 📝 Logging

- **File:** `./logs/tasks-backend.log`
- **Level:** INFO for production, DEBUG for development
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
- **Message Internationalization:** Multi-language support
- **Pagination Support:** Efficient data pagination
- **Validation Framework:** Comprehensive input validation

## 📊 API Endpoints

### Authentication
- `POST /auth/sign-in` - User login
- `POST /auth/sign-up` - User registration
- `GET /auth/check-status` - Check authentication status

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

## 🚀 Deployment

```bash
# Build for production
mvn clean package -Pprod

# Run JAR file
java -jar target/backend-0.0.1-SNAPSHOT.jar
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

All API responses follow a standardized format:

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

## 🛡️ Security Considerations

- **JWT Secret:** Use a strong secret (minimum 32 characters) in production
- **Database:** Use environment variables for database credentials
- **CORS:** Configure CORS properly for your frontend domain
- **Logging:** Avoid logging sensitive information
- **Validation:** All inputs are validated server-side

## 📚 Documentation

- **Swagger UI:** http://localhost:8080/api/swagger-ui.html
- **API Docs:** Auto-generated from code annotations
- **Postman Collection:** Available in `src/test/postman/`

## 🤝 Contributing

1. Follow the existing code style
2. Add tests for new features
3. Update documentation as needed
4. Use meaningful commit messages 