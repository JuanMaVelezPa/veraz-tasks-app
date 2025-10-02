--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ----------------
-- 1.0.0                      JMVELEZ         02/10/2024       Migration system with automatic tracking
-- ----------- -------------- --------------- ----------------
--
--
-- OBJECT TYPE: SCRIPT
-- OBJECT NAME: migrate
-- DESCRIPTION: Apply all pending migrations automatically
--

DO $$
DECLARE
    migration_record RECORD;
    max_applied_migration INTEGER;
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'STARTING AUTOMATIC MIGRATION PROCESS';
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'Database: %, User: %, Time: %', current_database(), current_user, now();
    
    -- Check if migration_history table exists
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'migration_history') THEN
        RAISE NOTICE 'Migration tracking table not found. Will apply first migration...';
    END IF;
    
    -- Get the highest applied migration number
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'migration_history') THEN
        SELECT COALESCE(MAX(migration_number), 0) INTO max_applied_migration 
        FROM migration_history 
        WHERE is_active = TRUE;
    ELSE
        max_applied_migration := 0;
    END IF;
    
    RAISE NOTICE 'Current migration level: %', max_applied_migration;
    RAISE NOTICE '=====================================================';
END $$;

-- Apply Migration 001: Create Migration Tracker
\echo 'Applying Migration 001: Create Migration Tracker...'
\i ../migrations/001_20241002_create_migration_tracker.sql

-- Apply Migration 002: Initial Schema
\echo 'Applying Migration 002: Initial Schema...'
\i ../migrations/002_20241002_initial_schema.sql

-- Apply Migration 003: Initial Auth Data
\echo 'Applying Migration 003: Initial Auth Data...'
\i ../migrations/003_20241002_initial_auth_data.sql

-- Apply Migration 004: Projects Table
\echo 'Applying Migration 004: Projects Table...'
\i ../migrations/004_20241002_add_projects_table.sql

-- Show final status
DO $$
DECLARE
    migration_record RECORD;
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'MIGRATION PROCESS COMPLETED';
    RAISE NOTICE '=====================================================';
    
    RAISE NOTICE 'Applied migrations:';
    FOR migration_record IN 
        SELECT migration_number, migration_name, version, applied_at 
        FROM migration_history 
        WHERE is_active = TRUE 
        ORDER BY migration_number
    LOOP
        RAISE NOTICE '  % - % (v%) - %', 
            migration_record.migration_number, 
            migration_record.migration_name, 
            migration_record.version,
            migration_record.applied_at;
    END LOOP;
    
    RAISE NOTICE '=====================================================';
END $$;