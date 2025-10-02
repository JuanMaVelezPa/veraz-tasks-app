--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         02/10/2024       Migration rollback system
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: SCRIPT
-- OBJECT NAME: rollback
-- DESCRIPTION: Rollback the last applied migration
--


DO $$
DECLARE
    last_migration RECORD;
    migration_count INTEGER;
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'STARTING MIGRATION ROLLBACK PROCESS';
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'Database: %, User: %, Time: %', current_database(), current_user, now();
    
    -- Check if migration_history table exists
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'migration_history') THEN
        RAISE NOTICE '❌ Migration tracking table not found';
        RAISE NOTICE '   No migrations to rollback';
        RAISE NOTICE '=====================================================';
        RETURN;
    END IF;
    
    -- Get the last applied migration
    SELECT migration_number, migration_name, version 
    INTO last_migration
    FROM migration_history 
    WHERE is_active = TRUE 
    ORDER BY migration_number DESC 
    LIMIT 1;
    
    -- Check if there are any migrations to rollback
    SELECT COUNT(*) INTO migration_count 
    FROM migration_history 
    WHERE is_active = TRUE;
    
    IF migration_count = 0 THEN
        RAISE NOTICE '❌ No migrations applied to rollback';
        RAISE NOTICE '=====================================================';
        RETURN;
    END IF;
    
    RAISE NOTICE '🔄 Rolling back migration: % - % (v%)', 
        last_migration.migration_number, 
        last_migration.migration_name, 
        last_migration.version;
    
    -- Perform rollback based on migration number
    CASE last_migration.migration_number
        WHEN 4 THEN
            RAISE NOTICE 'Rolling back: Projects table...';
            DROP TABLE IF EXISTS projects CASCADE;
        WHEN 3 THEN
            RAISE NOTICE 'Rolling back: Initial auth data...';
            -- Note: This would need specific rollback data
            RAISE NOTICE '⚠️  Manual rollback required for auth data';
        WHEN 2 THEN
            RAISE NOTICE 'Rolling back: Initial schema...';
            DROP TABLE IF EXISTS clients CASCADE;
            DROP TABLE IF EXISTS employees CASCADE;
            DROP TABLE IF EXISTS user_roles CASCADE;
            DROP TABLE IF EXISTS persons CASCADE;
            DROP TABLE IF EXISTS roles CASCADE;
            DROP TABLE IF EXISTS users CASCADE;
        WHEN 1 THEN
            RAISE NOTICE 'Rolling back: Migration tracker...';
            DROP TABLE IF EXISTS migration_history CASCADE;
        ELSE
            RAISE NOTICE '❌ Unknown migration number: %', last_migration.migration_number;
    END CASE;
    
    -- Mark migration as inactive
    UPDATE migration_history 
    SET is_active = FALSE, updated_at = CURRENT_TIMESTAMP
    WHERE migration_number = last_migration.migration_number;
    
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'ROLLBACK COMPLETED SUCCESSFULLY';
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'Migration % has been rolled back', last_migration.migration_number;
    
    -- Show remaining migrations
    RAISE NOTICE 'Remaining active migrations:';
    FOR last_migration IN 
        SELECT migration_number, migration_name, version 
        FROM migration_history 
        WHERE is_active = TRUE 
        ORDER BY migration_number
    LOOP
        RAISE NOTICE '  ✅ % - % (v%)', 
            last_migration.migration_number, 
            last_migration.migration_name, 
            last_migration.version;
    END LOOP;
    
    RAISE NOTICE '=====================================================';
END $$;
