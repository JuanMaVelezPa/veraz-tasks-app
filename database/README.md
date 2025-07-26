# Database - Tasks App

PostgreSQL database for the task management application with comprehensive schema and data management.

## 🚀 Initial Setup

### 1. Create Database
```bash
createdb tasks_app_db
```

### 2. Setup User and Extensions
```bash
cd infrastructure/scripts
# Windows PowerShell:
.\00_setup_db_user_and_extensions.ps1
# Linux/Mac:
# psql -h localhost -p 5432 -U postgres -d tasks_app_db -f 00_setup_db_user_and_extensions.sql
```

### 3. Apply Schema
```bash
cd infrastructure/scripts
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f 00_DROP_ALL.sql && psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f apply_patches.sql
```

## 🔧 Credentials

- **Superuser:** `postgres` / `jmvelez`
- **Application User:** `tasks_app_user` / `tasks_app_user`
- **Port:** 5432
- **Database:** `tasks_app_db`

## 📁 Structure

```
database/
├── infrastructure/   # Configuration scripts
│   ├── scripts/     # Setup and utility scripts
│   └── patches/     # Database version patches
├── schema/          # Table schemas
│   ├── tables/      # Table definitions
│   ├── constraints/ # Database constraints
│   ├── indexes/     # Database indexes
│   └── views/       # Database views
├── data/            # Test data
│   └── seed/        # Initial data seeding
└── logic/           # Database logic
    ├── functions/   # Stored functions
    ├── procedures/  # Stored procedures
    ├── packages/    # PL/pgSQL packages
    └── triggers/    # Database triggers
```

## 🛠️ Useful Commands

### Recreate Database
```bash
cd infrastructure/scripts
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f 00_DROP_ALL.sql && psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f apply_patches.sql
```

### Check Status
```bash
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f check_patch_status.sql
```

### Connect to Database
```bash
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user
```

## 📋 Main Tables

### Authentication Tables
- **GE_TUSER:** System users
- **GE_TROLE:** User roles
- **GE_TPERM:** Permissions
- **GE_TROPE:** Role-permission relationships
- **GE_TUSRO:** User-role relationships

### Business Tables
- **GE_TPERS:** Persons (base table)
- **GE_TEMPL:** Employees
- **CL_TCLIE:** Clients

## 🔐 Extensions

- **pgcrypto:** Password encryption and hashing
- **uuid-ossp:** UUID generation functions

## 📊 Database Features

### Security
- **Password Hashing:** Secure password storage using pgcrypto
- **User Roles:** Role-based access control
- **Permissions:** Granular permission system
- **Audit Trail:** User activity tracking

### Performance
- **Indexes:** Optimized database indexes
- **Constraints:** Data integrity constraints
- **Foreign Keys:** Referential integrity
- **Partitioning:** Large table optimization (if needed)

## 🔄 Migration System

### Patch Management
- **Version Control:** Database changes tracked by version
- **Rollback Support:** Ability to rollback changes
- **Incremental Updates:** Apply changes incrementally
- **Status Tracking:** Track applied patches

### Applying Patches
```bash
# Apply all pending patches
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f apply_patches.sql

# Check patch status
psql -h localhost -p 5432 tasks_app_db -U tasks_app_user -f check_patch_status.sql
```

## 📈 Data Management

### Test Data
- **Initial Users:** Default admin and test users
- **Sample Data:** Test employees and clients
- **Seed Scripts:** Automated data population

### Backup and Restore
```bash
# Create backup
pg_dump -h localhost -p 5432 -U tasks_app_user tasks_app_db > backup.sql

# Restore from backup
psql -h localhost -p 5432 -U tasks_app_user tasks_app_db < backup.sql
```

## 🔧 Development Guidelines

### Naming Conventions
- **Tables:** `{SCHEMA}_{PREFIX}_{NAME}` (e.g., `GE_TUSER`)
- **Columns:** `{PREFIX}_{NAME}` (e.g., `USR_ID`, `USR_NAME`)
- **Constraints:** `{TABLE}_{TYPE}_{COLUMN}` (e.g., `GE_TUSER_PK_USR_ID`)

### Best Practices
- **Use Transactions:** Wrap changes in transactions
- **Test Patches:** Test patches before applying to production
- **Document Changes:** Document all schema changes
- **Version Control:** Keep database scripts in version control

## 🚀 Production Considerations

### Security
- **Strong Passwords:** Use strong passwords for database users
- **Network Security:** Restrict database access to application servers
- **Encryption:** Enable SSL/TLS for database connections
- **Backup Encryption:** Encrypt database backups

### Performance
- **Connection Pooling:** Configure appropriate connection pools
- **Query Optimization:** Monitor and optimize slow queries
- **Index Maintenance:** Regular index maintenance and updates
- **Vacuum Operations:** Regular VACUUM and ANALYZE operations 