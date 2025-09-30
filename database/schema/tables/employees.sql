--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación tabla empleados
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: TABLE
-- OBJECT NAME: employees
-- DESCRIPTION: Stores employment information linked to persons
--

DROP TABLE IF EXISTS employees;
CREATE TABLE employees (
    -- CAMPOS OBLIGATORIOS
    employees_id UUID DEFAULT GEN_RANDOM_UUID(),
    persons_id UUID NOT NULL,
    hire_date DATE NOT NULL,
    position VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    salary NUMERIC(12,2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    salary_type VARCHAR(20) NOT NULL,
    
    -- CAMPOS OPCIONALES
    department VARCHAR(100),
    employment_type VARCHAR(20),
    termination_date DATE,
    probation_end_date DATE,
    work_email VARCHAR(100),
    work_phone VARCHAR(20),
    work_location VARCHAR(100),
    work_schedule VARCHAR(100),
    job_level VARCHAR(20),
    cost_center VARCHAR(50),
    work_shift VARCHAR(20),
    skills TEXT,
    certifications TEXT,
    education TEXT,
    benefits TEXT,
    notes TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID
);