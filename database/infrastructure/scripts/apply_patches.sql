--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Master patch application script
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: SCRIPT
-- OBJECT NAME: apply_patches
-- DESCRIPTION: Master script to apply all database patches in correct order
--

DO $$
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'STARTING PATCH APPLICATION PROCESS';
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'Database: %, User: %, Time: %', current_database(), current_user, now();
END $$;

-- =====================================================
-- APPLY VERSION 1.0.0 PATCHES
-- =====================================================
\i ../patches/v1.0.0/001_initial_schema.sql
\i ../patches/v1.0.0/002_initial_auth_data.sql

DO $$
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'ALL PATCHES APPLIED SUCCESSFULLY';
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'Database version: 1.0.0';
    RAISE NOTICE 'All tables, constraints, indexes and initial data created';
    RAISE NOTICE '=====================================================';
END $$;