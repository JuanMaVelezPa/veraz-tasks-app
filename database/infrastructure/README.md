# Database Infrastructure

Scripts and patches for database deployment and maintenance.

## 📁 Structure

```
infrastructure/
├── scripts/          # Setup and utility scripts
│   ├── 00_DROP_ALL.sql
│   ├── apply_patches.sql
│   └── 00_setup_db_user_and_extensions.ps1
└── patches/          # Versioned database changes
    └── v1.0.0/
        ├── 001_initial_schema.sql
        └── 002_initial_auth_data.sql
```

## 🚀 Quick Commands

```bash
# Apply all patches
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f scripts/apply_patches.sql

# Clean installation
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f scripts/00_DROP_ALL.sql
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f scripts/apply_patches.sql
```

## 📋 Current Patches

### Version 1.0.0
- **001_initial_schema**: Creates all tables, constraints, and indexes
- **002_initial_auth_data**: Inserts initial roles, permissions, and admin user

## ⚠️ Important

- Always backup before applying patches
- Test in development first
- Patches are applied in sequence order