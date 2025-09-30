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
-- DESCRIPTION: Insert initial authentication data (roles, admin user)
--

-- Insert roles
INSERT INTO roles (name, description) VALUES
('ADMIN', 'Full control over the application and user management.'),
('MANAGER', 'Can create, edit, and assign PROJECTS and TASKS.'),
('SUPERVISOR', 'Oversees TASKS and PROJECT progress.'),
('USER', 'Can view assigned TASKS and log HOURS.'),
('CLIENT', 'Can view their own projects and tasks, submit requests.');

-- Insert admin user
INSERT INTO users (username, email, password) VALUES
('jmvelez', 'jmvelez@empresa.com', '$2a$10$sT16vxheEx5PmMt.OBfhrOk5k1hEocQeU//wb9ebsz/oZ9Z3IElx6');

-- Assign admin role to user
INSERT INTO user_roles (users_id, roles_id) VALUES
(
    (SELECT users_id FROM users WHERE username = 'jmvelez'),
    (SELECT roles_id FROM roles WHERE name = 'ADMIN')
);

DO $$
BEGIN
    RAISE NOTICE 'Authentication data inserted: 5 roles, 1 admin user';
END $$;