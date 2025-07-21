# Database - Tasks App

Base de datos PostgreSQL para la aplicación de gestión de tareas.

## 🚀 Configuración Inicial

### 1. Crear Base de Datos
```bash
createdb tasks_app_db
```

### 2. Configurar Usuario y Extensiones
```bash
cd infrastructure/scripts
# Windows PowerShell:
.\00_setup_db_user_and_extensions.ps1
# Linux/Mac:
# psql -h localhost -p 5432 -U postgres -d tasks_app_db -f 00_setup_db_user_and_extensions.sql
```

### 3. Aplicar Esquema
```bash
cd infrastructure/scripts
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f 00_DROP_ALL.sql && psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f apply_patches.sql
```

## 🔧 Credenciales

- **Superusuario:** `postgres` / `jmvelez`
- **Usuario aplicación:** `tasks_app_user` / `tasks_app_user`
- **Puerto:** 5432
- **Base de datos:** `tasks_app_db`

## 📁 Estructura

```
database/
├── infrastructure/   # Scripts de configuración
├── schema/          # Esquemas de tablas
├── data/            # Datos de prueba
└── logic/           # Funciones y procedimientos
```

## 🛠️ Comandos Útiles

### Recrear Base de Datos
```bash
cd infrastructure/scripts
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f 00_DROP_ALL.sql && psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f apply_patches.sql
```

### Verificar Estado
```bash
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f check_patch_status.sql
```

### Conectar a Base de Datos
```bash
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user
```

## 📋 Tablas Principales

- **GE_TUSER:** Usuarios del sistema
- **GE_TROLE:** Roles de usuario
- **GE_TPERM:** Permisos
- **GE_TPERS:** Personas
- **GE_TEMPL:** Empleados
- **CL_TCLIE:** Clientes

## 🔐 Extensiones

- **pgcrypto:** Para encriptación de contraseñas 