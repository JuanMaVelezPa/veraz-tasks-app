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