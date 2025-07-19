--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Script limpieza todas las tablas
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: CLEANUP SCRIPT
-- OBJECT NAME: 00_DROP_ALL
-- DESCRIPTION: Drop all tables in reverse dependency order
--

-- =====================================================
-- STEP 1: DROP BUSINESS TABLES FIRST
-- =====================================================

-- Drop CU_TCLIE table (has foreign keys to GE_TUSER and GE_TPERS)
DROP TABLE IF EXISTS CU_TCLIE CASCADE;

-- Drop GE_TEMPL table (has foreign keys to GE_TUSER and GE_TPERS)
DROP TABLE IF EXISTS GE_TEMPL CASCADE;

-- =====================================================
-- STEP 2: DROP RELATIONSHIP TABLES
-- =====================================================

-- Drop GE_TROPE table (has foreign keys to GE_TROLE and GE_TPERM)
DROP TABLE IF EXISTS GE_TROPE CASCADE;

-- Drop GE_TUSRO table (has foreign keys to GE_TUSER and GE_TROLE)
DROP TABLE IF EXISTS GE_TUSRO CASCADE;

-- =====================================================
-- STEP 3: DROP BASE TABLES
-- =====================================================

-- Drop GE_TPERM table (no dependencies)
DROP TABLE IF EXISTS GE_TPERM CASCADE;

-- Drop GE_TROLE table (no dependencies)
DROP TABLE IF EXISTS GE_TROLE CASCADE;

-- Drop GE_TPERS table (no dependencies)
DROP TABLE IF EXISTS GE_TPERS CASCADE;

-- Drop GE_TUSER table (no dependencies)
DROP TABLE IF EXISTS GE_TUSER CASCADE;

-- =====================================================
-- COMPLETION MESSAGE
-- =====================================================

DO $$
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'ALL TABLES DROPPED SUCCESSFULLY';
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'Dropped business tables:';
    RAISE NOTICE '- CU_TCLIE (Clients)';
    RAISE NOTICE '- GE_TEMPL (Employees)';
    RAISE NOTICE '';
    RAISE NOTICE 'Dropped relationship tables:';
    RAISE NOTICE '- GE_TROPE (Role-Permission relationships)';
    RAISE NOTICE '- GE_TUSRO (User-Role relationships)';
    RAISE NOTICE '';
    RAISE NOTICE 'Dropped base tables:';
    RAISE NOTICE '- GE_TPERM (Permissions)';
    RAISE NOTICE '- GE_TROLE (Roles)';
    RAISE NOTICE '- GE_TPERS (Persons)';
    RAISE NOTICE '- GE_TUSER (Users)';
    RAISE NOTICE '=====================================================';
END $$; 