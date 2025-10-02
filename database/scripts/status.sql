--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         02/10/2024       Migration status checker
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: SCRIPT
-- OBJECT NAME: status
-- DESCRIPTION: Show current migration status and available migrations
--


DO $$
DECLARE
    migration_record RECORD;
    total_migrations INTEGER := 4;
    applied_migrations INTEGER;
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'MIGRATION STATUS REPORT';
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'Database: %, User: %, Time: %', current_database(), current_user, now();
    
    -- Check if migration_history table exists
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'migration_history') THEN
        RAISE NOTICE '❌ Migration tracking table not found';
        RAISE NOTICE '   Run: \i migrate.sql to initialize migration system';
        RAISE NOTICE '=====================================================';
        RETURN;
    END IF;
    
    -- Count applied migrations
    SELECT COUNT(*) INTO applied_migrations 
    FROM migration_history 
    WHERE is_active = TRUE;
    
    RAISE NOTICE '📊 Migration Summary:';
    RAISE NOTICE '   Applied: %/%', applied_migrations, total_migrations;
    RAISE NOTICE '   Status: %', 
        CASE 
            WHEN applied_migrations = total_migrations THEN '✅ UP TO DATE'
            WHEN applied_migrations = 0 THEN '❌ NO MIGRATIONS APPLIED'
            ELSE '⚠️  PENDING MIGRATIONS'
        END;
    
    RAISE NOTICE '';
    RAISE NOTICE '📋 Applied Migrations:';
    
    IF applied_migrations = 0 THEN
        RAISE NOTICE '   No migrations applied yet';
    ELSE
        FOR migration_record IN 
            SELECT migration_number, migration_name, version, applied_at 
            FROM migration_history 
            WHERE is_active = TRUE 
            ORDER BY migration_number
        LOOP
            RAISE NOTICE '   ✅ % - % (v%) - %', 
                migration_record.migration_number, 
                migration_record.migration_name, 
                migration_record.version,
                migration_record.applied_at;
        END LOOP;
    END IF;
    
    RAISE NOTICE '';
    RAISE NOTICE '📋 Available Migrations:';
    RAISE NOTICE '   001 - Create migration tracker (v1.0.0)';
    RAISE NOTICE '   002 - Initial schema (v1.0.0)';
    RAISE NOTICE '   003 - Initial auth data (v1.0.0)';
    RAISE NOTICE '   004 - Add projects table (v1.1.0)';
    
    RAISE NOTICE '';
    RAISE NOTICE '🔧 Commands:';
    RAISE NOTICE '   Apply pending: \i migrate.sql';
    RAISE NOTICE '   Check status:  \i status.sql';
    
    RAISE NOTICE '=====================================================';
END $$;
