# Database - Tasks App

PostgreSQL database with automatic migration system for the task management application.

## 🚀 Quick Start

```bash
# 1. Setup database, user and extensions (creates everything automatically)
cd database/scripts
.\setup_database.ps1

# 2. Apply all migrations automatically
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f migrate.sql
```

## 🔧 Database Info

- **Database:** `tasks_app_db`
- **User:** `tasks_app_user`
- **Password:** `tasks_app_user`
- **Extensions:** `pgcrypto`

## 📋 Available Tables

| Table | Description |
|-------|-------------|
| `migration_history` | Tracks applied migrations |
| `users` | User accounts |
| `persons` | Personal information |
| `roles` | User roles (ADMIN, MANAGER, etc.) |
| `user_roles` | User-role assignments |
| `employees` | Employee information |
| `clients` | Client information |
| `projects` | Project data with location coordinates |

## 🛠️ Common Commands

### Check Migration Status
```bash
cd database/scripts/
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f status.sql
```

### Apply Pending Migrations
```bash
cd database/scripts/
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f migrate.sql
```

### Rollback Last Migration
```bash
cd database/scripts/
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f rollback.sql
```

### Connect to Database
```bash
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user
```

## 🧹 Clean Installation

If you have the old system installed:

```bash
# 1. Drop old database and user
psql -h localhost -p 5432 -U postgres
DROP DATABASE IF EXISTS tasks_app_db;
DROP USER IF EXISTS tasks_app_user;
\q

# 2. Setup everything
cd database/scripts
.\setup_database.ps1

# 3. Apply migrations
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f migrate.sql
```

## 📁 Database Structure

```
database/
├── migrations/                    # Migration files
│   ├── 001_20241002_create_migration_tracker.sql
│   ├── 002_20241002_initial_schema.sql
│   ├── 003_20241002_initial_auth_data.sql
│   └── 004_20241002_add_projects_table.sql
├── scripts/                       # Database scripts
│   ├── migrate.sql               # Apply pending migrations
│   ├── status.sql                # Check migration status
│   ├── rollback.sql              # Rollback last migration
│   └── setup_database.ps1        # Database setup
├── schema/                       # Table definitions
│   ├── tables/
│   ├── constraints/
│   └── indexes/
└── data/                         # Data files
    └── seed/
```

## ⚠️ Important Notes

- **Migration system is automatic** - it only applies what's missing
- **Always backup** before applying migrations
- **Use `status.sql`** to check current state
- **Use `migrate.sql`** to apply pending migrations
- **Use `rollback.sql`** to undo last migration if needed

## 🎯 Naming Conventions

- **Tables:** `snake_case` (e.g., `users`, `user_roles`)
- **Columns:** `snake_case` (e.g., `users_id`, `created_at`)
- **Constraints:** `{type}_{table}_{column}` (e.g., `pk_users_id`)
- **Indexes:** `idx_{table}_{columns}` (e.g., `idx_users_email`)
- **Primary Keys:** `{table}_id` with UUID
- **Foreign Keys:** `{referenced_table}_id`