# Database Schema

Modern PostgreSQL schema with descriptive snake_case naming.

## 📁 Structure

```
schema/
├── tables/          # Table definitions
├── constraints/     # Database constraints  
├── indexes/         # Performance indexes
└── views/           # Database views (future)
```

## 🚀 Installation

```bash
# Complete installation
psql -d your_database -f infrastructure/scripts/apply_patches.sql

# Clean installation
psql -d your_database -f infrastructure/scripts/00_DROP_ALL.sql && psql -d your_database -f infrastructure/scripts/apply_patches.sql
```

## 📋 Table Dependencies

### Base Tables (No Dependencies)
- `users` - System authentication
- `persons` - Personal information
- `roles` - User roles
- `permissions` - System permissions

### Relationship Tables
- `user_roles` → `users` + `roles`
- `role_permissions` → `roles` + `permissions`

### Business Tables
- `employees` → `users` + `persons`
- `clients` → `users` + `persons`

## 📝 Naming Conventions

- **Tables:** `snake_case` (e.g., `user_roles`)
- **Primary Keys:** `{table}_id` (e.g., `users_id`)
- **Foreign Keys:** `{referenced_table}_id` (e.g., `users_id`)
- **Constraints:** `{type}_{table}_{column}` (e.g., `pk_users_id`)
- **Indexes:** `idx_{table}_{columns}` (e.g., `idx_users_email`)

## 🔍 Verification

```sql
-- List all tables
\dt

-- Check constraints
SELECT conname, contype, conrelid::regclass 
FROM pg_constraint 
WHERE conrelid::regclass::text IN ('users', 'persons', 'roles', 'permissions', 'user_roles', 'role_permissions', 'employees', 'clients');

-- Check indexes
SELECT indexname, tablename 
FROM pg_indexes 
WHERE tablename IN ('users', 'persons', 'roles', 'permissions', 'user_roles', 'role_permissions', 'employees', 'clients');
```

## ⚠️ Important Notes

- Always backup before running scripts
- Use transactions in production
- Verify dependency order before execution