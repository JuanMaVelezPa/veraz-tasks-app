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
-- OBJECT NAME: GE_TEMPL
-- DESCRIPTION: Stores employee information linked to persons and users
--

DROP TABLE IF EXISTS GE_TEMPL;
CREATE TABLE GE_TEMPL (
    -- Identificación básica
    empl_empl UUID DEFAULT GEN_RANDOM_UUID(),
    empl_pers UUID NOT NULL,
    empl_employee_code VARCHAR(20) NOT NULL,
    
    -- Información laboral básica
    empl_position VARCHAR(100) NOT NULL,
    empl_department VARCHAR(100),
    empl_employment_type VARCHAR(20) NOT NULL, -- FULL_TIME, PART_TIME, CONTRACTOR, INTERN, FREELANCER
    empl_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, INACTIVE, TERMINATED, ON_LEAVE
    
    -- Fechas importantes
    empl_hire_date DATE NOT NULL,
    empl_termination_date DATE,
    empl_probation_end_date DATE,
    
    -- Compensación
    empl_salary DECIMAL(12,2),
    empl_currency VARCHAR(3) DEFAULT 'USD',
    empl_salary_type VARCHAR(20), -- HOURLY, MONTHLY, ANNUAL
    
    -- Información de contacto laboral
    empl_work_email VARCHAR(100),
    empl_work_phone VARCHAR(20),
    empl_work_location VARCHAR(100),
    empl_work_schedule VARCHAR(100),
    
    -- Información adicional básica
    empl_job_level VARCHAR(20), -- JUNIOR, MID, SENIOR, LEAD, MANAGER, DIRECTOR, EXECUTIVE
    empl_cost_center VARCHAR(50),
    empl_work_shift VARCHAR(20), -- DAY, NIGHT, ROTATING, FLEXIBLE
    
    -- Información adicional (campos flexibles para primera fase)
    empl_skills TEXT,
    empl_certifications TEXT,
    empl_education TEXT,
    empl_benefits TEXT,
    empl_notes TEXT,
    
    -- Campos de auditoría
    empl_is_active BOOLEAN DEFAULT TRUE,
    empl_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    empl_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    empl_created_by UUID,
    empl_updated_by UUID
); 