--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Test data for employees and clients
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: PATCH
-- OBJECT NAME: 003_test_data_employees_clients
-- DESCRIPTION: Insert test data for persons, employees, clients and their users
--

-- =====================================================
-- TEST DATA FOR EMPLOYEES AND CLIENTS
-- =====================================================
-- This patch inserts test data including:
-- - 2 managers, 2 supervisors, 10 employees, 10 clients
-- - All users with password: Abc123456*
-- - All employees with salary in CAD currency
-- - All data for Canada (CAD currency)
-- =====================================================

DO $$
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'STARTING PATCH 003_test_data_employees_clients';
    RAISE NOTICE '=====================================================';
END $$; 

-- Insert test data
\i ../../data/seed/03_test_data_employees_clients.sql

-- =====================================================
-- COMPLETION MESSAGE
-- =====================================================

DO $$
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'PATCH 003_test_data_employees_clients APPLIED SUCCESSFULLY';
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'Test data inserted:';
    RAISE NOTICE '- 2 managers (manager01, manager02)';
    RAISE NOTICE '- 2 supervisors (supervisor01, supervisor02)';
    RAISE NOTICE '- 10 employees (employee01-10)';
    RAISE NOTICE '- 10 clients (client01-10)';
    RAISE NOTICE '';
    RAISE NOTICE 'User credentials:';
    RAISE NOTICE '- Username: [role][number] (e.g., manager01, employee05)';
    RAISE NOTICE '- Password: Abc123456* (for all users)';
    RAISE NOTICE '';
    RAISE NOTICE 'Employee details:';
    RAISE NOTICE '- All employees have salary in CAD currency';
    RAISE NOTICE '- Various construction-related positions';
    RAISE NOTICE '- All based in Toronto, Canada';
    RAISE NOTICE '=====================================================';
END $$; 