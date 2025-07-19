y--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Master patch application script
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: MASTER SCRIPT
-- OBJECT NAME: apply_patches
-- DESCRIPTION: Master script to apply all patches in order
--

-- =====================================================
-- PATCH APPLICATION SCRIPT
-- =====================================================
-- This script applies all patches in the correct order
-- Each patch is versioned and can be applied independently
-- =====================================================

DO $$
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'STARTING PATCH APPLICATION PROCESS';
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'Database: %', current_database();
    RAISE NOTICE 'User: %', current_user;
    RAISE NOTICE 'Timestamp: %', now();
    RAISE NOTICE '=====================================================';
END $$;

-- =====================================================
-- VERSION 1.0.0 PATCHES
-- =====================================================

-- Apply initial schema patch
\i infrastructure/patches/v1.0.0/001_initial_schema.sql

-- Apply initial authentication data patch
\i infrastructure/patches/v1.0.0/002_initial_auth_data.sql

-- Apply test data patch
\i infrastructure/patches/v1.0.0/003_test_data_employees_clients.sql

-- =====================================================
-- FUTURE PATCHES WILL BE ADDED HERE
-- =====================================================
-- Example:
-- \i infrastructure/patches/v1.1.0/004_add_new_table.sql
-- \i infrastructure/patches/v1.2.0/005_add_new_column.sql
-- \i infrastructure/patches/v2.0.0/006_major_version_update.sql

-- =====================================================
-- COMPLETION MESSAGE
-- =====================================================

DO $$
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'ALL PATCHES APPLIED SUCCESSFULLY';
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'Current database version: 1.0.0';
    RAISE NOTICE 'Applied patches:';
    RAISE NOTICE '- 001_initial_schema (v1.0.0)';
    RAISE NOTICE '- 002_initial_auth_data (v1.0.0)';
    RAISE NOTICE '- 003_test_data_employees_clients (v1.0.0)';
    RAISE NOTICE '=====================================================';
END $$; 