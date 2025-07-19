--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Initial schema creation
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: PATCH
-- OBJECT NAME: 001_initial_schema
-- DESCRIPTION: Initial database schema with all base tables, roles, permissions and business tables
--

-- =====================================================
-- STEP 1: CREATE BASE TABLES (No dependencies)
-- =====================================================

-- Create GE_TUSER table (no dependencies)
\i schema/tables/GE_TUSER.sql

-- Create GE_TPERS table (no dependencies)
\i schema/tables/GE_TPERS.sql

-- Create GE_TROLE table (no dependencies)
\i schema/tables/GE_TROLE.sql

-- Create GE_TPERM table (no dependencies)
\i schema/tables/GE_TPERM.sql

-- =====================================================
-- STEP 2: CREATE RELATIONSHIP TABLES (With dependencies)
-- =====================================================

-- Create GE_TUSRO table (depends on GE_TUSER and GE_TROLE)
\i schema/tables/GE_TUSRO.sql

-- Create GE_TROPE table (depends on GE_TROLE and GE_TPERM)
\i schema/tables/GE_TROPE.sql

-- =====================================================
-- STEP 3: CREATE BUSINESS TABLES (With dependencies)
-- =====================================================

-- Create GE_TEMPL table (depends on GE_TUSER and GE_TPERS)
\i schema/tables/GE_TEMPL.sql

-- Create CU_TCLIE table (depends on GE_TUSER and GE_TPERS)
\i schema/tables/CU_TCLIE.sql


-- =====================================================
-- STEP 4: CREATE CONSTRAINTS
-- =====================================================

-- Add constraints to base tables
\i schema/constraints/GE_TUSER_CONSTRAINTS.sql
\i schema/constraints/GE_TPERS_CONSTRAINTS.sql
\i schema/constraints/GE_TROLE_CONSTRAINTS.sql
\i schema/constraints/GE_TPERM_CONSTRAINTS.sql

-- Add constraints to relationship tables
\i schema/constraints/GE_TUSRO_CONSTRAINTS.sql
\i schema/constraints/GE_TROPE_CONSTRAINTS.sql

-- Add constraints to business tables
\i schema/constraints/GE_TEMPL_CONSTRAINTS.sql
\i schema/constraints/CU_TCLIE_CONSTRAINTS.sql

-- =====================================================
-- STEP 5: CREATE INDEXES
-- =====================================================

-- Create indexes for base tables
\i schema/indixes/GE_TUSER_INDEXES.sql
\i schema/indixes/GE_TPERS_INDEXES.sql
\i schema/indixes/GE_TROLE_INDEXES.sql
\i schema/indixes/GE_TPERM_INDEXES.sql

-- Create indexes for relationship tables
\i schema/indixes/GE_TUSRO_INDEXES.sql
\i schema/indixes/GE_TROPE_INDEXES.sql

-- Create indexes for business tables
\i schema/indixes/GE_TEMPL_INDEXES.sql
\i schema/indixes/CU_TCLIE_INDEXES.sql

-- =====================================================
-- COMPLETION MESSAGE
-- =====================================================

DO $$
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'PATCH 001_initial_schema APPLIED SUCCESSFULLY';
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'Created base tables:';
    RAISE NOTICE '- GE_TUSER (Users)';
    RAISE NOTICE '- GE_TPERS (Persons)';
    RAISE NOTICE '- GE_TROLE (Roles)';
    RAISE NOTICE '- GE_TPERM (Permissions)';
    RAISE NOTICE '';
    RAISE NOTICE 'Created relationship tables:';
    RAISE NOTICE '- GE_TUSRO (User-Role relationships)';
    RAISE NOTICE '- GE_TROPE (Role-Permission relationships)';
    RAISE NOTICE '';
    RAISE NOTICE 'Created business tables:';
    RAISE NOTICE '- GE_TEMPL (Employees)';
    RAISE NOTICE '- CU_TCLIE (Clients)';
    RAISE NOTICE '=====================================================';
END $$; 