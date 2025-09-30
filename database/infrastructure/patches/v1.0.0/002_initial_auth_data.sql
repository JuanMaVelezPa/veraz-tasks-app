--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Initial authentication data
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: PATCH
-- OBJECT NAME: 002_initial_auth_data
-- DESCRIPTION: Insert initial authentication data (roles, admin user)
--

DO $$
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'STARTING PATCH 002_initial_auth_data';
    RAISE NOTICE '=====================================================';
END $$; 

-- =====================================================
-- INITIAL AUTHENTICATION DATA
-- =====================================================
-- This patch inserts the initial data needed for authentication
-- Includes roles and admin user
-- =====================================================

-- Insert initial authentication data
\i ../../data/seed/00_initial_data_auth.sql

-- =====================================================
-- COMPLETION MESSAGE
-- =====================================================

DO $$
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'PATCH 002_initial_auth_data APPLIED SUCCESSFULLY';
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'Authentication system initialized with:';
    RAISE NOTICE '- 5 roles (ADMIN, MANAGER, SUPERVISOR, USER, CLIENT)';
    RAISE NOTICE '- 1 admin user (jmvelez)';
    RAISE NOTICE '- Role-based authentication';
    RAISE NOTICE '=====================================================';
END $$;