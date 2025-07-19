# Tasks App

Aplicación de gestión de tareas con arquitectura backend en Spring Boot y base de datos PostgreSQL.

## 📁 Estructura del Proyecto

```
tasks-app/
├── backend/          # API REST con Spring Boot
├── database/         # Scripts y esquemas de base de datos
└── README.md         # Este archivo
```

## 🚀 Inicio Rápido

### Prerrequisitos

- Java 21 o superior
- PostgreSQL 17 o superior
- Maven 3.6 o superior

### Configuración de Base de Datos

1. **Crear la base de datos:**
   ```bash
   createdb tasks_app_db
   ```

2. **Configurar usuario y extensiones (ejecutar como superusuario postgres):**
   ```bash
   cd database/infrastructure/scripts
   # En Windows PowerShell:
   .\00_setup_db_user_and_extensions.ps1
   # En Linux/Mac:
   # psql -h localhost -p 5432 -U postgres -d tasks_app_db -f 00_setup_db_user_and_extensions.sql
   ```

3. **Aplicar el esquema inicial:**
   ```bash
   cd database/infrastructure/scripts
   psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f 00_DROP_ALL.sql && psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f apply_patches.sql
   ```

**Nota:** Las credenciales por defecto para desarrollo son:
- **Superusuario PostgreSQL:** `postgres` / `jmvelez`
- **Usuario base de datos:** `tasks_app_user` / `tasks_app_user`
- **Usuarios del sistema (login):** `admin` / `Abc123456*` (y otros usuarios creados por defecto)

### Ejecutar el Backend

1. **Navegar al directorio backend:**
   ```bash
   cd backend
   ```

2. **Instalar dependencias (solo la primera vez o cuando se agreguen nuevas):**
   ```bash
   mvn clean install
   ```

3. **Ejecutar la aplicación:**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Verificar que esté funcionando:**
   - API disponible en: http://localhost:3000/api
   - Documentación Swagger: http://localhost:3000/api/swagger-ui.html

## 📚 Documentación Detallada

- **[Backend Documentation](backend/README.md)** - Documentación completa del API REST
- **[Database Documentation](database/README.md)** - Guías de base de datos y scripts

## 🔧 Tecnologías

- **Backend:** Spring Boot 3.5.3, Spring Security, JWT
- **Base de Datos:** PostgreSQL 17
- **Java:** 21
- **Build Tool:** Maven 3.6+
- **Documentación:** Swagger/OpenAPI 2.8.9
- **Puerto API:** 3000

## 👥 Equipo de Desarrollo

- **Desarrollador Principal:** JMVELEZ
- **Fecha de Inicio:** 15/01/2025

## 📝 Notas

- Este es un proyecto en desarrollo
- La base de datos se puede recrear completamente usando los scripts en `database/infrastructure/scripts/`
- Para desarrollo local, usar las credenciales por defecto configuradas en los scripts
- **Importante:** Ejecutar `mvn clean install` después de clonar el proyecto o cuando se agreguen nuevas dependencias
- **Credenciales de desarrollo:**
  - Base de datos: `tasks_app_user` / `tasks_app_user`
  - Superusuario PostgreSQL: `postgres` / `jmvelez`
  - Usuarios del sistema: `admin` / `Abc123456*` 