# Database Infrastructure

## 📁 Folder Structure

```
database/infrastructure/
├── README.md                     # This file
├── scripts/                      # Master scripts
│   ├── 00_DROP_ALL.sql          # Drop all tables
│   ├── apply_patches.sql         # Master patch application script
│   └── check_patch_status.sql    # Verify patch status
└── patches/                      # Versioned patches
    └── v1.0.0/                   # Version 1.0.0 patches
        ├── 001_initial_schema.sql # Initial schema creation
        └── 002_initial_auth_data.sql # Initial authentication data
```

## 🚀 How to Use

### Apply All Patches (Recommended)
```bash
# From database/ folder
psql -d your_database -U your_user -f infrastructure/scripts/apply_patches.sql
```

### Check Patch Status
```bash
# From database/ folder
psql -d your_database -U your_user -f infrastructure/scripts/check_patch_status.sql
```

### Drop All Tables (Cleanup)
```bash
# From database/ folder
psql -d your_database -U your_user -f infrastructure/scripts/00_DROP_ALL.sql
```

## 📋 Patch System

### What are Patches?
Patches are versioned database changes that can be applied incrementally. Each patch:
- Has a unique version number
- Can be applied independently
- Is tracked and documented
- Can be rolled back if needed

### Patch Naming Convention
```
patches/v{Major}.{Minor}.{Patch}/{Sequence}_{Description}.sql
```

**Examples:**
- `v1.0.0/001_initial_schema.sql`
- `v1.0.0/002_initial_auth_data.sql`
- `v1.1.0/003_add_new_table.sql`
- `v1.2.0/004_add_new_column.sql`
- `v2.0.0/005_major_version_update.sql`

### Version Numbering
- **Major**: Breaking changes, major refactoring
- **Minor**: New features, new tables, new columns
- **Patch**: Bug fixes, index optimizations, constraint updates

### Sequence Numbers
- **001**: Initial schema
- **002**: Initial data
- **003**: First modification
- **004**: Second modification
- etc.

## 🛠️ Creating New Patches

### 1. Create Patch File
Create a new file in the appropriate version folder:
```bash
# From database/ folder
mkdir -p infrastructure/patches/v1.1.0
touch infrastructure/patches/v1.1.0/003_add_new_table.sql
```

### 2. Write Patch Content
```sql
--
-- VERSION 1.1.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.1.0                      JMVELEZ         15/01/2025       Add new business table
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: PATCH
-- OBJECT NAME: 003_add_new_table
-- DESCRIPTION: Add new business table for orders
--

-- Create new table
\i schema/tables/GE_TORD.sql

-- Add constraints
\i schema/constraints/GE_TORD_CONSTRAINTS.sql

-- Add indexes
\i schema/indixes/GE_TORD_INDEXES.sql

-- Completion message
DO $$
BEGIN
    RAISE NOTICE 'PATCH 003_add_new_table APPLIED SUCCESSFULLY';
END $$;
```

### 3. Update Master Script
Add the patch to `apply_patches.sql`:
```sql
-- =====================================================
-- VERSION 1.1.0 PATCHES
-- =====================================================

-- Apply new table patch
\i infrastructure/patches/v1.1.0/003_add_new_table.sql
```

## 📊 Current Patches

### Version 1.0.0
- **001_initial_schema**: Initial database schema with all base tables, roles, permissions and business tables
- **002_initial_auth_data**: Initial authentication data including roles, permissions, admin user and role-permission assignments

## 🔍 Verification

### Check Applied Patches
```bash
# From database/ folder
psql -d your_database -U your_user -f infrastructure/scripts/check_patch_status.sql
```

### Manual Verification
```sql
-- Check tables
\dt GE_*
\dt CL_*

-- Check constraints
SELECT conname, contype, conrelid::regclass 
FROM pg_constraint 
WHERE conrelid::regclass::text LIKE 'GE_%' OR conrelid::regclass::text LIKE 'CL_%';

-- Check indexes
SELECT indexname, tablename 
FROM pg_indexes 
WHERE tablename LIKE 'GE_%' OR tablename LIKE 'CL_%';

-- Check initial data
SELECT 'Roles' as table_name, COUNT(*) as record_count FROM GE_TROLE
UNION ALL
SELECT 'Permissions' as table_name, COUNT(*) as record_count FROM GE_TPERM
UNION ALL
SELECT 'Users' as table_name, COUNT(*) as record_count FROM GE_TUSER;
```

## ⚠️ Important Notes

### Before Applying Patches
- **Always backup** your database
- **Test patches** in development environment first
- **Check dependencies** between patches
- **Verify patch order** in master script

### Patch Best Practices
- **One change per patch**: Keep patches focused and small
- **Document changes**: Always include clear descriptions
- **Test thoroughly**: Verify patches work correctly
- **Version control**: Commit patches to version control
- **Rollback plan**: Have a plan to rollback if needed

### Production Deployment
- **Schedule maintenance window** for major patches
- **Monitor performance** after applying patches
- **Have rollback scripts** ready
- **Test in staging** environment first

## 🔄 Migration Workflow

### Development
1. Create new patch file
2. Write patch content
3. Test patch locally
4. Update master script
5. Commit to version control

### Staging
1. Apply patches to staging database
2. Run verification scripts
3. Test application functionality
4. Fix any issues found

### Production
1. Schedule maintenance window
2. Backup production database
3. Apply patches
4. Run verification scripts
5. Monitor application performance
6. Update documentation

## 📚 Related Documentation

- [Database Guidelines](../DATABASE_GUIDELINES.md)
- [Schema Documentation](../schema/README.md)
- [Data Seeding](../data/README.md) 