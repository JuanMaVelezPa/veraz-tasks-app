--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación constraints tabla usuarios-roles
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: CONSTRAINTS
-- OBJECT NAME: user_roles_constraints
-- DESCRIPTION: Primary key, foreign keys and unique constraints for user_roles table
--

ALTER TABLE user_roles ADD CONSTRAINT pk_user_roles_id PRIMARY KEY (user_roles_id);
ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles_users FOREIGN KEY (users_id) REFERENCES users(users_id) ON DELETE CASCADE;
ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles_roles FOREIGN KEY (roles_id) REFERENCES roles(roles_id) ON DELETE CASCADE;
ALTER TABLE user_roles ADD CONSTRAINT uq_user_roles_user_role UNIQUE (users_id, roles_id);