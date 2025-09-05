--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Initial database schema creation
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: PATCH
-- OBJECT NAME: 001_initial_schema
-- DESCRIPTION: Create initial database schema with all tables, constraints and indexes
--

DO $$
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'STARTING PATCH 001_initial_schema';
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'Creating initial database schema...';
END $$; 

-- =====================================================
-- CREATE BASE TABLES
-- =====================================================
\i ../../schema/tables/users.sql
\i ../../schema/tables/persons.sql
\i ../../schema/tables/roles.sql
\i ../../schema/tables/permissions.sql

-- =====================================================
-- CREATE RELATIONSHIP TABLES
-- =====================================================
\i ../../schema/tables/user_roles.sql
\i ../../schema/tables/role_permissions.sql

-- =====================================================
-- CREATE BUSINESS TABLES
-- =====================================================
\i ../../schema/tables/employees.sql
\i ../../schema/tables/clients.sql

-- =====================================================
-- ADD CONSTRAINTS
-- =====================================================
\i ../../schema/constraints/users_constraints.sql
\i ../../schema/constraints/persons_constraints.sql
\i ../../schema/constraints/roles_constraints.sql
\i ../../schema/constraints/permissions_constraints.sql
\i ../../schema/constraints/user_roles_constraints.sql
\i ../../schema/constraints/role_permissions_constraints.sql
\i ../../schema/constraints/employees_constraints.sql
\i ../../schema/constraints/clients_constraints.sql

-- =====================================================
-- ADD INDEXES
-- =====================================================
\i ../../schema/indixes/users_indexes.sql
\i ../../schema/indixes/persons_indexes.sql
\i ../../schema/indixes/roles_indexes.sql
\i ../../schema/indixes/permissions_indexes.sql
\i ../../schema/indixes/user_roles_indexes.sql
\i ../../schema/indixes/role_permissions_indexes.sql
\i ../../schema/indixes/employees_indexes.sql
\i ../../schema/indixes/clients_indexes.sql

DO $$
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'PATCH 001_initial_schema APPLIED SUCCESSFULLY';
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'Schema created successfully:';
    RAISE NOTICE '- 8 tables: users, persons, roles, permissions, user_roles, role_permissions, employees, clients';
    RAISE NOTICE '- All constraints and indexes applied';
    RAISE NOTICE '=====================================================';
END $$;