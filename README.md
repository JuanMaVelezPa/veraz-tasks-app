# Tasks App

A comprehensive task management application with Spring Boot backend, Angular frontend, and PostgreSQL database.

## 📁 Project Structure

```
tasks-app/
├── backend/          # Spring Boot REST API
├── frontend/         # Angular 20 SPA
├── database/         # PostgreSQL scripts and schemas
└── README.md         # This file
```

## 🚀 Quick Start

### Prerequisites

- **Java 21** or higher
- **Node.js 18** or higher
- **PostgreSQL 17** or higher
- **Maven 3.6** or higher

### Database Setup

1. **Create database:**
   ```bash
   createdb tasks_app_db
   ```

2. **Setup user and extensions (run as postgres superuser):**
   ```bash
   cd database/infrastructure/scripts
   # Windows PowerShell:
   .\00_setup_db_user_and_extensions.ps1
   # Linux/Mac:
   # psql -h localhost -p 5432 -U postgres -d tasks_app_db -f 00_setup_db_user_and_extensions.sql
   ```

3. **Apply initial schema:**
   ```bash
   cd database/infrastructure/scripts
   psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f 00_DROP_ALL.sql && psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f apply_patches.sql
   ```

**Note:** Default development credentials:
- **PostgreSQL Superuser:** `postgres` / `jmvelez`
- **Database User:** `tasks_app_user` / `tasks_app_user`
- **System Users:** `admin_user` / `Abc123456*`

### Backend Setup

1. **Navigate to backend directory:**
   ```bash
   cd backend
   ```

2. **Install dependencies:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Verify it's running:**
   - API: http://localhost:3000/api
   - Swagger Docs: http://localhost:3000/api/swagger-ui.html

### Frontend Setup

1. **Navigate to frontend directory:**
   ```bash
   cd frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Run the application:**
   ```bash
   npm start
   ```

4. **Verify it's running:**
   - Frontend: http://localhost:4200

## 📚 Detailed Documentation

- **[Backend Documentation](backend/README.md)** - Complete REST API guide
- **[Frontend Documentation](frontend/README.md)** - Angular application guide
- **[Database Documentation](database/README.md)** - Database configuration guide

## 🔧 Technologies

### Backend
- **Framework:** Spring Boot 3.5.4
- **Security:** Spring Security + JWT
- **Database:** PostgreSQL 17
- **Java:** 21
- **Build Tool:** Maven 3.6+
- **Documentation:** Swagger/OpenAPI 2.8.9
- **Port:** 3000

### Frontend
- **Framework:** Angular 20
- **UI Library:** DaisyUI + Tailwind CSS
- **Build Tool:** Angular CLI
- **Port:** 4200

## 🔐 Authentication

- **Sign-in endpoint:** `/api/auth/sign-in`
- **Default user:** `admin_user` / `Abc123456*`
- **JWT Token:** Required for protected endpoints

## 🆕 Recent Features & Improvements

### 🛡️ Centralized Error Handling System
- **HttpErrorService:** Centralized HTTP error management across the entire frontend
- **Contextual Messages:** Specific error messages based on operation type
- **Backend Integration:** Prioritizes backend-provided messages over frontend defaults
- **Status Code Handling:** Comprehensive handling for 400, 401, 403, 404, 409, 422, 429, 500, 502, 503, 504, and connection errors

### 👥 Enhanced User Management
- **User Deletion:** Complete user deletion functionality with confirmation modal
- **Cache Management:** Automatic cache invalidation after user operations
- **Navigation Protection:** Automatic redirection when accessing deleted users
- **Responsive UI:** Optimized button layout for desktop and mobile

### 🎨 Improved UI/UX
- **Button Organization:** Consolidated action buttons in header with responsive design
- **Modal System:** DaisyUI modals for destructive actions
- **Loading States:** Visual feedback during operations
- **Form Validation:** Enhanced client-side validation with descriptive messages

### 🔧 Architecture Improvements
- **Service Layer:** Optimized service architecture with clear separation of concerns
- **API Services:** Dedicated API services for HTTP communication
- **Error Propagation:** Consistent error handling from backend to frontend
- **State Management:** Improved state management with signals and observables

## 👥 Development Team

- **Lead Developer:** JMVELEZ
- **Start Date:** 15/01/2025

## 📝 Notes

- This is a development project
- Database can be completely recreated using scripts in `database/infrastructure/scripts/`
- Use default credentials for local development
- **Important:** Run `mvn clean install` after cloning or adding new dependencies
- **Development Credentials:**
  - Database: `tasks_app_user` / `tasks_app_user`
  - PostgreSQL Superuser: `postgres` / `jmvelez`
  - System Users: `admin_user` / `Abc123456*`

## 📊 Current Project Status

### ✅ Completed Features
- **Authentication System:** Complete JWT-based authentication
- **User Management:** Full CRUD operations with deletion
- **Error Handling:** Centralized error management system
- **UI/UX:** Responsive design with modern components
- **Database:** PostgreSQL integration with proper schemas
- **API Documentation:** Swagger/OpenAPI integration

### 🔄 In Development
- **Person Management:** Basic CRUD operations implemented
- **Employee Management:** Initial structure in place
- **Client Management:** Initial structure in place

### 📋 Planned Features
- **Task Management:** Core task functionality
- **Project Management:** Project organization features
- **Advanced Reporting:** Analytics and reporting tools
- **Real-time Updates:** WebSocket integration for live updates

### 🛠️ Technical Debt & Improvements
- **Testing Coverage:** Need to increase unit and integration tests
- **Performance Optimization:** Database query optimization
- **Security Auditing:** Regular security reviews
- **Documentation:** Continuous documentation updates

## 🔗 Quick Links

- **[API Documentation](http://localhost:3000/api/swagger-ui.html)** - Interactive API docs
- **[Frontend Application](http://localhost:4200)** - Main application
- **[Backend API](http://localhost:3000/api)** - REST API endpoint 