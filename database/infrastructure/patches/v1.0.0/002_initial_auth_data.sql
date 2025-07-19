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
-- DESCRIPTION: Insert initial authentication data (roles, permissions, admin user)
--

-- =====================================================
-- INITIAL AUTHENTICATION DATA
-- =====================================================
-- This patch inserts the initial data needed for authentication
-- Includes roles, permissions, admin user and role-permission assignments
-- =====================================================

-- Insert initial authentication data
\i data/seed/00_initial_data_auth.sql

-- =====================================================
-- COMPLETION MESSAGE
-- =====================================================

DO $$
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'PATCH 002_initial_auth_data APPLIED SUCCESSFULLY';
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'Authentication system initialized with:';
    RAISE NOTICE '- 4 roles (ADMIN, MANAGER, SUPERVISOR, USER)';
    RAISE NOTICE '- 14 permissions for system operations';
    RAISE NOTICE '- 1 admin user (admin_user)';
    RAISE NOTICE '- Complete role-permission matrix';
    RAISE NOTICE '=====================================================';
END $$; 