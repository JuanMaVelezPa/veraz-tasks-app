--
-- MIGRATION: 003_20241002_initial_auth_data
-- VERSION: 1.0.0
-- DATE: 02/10/2024
-- DESCRIPTION: Insert initial authentication data (roles and admin user)
--


\echo '====================================================='
\echo 'APPLYING MIGRATION: 003_20241002_initial_auth_data'
\echo '====================================================='

-- Insert initial auth data
\i ../data/seed/00_initial_data_auth.sql

-- Insert this migration record
INSERT INTO migration_history (version, migration_number, migration_name, checksum) 
VALUES ('1.0.0', 3, '003_20241002_initial_auth_data', 'initial_auth_data_v1');

\echo '====================================================='
\echo 'MIGRATION 003 APPLIED SUCCESSFULLY'
\echo 'Initial authentication data inserted'
\echo '====================================================='
