# Database - Tasks App

PostgreSQL database with modern snake_case naming for the task management application.

## 🚀 Quick Start

```bash
# 1. Create database
createdb tasks_app_db

# 2. Setup user and extensions
cd infrastructure/scripts
.\00_setup_db_user_and_extensions.ps1

# 3. Apply schema
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f 00_DROP_ALL.sql
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f apply_patches.sql
```

## 🔧 Database Info

- **Database:** `tasks_app_db`
- **User:** `tasks_app_user`
- **Password:** `tasks_app_user`
- **Port:** 5432

## 📋 Tables Overview

| Table | Description |
|-------|-------------|
| `users` | System authentication |
| `persons` | Personal information |
| `roles` | User roles (ADMIN, MANAGER, etc.) |
| `user_roles` | User-role assignments |
| `employees` | Employment information |
| `clients` | Client information |

## 🛠️ Common Commands

```bash
# Connect to database
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user

# Create backup
pg_dump -h localhost -p 5432 -U tasks_app_user tasks_app_db > backup.sql

# Reset database (clean install)
cd infrastructure/scripts
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f 00_DROP_ALL.sql
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f apply_patches.sql
```

## 📁 Structure

```
database/
├── infrastructure/   # Setup scripts and patches
├── schema/          # Tables, constraints, indexes
├── data/            # Seed data
└── logic/           # Functions, procedures, triggers
```

## 🎯 Naming

- **Tables:** `snake_case` (e.g., `users`, `user_roles`)
- **Columns:** `snake_case` (e.g., `users_id`, `created_at`)
- **Constraints:** `{type}_{table}_{column}` (e.g., `pk_users_id`)
- **Indexes:** `idx_{table}_{columns}` (e.g., `idx_users_email`)