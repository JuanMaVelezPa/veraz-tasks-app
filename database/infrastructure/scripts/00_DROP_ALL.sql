--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Drop all tables in reverse dependency order
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: SCRIPT
-- OBJECT NAME: 00_DROP_ALL
-- DESCRIPTION: Drop all tables in reverse dependency order for clean installation
--

DO $$
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'STARTING CLEANUP PROCESS';
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'Dropping all tables in reverse dependency order...';
END $$;

-- =====================================================
-- DROP BUSINESS TABLES (depend on persons)
-- =====================================================
DROP TABLE IF EXISTS clients CASCADE;
DROP TABLE IF EXISTS employees CASCADE;
DROP TABLE IF EXISTS persons CASCADE;

-- =====================================================
-- DROP RELATIONSHIP TABLES (depend on users, roles, permissions)
-- =====================================================
DROP TABLE IF EXISTS role_permissions CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;

-- =====================================================
-- DROP BASE TABLES (no dependencies)
-- =====================================================
DROP TABLE IF EXISTS permissions CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;

DO $$
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'CLEANUP COMPLETED SUCCESSFULLY';
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'All tables dropped successfully';
    RAISE NOTICE 'Database is ready for fresh installation';
    RAISE NOTICE '=====================================================';
END $$;