--
-- VERSION 1.1.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.1.0                      JMVELEZ         02/10/2024       Creación tabla proyectos
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: TABLE
-- OBJECT NAME: projects
-- DESCRIPTION: Stores basic project information with client relationships
--


DROP TABLE IF EXISTS projects;
CREATE TABLE projects (
    projects_id UUID DEFAULT GEN_RANDOM_UUID(),
    clients_id UUID NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    start_date DATE,
    estimated_end_date DATE,
    real_end_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'PLANNING',
    project_type VARCHAR(50) NOT NULL,
    longitude DECIMAL(10, 8),
    latitude DECIMAL(11, 8),
    notes TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
