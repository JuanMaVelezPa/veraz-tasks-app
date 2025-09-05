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
-- OBJECT TYPE: DATA
-- OBJECT NAME: 00_initial_data_auth
-- DESCRIPTION: Insert initial authentication data (roles, permissions, admin user)
--

-- Insert roles
INSERT INTO roles (name, description) VALUES
('ADMIN', 'Full control over the application and user management.'),
('MANAGER', 'Can create, edit, and assign PROJECTS and TASKS.'),
('SUPERVISOR', 'Oversees TASKS and PROJECT progress.'),
('USER', 'Can view assigned TASKS and log HOURS.'),
('CLIENT', 'Can view their own projects and tasks, submit requests.');

-- Insert permissions
INSERT INTO permissions (name, description) VALUES
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
('VIEW_ALL_HOURS', 'Allows viewing all users hour logs.'),
('APPROVE_TASKS', 'Allows approving reported TASKS and hour logs.'),
('VIEW_OWN_PROJECTS', 'Allows viewing own projects only.'),
('VIEW_OWN_TASKS', 'Allows viewing own tasks only.'),
('SUBMIT_REQUESTS', 'Allows submitting project requests.');

-- Insert admin user
INSERT INTO users (username, email, password) VALUES
('jmvelez', 'jmvelez@empresa.com', '$2a$10$sT16vxheEx5PmMt.OBfhrOk5k1hEocQeU//wb9ebsz/oZ9Z3IElx6');

-- Assign admin role to user
INSERT INTO user_roles (users_id, roles_id) VALUES
(
    (SELECT users_id FROM users WHERE username = 'jmvelez'),
    (SELECT roles_id FROM roles WHERE name = 'ADMIN')
);

-- Assign all permissions to admin role
INSERT INTO role_permissions (roles_id, permissions_id)
SELECT
    (SELECT roles_id FROM roles WHERE name = 'ADMIN'),
    permissions_id
FROM permissions;

-- Assign permissions to manager role
INSERT INTO role_permissions (roles_id, permissions_id) VALUES
(
    (SELECT roles_id FROM roles WHERE name = 'MANAGER'),
    (SELECT permissions_id FROM permissions WHERE name = 'CREATE_PROJECT')
),
(
    (SELECT roles_id FROM roles WHERE name = 'MANAGER'),
    (SELECT permissions_id FROM permissions WHERE name = 'EDIT_PROJECT')
),
(
    (SELECT roles_id FROM roles WHERE name = 'MANAGER'),
    (SELECT permissions_id FROM permissions WHERE name = 'VIEW_PROJECT')
),
(
    (SELECT roles_id FROM roles WHERE name = 'MANAGER'),
    (SELECT permissions_id FROM permissions WHERE name = 'CREATE_TASK')
),
(
    (SELECT roles_id FROM roles WHERE name = 'MANAGER'),
    (SELECT permissions_id FROM permissions WHERE name = 'EDIT_TASK')
),
(
    (SELECT roles_id FROM roles WHERE name = 'MANAGER'),
    (SELECT permissions_id FROM permissions WHERE name = 'VIEW_TASK')
),
(
    (SELECT roles_id FROM roles WHERE name = 'MANAGER'),
    (SELECT permissions_id FROM permissions WHERE name = 'VIEW_ALL_HOURS')
),
(
    (SELECT roles_id FROM roles WHERE name = 'MANAGER'),
    (SELECT permissions_id FROM permissions WHERE name = 'APPROVE_TASKS')
);

-- Assign permissions to supervisor role
INSERT INTO role_permissions (roles_id, permissions_id) VALUES
(
    (SELECT roles_id FROM roles WHERE name = 'SUPERVISOR'),
    (SELECT permissions_id FROM permissions WHERE name = 'CREATE_TASK')
),
(
    (SELECT roles_id FROM roles WHERE name = 'SUPERVISOR'),
    (SELECT permissions_id FROM permissions WHERE name = 'EDIT_TASK')
),
(
    (SELECT roles_id FROM roles WHERE name = 'SUPERVISOR'),
    (SELECT permissions_id FROM permissions WHERE name = 'VIEW_TASK')
),
(
    (SELECT roles_id FROM roles WHERE name = 'SUPERVISOR'),
    (SELECT permissions_id FROM permissions WHERE name = 'LOG_HOURS')
),
(
    (SELECT roles_id FROM roles WHERE name = 'SUPERVISOR'),
    (SELECT permissions_id FROM permissions WHERE name = 'VIEW_ALL_HOURS')
),
(
    (SELECT roles_id FROM roles WHERE name = 'SUPERVISOR'),
    (SELECT permissions_id FROM permissions WHERE name = 'APPROVE_TASKS')
),
(
    (SELECT roles_id FROM roles WHERE name = 'SUPERVISOR'),
    (SELECT permissions_id FROM permissions WHERE name = 'VIEW_PROJECT')
);

-- Assign permissions to user role
INSERT INTO role_permissions (roles_id, permissions_id) VALUES
(
    (SELECT roles_id FROM roles WHERE name = 'USER'),
    (SELECT permissions_id FROM permissions WHERE name = 'VIEW_PROJECT')
),
(
    (SELECT roles_id FROM roles WHERE name = 'USER'),
    (SELECT permissions_id FROM permissions WHERE name = 'VIEW_TASK')
),
(
    (SELECT roles_id FROM roles WHERE name = 'USER'),
    (SELECT permissions_id FROM permissions WHERE name = 'LOG_HOURS')
);

-- Assign permissions to client role
INSERT INTO role_permissions (roles_id, permissions_id) VALUES
(
    (SELECT roles_id FROM roles WHERE name = 'CLIENT'),
    (SELECT permissions_id FROM permissions WHERE name = 'VIEW_OWN_PROJECTS')
),
(
    (SELECT roles_id FROM roles WHERE name = 'CLIENT'),
    (SELECT permissions_id FROM permissions WHERE name = 'VIEW_OWN_TASKS')
),
(
    (SELECT roles_id FROM roles WHERE name = 'CLIENT'),
    (SELECT permissions_id FROM permissions WHERE name = 'SUBMIT_REQUESTS')
);

DO $$
BEGIN
    RAISE NOTICE 'Authentication data inserted: 5 roles, 17 permissions, 1 admin user';
END $$;