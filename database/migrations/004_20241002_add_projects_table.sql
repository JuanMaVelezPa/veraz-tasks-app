--
-- MIGRATION: 004_20241002_add_projects_table
-- VERSION: 1.1.0
-- DATE: 02/10/2024
-- DESCRIPTION: Add projects table with location coordinates support
--


\echo '====================================================='
\echo 'APPLYING MIGRATION: 004_20241002_add_projects_table'
\echo '====================================================='

-- Create projects table
\i ../schema/tables/projects.sql
\i ../schema/constraints/projects_constraints.sql
\i ../schema/indixes/projects_indexes.sql

-- Insert this migration record
INSERT INTO migration_history (version, migration_number, migration_name, checksum) 
VALUES ('1.1.0', 4, '004_20241002_add_projects_table', 'projects_table_v1');

\echo '====================================================='
\echo 'MIGRATION 004 APPLIED SUCCESSFULLY'
\echo 'Projects table created with location coordinates support'
\echo '====================================================='
