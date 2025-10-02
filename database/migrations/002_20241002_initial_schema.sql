--
-- MIGRATION: 002_20241002_initial_schema
-- VERSION: 1.0.0
-- DATE: 02/10/2024
-- DESCRIPTION: Create initial database schema (6 base tables)
--


\echo '====================================================='
\echo 'APPLYING MIGRATION: 002_20241002_initial_schema'
\echo '====================================================='

-- Create base tables
\i ../schema/tables/users.sql
\i ../schema/tables/persons.sql
\i ../schema/tables/roles.sql
\i ../schema/tables/user_roles.sql
\i ../schema/tables/employees.sql
\i ../schema/tables/clients.sql

-- Add constraints
\i ../schema/constraints/users_constraints.sql
\i ../schema/constraints/persons_constraints.sql
\i ../schema/constraints/roles_constraints.sql
\i ../schema/constraints/user_roles_constraints.sql
\i ../schema/constraints/employees_constraints.sql
\i ../schema/constraints/clients_constraints.sql

-- Add indexes
\i ../schema/indixes/users_indexes.sql
\i ../schema/indixes/persons_indexes.sql
\i ../schema/indixes/roles_indexes.sql
\i ../schema/indixes/user_roles_indexes.sql
\i ../schema/indixes/employees_indexes.sql
\i ../schema/indixes/clients_indexes.sql

-- Insert this migration record
INSERT INTO migration_history (version, migration_number, migration_name, checksum) 
VALUES ('1.0.0', 2, '002_20241002_initial_schema', 'initial_schema_v1');

\echo '====================================================='
\echo 'MIGRATION 002 APPLIED SUCCESSFULLY'
\echo 'Initial schema created: 6 tables (users, persons, roles, user_roles, employees, clients)'
\echo '====================================================='
