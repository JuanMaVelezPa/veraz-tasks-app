--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Patch status verification script
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: VERIFICATION SCRIPT
-- OBJECT NAME: check_patch_status
-- DESCRIPTION: Script to verify which patches have been applied
--

-- =====================================================
-- PATCH STATUS VERIFICATION
-- =====================================================

DO $$
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'PATCH STATUS VERIFICATION';
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'Database: %', current_database();
    RAISE NOTICE 'User: %', current_user;
    RAISE NOTICE 'Timestamp: %', now();
    RAISE NOTICE '=====================================================';
END $$;

-- Check if base tables exist
DO $$
DECLARE
    table_count INTEGER;
BEGIN
    -- Check base tables
    SELECT COUNT(*) INTO table_count 
    FROM information_schema.tables 
    WHERE table_schema = 'public' 
    AND table_name IN ('ge_tuser', 'ge_tpers', 'ge_trole', 'ge_tperm');
    
    IF table_count = 4 THEN
        RAISE NOTICE '✓ Base tables exist (4/4)';
    ELSE
        RAISE NOTICE '✗ Base tables missing (%/4)', table_count;
    END IF;
    
    -- Check relationship tables
    SELECT COUNT(*) INTO table_count 
    FROM information_schema.tables 
    WHERE table_schema = 'public' 
    AND table_name IN ('ge_tusro', 'ge_trope');
    
    IF table_count = 2 THEN
        RAISE NOTICE '✓ Relationship tables exist (2/2)';
    ELSE
        RAISE NOTICE '✗ Relationship tables missing (%/2)', table_count;
    END IF;
    
    -- Check business tables
    SELECT COUNT(*) INTO table_count 
    FROM information_schema.tables 
    WHERE table_schema = 'public' 
    AND table_name IN ('ge_templ', 'cu_tclie');
    
    IF table_count = 2 THEN
        RAISE NOTICE '✓ Business tables exist (2/2)';
    ELSE
        RAISE NOTICE '✗ Business tables missing (%/2)', table_count;
    END IF;
END $$;

-- Check constraints
DO $$
DECLARE
    constraint_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO constraint_count 
    FROM information_schema.table_constraints 
    WHERE table_schema = 'public' 
    AND constraint_name LIKE 'pky_%';
    
    IF constraint_count >= 8 THEN
        RAISE NOTICE '✓ Primary key constraints exist (%/8)', constraint_count;
    ELSE
        RAISE NOTICE '✗ Primary key constraints missing (%/8)', constraint_count;
    END IF;
    
    SELECT COUNT(*) INTO constraint_count 
    FROM information_schema.table_constraints 
    WHERE table_schema = 'public' 
    AND constraint_name LIKE 'fky_%';
    
    IF constraint_count >= 6 THEN
        RAISE NOTICE '✓ Foreign key constraints exist (%/6)', constraint_count;
    ELSE
        RAISE NOTICE '✗ Foreign key constraints missing (%/6)', constraint_count;
    END IF;
END $$;

-- Check indexes
DO $$
DECLARE
    index_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO index_count 
    FROM pg_indexes 
    WHERE schemaname = 'public' 
    AND indexname LIKE 'ge_i%' OR indexname LIKE 'cu_i%';
    
    IF index_count >= 20 THEN
        RAISE NOTICE '✓ Performance indexes exist (%/20+)', index_count;
    ELSE
        RAISE NOTICE '✗ Performance indexes missing (%/20+)', index_count;
    END IF;
END $$;

-- Check initial data
DO $$
DECLARE
    role_count INTEGER;
    perm_count INTEGER;
    user_count INTEGER;
    usro_count INTEGER;
    rope_count INTEGER;
BEGIN
    -- Check roles
    SELECT COUNT(*) INTO role_count FROM GE_TROLE;
    IF role_count >= 4 THEN
        RAISE NOTICE '✓ Initial roles exist (%/5)', role_count;
    ELSE
        RAISE NOTICE '✗ Initial roles missing (%/5)', role_count;
    END IF;
    
    -- Check permissions
    SELECT COUNT(*) INTO perm_count FROM GE_TPERM;
    IF perm_count >= 14 THEN
        RAISE NOTICE '✓ Initial permissions exist (%/17)', perm_count;
    ELSE
        RAISE NOTICE '✗ Initial permissions missing (%/17)', perm_count;
    END IF;
    
    -- Check users
    SELECT COUNT(*) INTO user_count FROM GE_TUSER;
    IF user_count >= 25 THEN
        RAISE NOTICE '✓ Test users exist (%/25)', user_count;
    ELSE
        RAISE NOTICE '✗ Test users missing (%/25)', user_count;
    END IF;
    
    -- Check user-role assignments
    SELECT COUNT(*) INTO usro_count FROM GE_TUSRO;
    IF usro_count >= 25 THEN
        RAISE NOTICE '✓ User-role assignments exist (%/25)', usro_count;
    ELSE
        RAISE NOTICE '✗ User-role assignments missing (%/25)', usro_count;
    END IF;
    
    -- Check role-permission assignments
    SELECT COUNT(*) INTO rope_count FROM GE_TROPE;
    IF rope_count >= 20 THEN
        RAISE NOTICE '✓ Role-permission assignments exist (%/20+)', rope_count;
    ELSE
        RAISE NOTICE '✗ Role-permission assignments missing (%/20+)', rope_count;
    END IF;
