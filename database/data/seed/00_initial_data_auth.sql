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
-- OBJECT TYPE: SEED DATA
-- OBJECT NAME: 00_initial_data_auth
-- DESCRIPTION: Initial data for authentication tables (roles, permissions, users)
--

-- =====================================================
-- STEP 1: INSERT ROLES
-- =====================================================

-- Insert Roles into GE_TROLE
INSERT INTO GE_TROLE (role_name, role_desc) VALUES
('ADMIN', 'Full control over the application and user management.'),
('MANAGER', 'Can create, edit, and assign PROJECTS and TASKS.'),
('SUPERVISOR', 'Oversees TASKS and PROJECT progress.'),
('USER', 'Can view assigned TASKS and log HOURS.'),
('CLIENT', 'Can view their own projects and tasks, submit requests.');

-- =====================================================
-- STEP 2: INSERT PERMISSIONS
-- =====================================================

-- Insert Permissions into GE_TPERM
INSERT INTO GE_TPERM (perm_name, perm_descr) VALUES
('MANAGE_USERS', 'Allows creating, editing, and deleting users.'),
('MANAGE_ROLES', 'Allows creating, editing, and deleting roles.'),
('MANAGE_PERMISSIONS', 'Allows managing permissions.'),
('CREATE_PROJECT', 'Allows creating new PROJECTS.'),
('EDIT_PROJECT', 'Allows editing existing PROJECTS.'),
('VIEW_PROJECT', 'Allows viewing PROJECT details.'),
('DELETE_PROJECT', 'Allows deleting PROJECTS.'),
('CREATE_TASK', 'Allows creating new TASKS.'),
('EDIT_TASK', 'Allows editing existing TASKS.'),
('VIEW_TASK', 'Allows viewing TASK details.'),
('DELETE_TASK', 'Allows deleting TASKS.'),
('LOG_HOURS', 'Allows logging HOURS on TASKS.'),
('VIEW_ALL_HOURS', 'Allows viewing all users'' hour logs.'),
('APPROVE_TASKS', 'Allows approving reported TASKS and hour logs.'),
('VIEW_OWN_PROJECTS', 'Allows viewing own projects only.'),
('VIEW_OWN_TASKS', 'Allows viewing own tasks only.'),
('SUBMIT_REQUESTS', 'Allows submitting project requests.');

-- =====================================================
-- STEP 3: INSERT EXAMPLE USER
-- =====================================================

-- Insert Example User into GE_TUSER
-- Note: Replace 'password_hashed_here' with the actual hashed password
INSERT INTO GE_TUSER (user_username, user_email, user_pwd) VALUES
('admin_user', 'admin@empresa.com', '$2a$10$T9WY1lL.AGTy14qvfqfTMe45YjO6IiDyTLrAMhylgGWni3CP2t8Mi');

-- =====================================================
-- STEP 4: ASSIGN ROLES TO USERS
-- =====================================================

-- Assign the 'ADMIN' role to the 'admin_user'
INSERT INTO GE_TUSRO (usro_user, usro_role) VALUES
(
    (SELECT user_user FROM GE_TUSER WHERE user_username = 'admin_user'),
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'ADMIN')
);

-- =====================================================
-- STEP 5: ASSIGN PERMISSIONS TO ROLES
-- =====================================================

-- Assign all permissions to the 'ADMIN' role
INSERT INTO GE_TROPE (rope_role, rope_perm)
SELECT
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'ADMIN'),
    perm_perm
FROM GE_TPERM;

