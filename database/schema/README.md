# Database Schema Structure

## 📁 Folder Structure

```
database/schema/
├── 00_DROP_ALL.sql              # Script to drop all tables
├── 01_CREATE_TABLES.sql          # Master script to create everything
├── README.md                     # This file
├── tables/                       # Table definitions
├── constraints/                  # Table constraints
├── indixes/                      # Table indexes
├── views/                        # Views (future)
├── alter/                        # Alteration scripts (future)
└── functions/                    # Functions (future)
```

## 🚀 How to Use Scripts

### Complete Installation
To create all tables, constraints and indexes:

```bash
psql -d your_database -f database/schema/01_CREATE_TABLES.sql
```

### Complete Cleanup
To drop all tables:

```bash
psql -d your_database -f database/schema/00_DROP_ALL.sql
```

### Reinstallation
To reinstall everything from scratch:

```bash
# 1. Clean everything
psql -d your_database -f database/schema/00_DROP_ALL.sql

# 2. Create everything again
psql -d your_database -f database/schema/01_CREATE_TABLES.sql
```

## 📋 Execution Order

The master script `01_CREATE_TABLES.sql` executes files in the following order:

### 1. Create Base Tables (No Dependencies)
- `GE_TUSER.sql` - Users table
- `GE_TPERS.sql` - Persons table
- `GE_TROLE.sql` - Roles table
- `GE_TPERM.sql` - Permissions table

### 2. Create Relationship Tables (With Dependencies)
- `GE_TUSRO.sql` - User-Role relationships (depends on GE_TUSER and GE_TROLE)
- `GE_TROPE.sql` - Role-Permission relationships (depends on GE_TROLE and GE_TPERM)

### 3. Create Business Tables (With Dependencies)
- `GE_TEMPL.sql` - Employees table (depends on GE_TUSER and GE_TPERS)
- `CU_TCLIE.sql` - Clients table (depends on GE_TUSER and GE_TPERS)

### 4. Create Constraints
- Base table constraints
- Relationship table constraints
- Business table constraints

### 5. Create Indexes
- Base table indexes
- Relationship table indexes
- Business table indexes

## 📝 Naming Conventions

### Tables
- **Format**: `{MODULE}_T{4-letter-name}`
- **Examples**: `GE_TUSER`, `GE_TPERS`, `GE_TROLE`, `GE_TPERM`, `GE_TUSRO`, `GE_TROPE`, `GE_TEMPL`, `CU_TCLIE`

### Primary Keys
- **Format**: `{4-letter_prefix}_{4-letter_prefix}`
- **Examples**: `user_user`, `pers_pers`, `role_role`, `perm_perm`, `usro_usro`, `rope_rope`, `empl_empl`, `clie_clie`

### Constraints
- **Primary Key**: `PKY_{TABLE}_{4-letter_prefix}`
- **Foreign Key**: `FKY_{TABLE}_{4-letter_prefix}`
- **Unique**: `UQ_{TABLE}_{4-letter_prefix}`
- **Check**: `CK_{TABLE}_{4-letter_prefix}`

### Indexes
- **Format**: `{MODULE}_I{4-letter-table}_{columns}`

## 🛠️ Maintenance

### Add New Table
1. Create file in `tables/`
2. Create file in `constraints/`
3. Create file in `indixes/`
4. Update `01_CREATE_TABLES.sql`

### Modify Existing Table
1. Update file in `tables/`
2. Update file in `constraints/` if necessary
3. Update file in `indixes/` if necessary

### Delete Table
1. Delete corresponding files
2. Update `01_CREATE_TABLES.sql`
3. Update `00_DROP_ALL.sql`

## ⚠️ Important Notes

- **Always use `DROP TABLE IF EXISTS`** to avoid errors
- **Use `CASCADE`** in cleanup script to remove dependencies
- **Verify dependency order** before executing
- **Make backup** before running cleanup scripts
- **Use transactions** in production environments

## 🔍 Verification

After running the master script, you can verify everything was created correctly:

```sql
-- Verify created tables
\dt GE_*
\dt CU_*

-- Verify constraints
SELECT conname, contype, conrelid::regclass 
FROM pg_constraint 
WHERE conrelid::regclass::text LIKE 'GE_%' OR conrelid::regclass::text LIKE 'CU_%';

-- Verify indexes
SELECT indexname, tablename 
FROM pg_indexes 
WHERE tablename LIKE 'GE_%' OR tablename LIKE 'CU_%';
```

## 📊 Table Summary

### Base Tables (No Dependencies)
- **GE_TUSER**: System users
- **GE_TPERS**: Complete person information
- **GE_TROLE**: System roles
- **GE_TPERM**: System permissions

### Relationship Tables
- **GE_TUSRO**: User-role assignments
- **GE_TROPE**: Role-permission assignments

### Business Tables
- **GE_TEMPL**: Employee information
- **CU_TCLIE**: Client information 