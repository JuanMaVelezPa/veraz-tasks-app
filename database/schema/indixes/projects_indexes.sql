--
-- VERSION 1.1.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.1.0                      JMVELEZ         02/10/2024       Creación índices tabla proyectos
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: INDEX
-- OBJECT NAME: projects_indexes
-- DESCRIPTION: Indexes for projects table to optimize query performance
--


-- Index on foreign key for efficient joins
CREATE INDEX idx_projects_clients_id ON projects (clients_id);

-- Index on status for filtering projects
CREATE INDEX idx_projects_status ON projects (status);

-- Index on project type for categorization queries
CREATE INDEX idx_projects_type ON projects (project_type);

-- Index on start date for date range queries
CREATE INDEX idx_projects_start_date ON projects (start_date);

-- Index on estimated end date for deadline tracking
CREATE INDEX idx_projects_estimated_end_date ON projects (estimated_end_date);

-- Index on real end date for completion tracking
CREATE INDEX idx_projects_real_end_date ON projects (real_end_date);

-- Index on longitude for location-based queries
CREATE INDEX idx_projects_longitude ON projects (longitude);

-- Index on latitude for location-based queries
CREATE INDEX idx_projects_latitude ON projects (latitude);

-- Index on is_active for filtering active records
CREATE INDEX idx_projects_is_active ON projects (is_active);

-- Index on created_at for audit and reporting queries
CREATE INDEX idx_projects_created_at ON projects (created_at);

-- Index on updated_at for change tracking
CREATE INDEX idx_projects_updated_at ON projects (updated_at);

-- Composite index for client and status queries
CREATE INDEX idx_projects_client_status ON projects (clients_id, status);

-- Composite index for date range queries
CREATE INDEX idx_projects_date_range ON projects (start_date, estimated_end_date);

-- Composite index for active projects with client
CREATE INDEX idx_projects_active_client ON projects (is_active, clients_id);

-- Composite index for location-based queries
CREATE INDEX idx_projects_location ON projects (longitude, latitude);