-- Assign permissions to the 'MANAGER' role
INSERT INTO GE_TROPE (rope_role, rope_perm) VALUES
(
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'MANAGER'),
    (SELECT perm_perm FROM GE_TPERM WHERE perm_name = 'CREATE_PROJECT')
),
(
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'MANAGER'),
    (SELECT perm_perm FROM GE_TPERM WHERE perm_name = 'EDIT_PROJECT')
),
(
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'MANAGER'),
    (SELECT perm_perm FROM GE_TPERM WHERE perm_name = 'VIEW_PROJECT')
),
(
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'MANAGER'),
    (SELECT perm_perm FROM GE_TPERM WHERE perm_name = 'CREATE_TASK')
),
(
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'MANAGER'),
    (SELECT perm_perm FROM GE_TPERM WHERE perm_name = 'EDIT_TASK')
),
(
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'MANAGER'),
    (SELECT perm_perm FROM GE_TPERM WHERE perm_name = 'VIEW_TASK')
),
(
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'MANAGER'),
    (SELECT perm_perm FROM GE_TPERM WHERE perm_name = 'VIEW_ALL_HOURS')
),
(
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'MANAGER'),
    (SELECT perm_perm FROM GE_TPERM WHERE perm_name = 'APPROVE_TASKS')
);

-- Assign permissions to the 'SUPERVISOR' role
INSERT INTO GE_TROPE (rope_role, rope_perm) VALUES
(
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'SUPERVISOR'),
    (SELECT perm_perm FROM GE_TPERM WHERE perm_name = 'CREATE_TASK')
),
(
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'SUPERVISOR'),
    (SELECT perm_perm FROM GE_TPERM WHERE perm_name = 'EDIT_TASK')
),
(
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'SUPERVISOR'),
    (SELECT perm_perm FROM GE_TPERM WHERE perm_name = 'VIEW_TASK')
),
(
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'SUPERVISOR'),
    (SELECT perm_perm FROM GE_TPERM WHERE perm_name = 'LOG_HOURS')
),
(
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'SUPERVISOR'),
    (SELECT perm_perm FROM GE_TPERM WHERE perm_name = 'VIEW_ALL_HOURS')
),
(
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'SUPERVISOR'),
    (SELECT perm_perm FROM GE_TPERM WHERE perm_name = 'APPROVE_TASKS')
),
(
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'SUPERVISOR'),
    (SELECT perm_perm FROM GE_TPERM WHERE perm_name = 'VIEW_PROJECT')
);

-- Assign permissions to the 'USER' role
INSERT INTO GE_TROPE (rope_role, rope_perm) VALUES
(
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'USER'),
    (SELECT perm_perm FROM GE_TPERM WHERE perm_name = 'VIEW_PROJECT')
),
(
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'USER'),
    (SELECT perm_perm FROM GE_TPERM WHERE perm_name = 'VIEW_TASK')
),
(
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'USER'),
    (SELECT perm_perm FROM GE_TPERM WHERE perm_name = 'LOG_HOURS')
);

-- Assign permissions to the 'CLIENT' role
INSERT INTO GE_TROPE (rope_role, rope_perm) VALUES
(
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'CLIENT'),
    (SELECT perm_perm FROM GE_TPERM WHERE perm_name = 'VIEW_OWN_PROJECTS')
),
(
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'CLIENT'),
    (SELECT perm_perm FROM GE_TPERM WHERE perm_name = 'VIEW_OWN_TASKS')
),
(
    (SELECT role_role FROM GE_TROLE WHERE role_name = 'CLIENT'),
    (SELECT perm_perm FROM GE_TPERM WHERE perm_name = 'SUBMIT_REQUESTS')
);

-- =====================================================
-- COMPLETION MESSAGE
-- =====================================================

DO $$
BEGIN
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'INITIAL AUTHENTICATION DATA INSERTED SUCCESSFULLY';
    RAISE NOTICE '=====================================================';
    RAISE NOTICE 'Created roles:';
    RAISE NOTICE '- ADMIN (Full control)';
    RAISE NOTICE '- MANAGER (Project and task management)';
    RAISE NOTICE '- SUPERVISOR (Task oversight)';
    RAISE NOTICE '- USER (Basic access)';
    RAISE NOTICE '- CLIENT (Client access)';
    RAISE NOTICE '';
    RAISE NOTICE 'Created permissions:';
    RAISE NOTICE '- 17 system permissions';
    RAISE NOTICE '';
    RAISE NOTICE 'Created user:';
    RAISE NOTICE '- admin_user (admin@empresa.com) with ADMIN role';
    RAISE NOTICE '';
    RAISE NOTICE 'Role-permission assignments completed';
    RAISE NOTICE '=====================================================';
END $$;