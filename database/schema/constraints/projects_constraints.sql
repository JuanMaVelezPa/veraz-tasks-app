--
-- VERSION 1.1.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.1.0                      JMVELEZ         02/10/2024       Creación constraints tabla proyectos
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: CONSTRAINT
-- OBJECT NAME: projects_constraints
-- DESCRIPTION: Constraints for projects table
--


-- Primary Key Constraint
ALTER TABLE projects ADD CONSTRAINT pk_projects_id PRIMARY KEY (projects_id);

-- Foreign Key Constraints
ALTER TABLE projects ADD CONSTRAINT fk_projects_clients 
    FOREIGN KEY (clients_id) REFERENCES clients(clients_id) ON DELETE RESTRICT;

-- Check Constraints
ALTER TABLE projects ADD CONSTRAINT ck_projects_status 
    CHECK (status IN ('PLANNING', 'ACTIVE', 'ON_HOLD', 'COMPLETED', 'CANCELLED', 'SUSPENDED'));

ALTER TABLE projects ADD CONSTRAINT ck_projects_dates 
    CHECK (
        start_date IS NULL OR 
        estimated_end_date IS NULL OR 
        start_date <= estimated_end_date
    );

ALTER TABLE projects ADD CONSTRAINT ck_projects_real_end_date 
    CHECK (
        real_end_date IS NULL OR 
        start_date IS NULL OR 
        start_date <= real_end_date
    );

-- Location Constraints
ALTER TABLE projects ADD CONSTRAINT ck_projects_longitude 
    CHECK (longitude IS NULL OR (longitude >= -180 AND longitude <= 180));

ALTER TABLE projects ADD CONSTRAINT ck_projects_latitude 
    CHECK (latitude IS NULL OR (latitude >= -90 AND latitude <= 90));

-- Not Null Constraints
ALTER TABLE projects ALTER COLUMN name SET NOT NULL;
ALTER TABLE projects ALTER COLUMN clients_id SET NOT NULL;
ALTER TABLE projects ALTER COLUMN project_type SET NOT NULL;
ALTER TABLE projects ALTER COLUMN status SET NOT NULL;