END $$;

-- Check test data
DO $$
DECLARE
    pers_count INTEGER;
    empl_count INTEGER;
    clie_count INTEGER;
BEGIN
    -- Check persons
    SELECT COUNT(*) INTO pers_count FROM GE_TPERS;
    IF pers_count >= 24 THEN
        RAISE NOTICE '✓ Test persons exist (%/24)', pers_count;
    ELSE
        RAISE NOTICE '✗ Test persons missing (%/24)', pers_count;
    END IF;
    
    -- Check employees
    SELECT COUNT(*) INTO empl_count FROM GE_TEMPL;
    IF empl_count >= 14 THEN
        RAISE NOTICE '✓ Test employees exist (%/14)', empl_count;
    ELSE
        RAISE NOTICE '✗ Test employees missing (%/14)', empl_count;
    END IF;
    
    -- Check clients
    SELECT COUNT(*) INTO clie_count FROM CU_TCLIE;
    IF clie_count >= 10 THEN
        RAISE NOTICE '✓ Test clients exist (%/10)', clie_count;
    ELSE
        RAISE NOTICE '✗ Test clients missing (%/10)', clie_count;
    END IF;
END $$;

-- Show table details
\echo '\n=== TABLE DETAILS ==='
SELECT 
    table_name,
    CASE 
        WHEN table_name LIKE 'ge_tuser' THEN 'System Users'
        WHEN table_name LIKE 'ge_tpers' THEN 'Persons'
        WHEN table_name LIKE 'ge_trole' THEN 'Roles'
        WHEN table_name LIKE 'ge_tperm' THEN 'Permissions'
        WHEN table_name LIKE 'ge_tusro' THEN 'User-Role Relationships'
        WHEN table_name LIKE 'ge_trope' THEN 'Role-Permission Relationships'
        WHEN table_name LIKE 'ge_templ' THEN 'Employees'
        WHEN table_name LIKE 'cu_tclie' THEN 'Clients'
        ELSE 'Unknown'
    END as description
FROM information_schema.tables 
WHERE table_schema = 'public' 
AND table_name IN ('ge_tuser', 'ge_tpers', 'ge_trole', 'ge_tperm', 'ge_tusro', 'ge_trope', 'ge_templ', 'cu_tclie')
ORDER BY table_name;

\echo '\n=== CONSTRAINT SUMMARY ==='
SELECT 
    constraint_type,
    COUNT(*) as count
FROM information_schema.table_constraints 
WHERE table_schema = 'public' 
AND constraint_name LIKE 'pky_%' OR constraint_name LIKE 'fky_%' OR constraint_name LIKE 'uq_%'
GROUP BY constraint_type
ORDER BY constraint_type;

\echo '\n=== INDEX SUMMARY ==='
SELECT 
    tablename,
    COUNT(*) as index_count
FROM pg_indexes 
WHERE schemaname = 'public' 
AND (indexname LIKE 'ge_i%' OR indexname LIKE 'cu_i%')
GROUP BY tablename
ORDER BY tablename;

\echo '\n=== DATA SUMMARY ==='
SELECT 'Roles' as table_name, COUNT(*) as record_count FROM GE_TROLE
UNION ALL
SELECT 'Permissions' as table_name, COUNT(*) as record_count FROM GE_TPERM
UNION ALL
SELECT 'Users' as table_name, COUNT(*) as record_count FROM GE_TUSER
UNION ALL
SELECT 'Persons' as table_name, COUNT(*) as record_count FROM GE_TPERS
UNION ALL
SELECT 'Employees' as table_name, COUNT(*) as record_count FROM GE_TEMPL
UNION ALL
SELECT 'Clients' as table_name, COUNT(*) as record_count FROM CU_TCLIE
UNION ALL
SELECT 'User-Role Assignments' as table_name, COUNT(*) as record_count FROM GE_TUSRO
UNION ALL
SELECT 'Role-Permission Assignments' as table_name, COUNT(*) as record_count FROM GE_TROPE
ORDER BY table_name;

\echo '\n=== TEST USER CREDENTIALS ==='
SELECT 
    user_username,
    CASE 
        WHEN user_username LIKE 'manager%' THEN 'MANAGER'
        WHEN user_username LIKE 'supervisor%' THEN 'SUPERVISOR'
        WHEN user_username LIKE 'employee%' THEN 'USER'
        WHEN user_username LIKE 'client%' THEN 'CLIENT'
        ELSE 'OTHER'
    END as role_type,
    'Abc123456*' as password
FROM GE_TUSER 
WHERE user_username IN ('manager01', 'manager02', 'supervisor01', 'supervisor02', 'employee01', 'employee05', 'employee10', 'client01', 'client05', 'client10')
ORDER BY role_type, user_username;

DO $$
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'VERIFICATION COMPLETED';
    RAISE NOTICE '=====================================================';
END $$; 