# Database Objects Guidelines

## 1. Naming Conventions

### Modules
GE => General
CL => Client
HR => Human Resources

### 1.1 Tables
- **Prefix**: Use `{MODULE}_T` prefix for all system tables
- **Format**: `{MODULE}_T` + 4-letter descriptive name in uppercase
- **Examples**: 
  - `GE_TUSER` (users)
  - `GE_TPERS` (persons)
  - `GE_TEMPL` (employees)
  - `GE_TROLE` (roles)
  - `GE_TPERM` (permissions)
  - `GE_TROPE` (role-permission relationship)
  - `GE_TUSRO` (user-role relationship)
  - `CL_TCLIE` (clients)

### 1.2 Views
- **Prefix**: Use `{MODULE}_V` prefix for all views
- **Format**: `{MODULE}_V` + 4-letter descriptive name in uppercase
- **Example**: `GE_VUSER_WITH_ROLES`

### 1.3 Stored Procedures
- **Prefix**: Use `{MODULE}_P` prefix for stored procedures
- **Format**: `{MODULE}_P` + verb + 4-letter descriptive name
- **Examples**:
  - `GE_PINS_USER`
  - `GE_PUPD_USER_STATUS`
  - `GE_PGET_USERS_BY_ROLE`

### 1.4 Functions
- **Prefix**: Use `{MODULE}_F` prefix for functions
- **Format**: `{MODULE}_F` + verb + 4-letter descriptive name
- **Examples**:
  - `GE_FGET_USER_FULL_NAME`
  - `GE_FCALC_USER_AGE`

### 1.5 Triggers
- **Prefix**: Use `{MODULE}_T` prefix for triggers
- **Format**: `{MODULE}_T` + 4-letter table name + action
- **Examples**:
  - `GE_TUSER_AUDIT_INSERT`
  - `GE_TUSER_AUDIT_UPDATE`
  - `GE_TUSER_AUDIT_DELETE`

### 1.6 Indexes
- **Prefix**: Use `{MODULE}_I` prefix for indexes
- **Format**: `{MODULE}_I` + 4-letter table name + columns
- **Examples**:
  - `GE_IUSER_EMAIL`
  - `GE_IUSER_USERNAME`

## 2. Table Structure

### 2.1 Standard Columns
All tables must include the following audit columns:

```sql
-- Mandatory audit columns
CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
VERSION INTEGER DEFAULT 1
```

### 2.2 Column Naming Convention
- **Format**: `{4-letter_table_prefix}_{column_name}` in lowercase
- **Examples**:
  - `user_user` (primary key for GE_TUSER)
  - `user_username` (username column for GE_TUSER)
  - `pers_pers` (primary key for GE_TPERS)
  - `empl_empl` (primary key for GE_TEMPL)
  - `clie_clie` (primary key for CL_TCLIE)

### 2.3 Primary Key Convention
- **Format**: `{4-letter_table_prefix}_{4-letter_table_prefix}` (repeat the prefix)
- **Examples**:
  - `user_user` (primary key for GE_TUSER)
  - `pers_pers` (primary key for GE_TPERS)
  - `empl_empl` (primary key for GE_TEMPL)
  - `clie_clie` (primary key for CL_TCLIE)

### 2.4 Constraints
- **Primary Keys**: Always use `UUID` with `GEN_RANDOM_UUID()` function
  - Name: `PKY_{TABLE}_{4-letter_prefix}`
  - Example: `PKY_GE_TUSER_USER`
- **Foreign Keys**: Use the same type as the referenced primary key
  - Name: `FKY_{TABLE}_{4-letter_prefix}`
  - Example: `FKY_GE_TUSRO_ROLE`
- **NOT NULL Constraints**: Apply to mandatory fields
  - Name: `NN_{TABLE}_{4-letter_prefix}`
  - Example: `NN_GE_TUSRO_ROLE`
- **UNIQUE Constraints**: For fields that must be unique
  - Name: `UQ_{TABLE}_{4-letter_prefix}`
  - Example: `UQ_GE_TUSER_USERNAME`
- **CHECK Constraints**: For value validations
  - Name: `CK_{TABLE}_{4-letter_prefix}`
  - Example: `CK_GE_TUSER_STATUS`

## 3. PostgreSQL Specific Guidelines

### 3.1 Data Types
- **UUID**: Use `UUID` with `GEN_RANDOM_UUID()` function
- **Timestamps**: Use `TIMESTAMP DEFAULT CURRENT_TIMESTAMP`
- **Text**: Use `TEXT` for long text, `VARCHAR(n)` for limited text
- **Boolean**: Use `BOOLEAN DEFAULT TRUE/FALSE`
- **Numeric**: Use `DECIMAL(p,s)` for money, `INTEGER` for whole numbers

### 3.2 PostgreSQL Functions
- **UUID Generation**: `GEN_RANDOM_UUID()`
- **Current Timestamp**: `CURRENT_TIMESTAMP`
- **String Functions**: `LENGTH()`, `TRIM()`, `UPPER()`, `LOWER()`
- **Date Functions**: `NOW()`, `CURRENT_DATE`, `EXTRACT()`

### 3.3 PostgreSQL Features
- **Triggers**: Use `CREATE OR REPLACE FUNCTION` and `CREATE TRIGGER`
- **Indexes**: Use `CREATE INDEX` with `USING` clause if needed
- **Constraints**: Use `CHECK` constraints for data validation
- **Sequences**: Use `SERIAL` or `BIGSERIAL` for auto-increment (if needed)

## 4. File Organization

### 4.1 Directory Structure
```
database/
├── schema/
│   ├── tables/          # Table creation scripts
│   ├── views/           # View creation scripts
│   ├── indexes/         # Index creation scripts
│   └── constraints/     # Constraint scripts
├── logic/
│   ├── functions/       # Custom functions
│   ├── procedures/      # Stored procedures
│   ├── triggers/        # Audit and validation triggers
│   └── packages/        # Packages (if applicable)
├── data/
│   └── seed/           # Initial and test data
└── infrastructure/     # Deployment scripts
```

### 4.2 File Naming
- **Tables**: `{MODULE}_T{4-letter-name}.sql`
- **Views**: `{MODULE}_V{4-letter-name}.sql`
- **Procedures**: `{MODULE}_P{4-letter-name}.sql`
- **Functions**: `{MODULE}_F{4-letter-name}.sql`
- **Triggers**: `{MODULE}_T{4-letter-table}_{ACTION}.sql`

## 5. SQL Code Standards

### 5.1 Format
- Use **UPPERCASE** for SQL reserved words and functions
- Use **lowercase** for custom object names (tables, columns, constraints)
- Indent with 4 spaces
- Separate sections with blank lines
- Use PostgreSQL-specific syntax and functions

### 5.2 Version Header Template
**MANDATORY**: All SQL files must start with the following version header:

```sql
--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Create users table
-- ----------- -------------- --------------- ---------------- ----------------
---
```

**Date Format**: Use `DD/MM/YYYY` format
**Changes Description**: Brief description in English explaining what was created/modified

### 5.3 File Structure Template
Every SQL file should follow this structure:

```sql
--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Create users table
-- ----------- -------------- --------------- ---------------- ----------------
---
--
-- OBJECT TYPE: [TABLE/VIEW/PROCEDURE/FUNCTION/TRIGGER]
-- OBJECT NAME: [FULL_OBJECT_NAME]
-- DESCRIPTION: [Brief description of the object]
--
-- [SQL CODE HERE]
```

### 5.4 Version Control Guidelines
- **VERSION**: Increment version number for each change (1.0.0, 1.0.1, 1.1.0, etc.)
- **REQUEST NRO**: Reference to ticket/request number (if applicable)
- **USER**: Developer initials or username
- **DATE**: Date of change in DD/MM/YYYY format
- **CHANGES**: Brief description in English of what was changed

## 6. Example Table Structure

```sql
--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Create users table
-- ----------- -------------- --------------- ---------------- ----------------
---
--
-- OBJECT TYPE: TABLE
-- OBJECT NAME: GE_TUSER
-- DESCRIPTION: Stores system user information
--

DROP TABLE IF EXISTS GE_TUSER;
CREATE TABLE GE_TUSER (
    user_user UUID DEFAULT GEN_RANDOM_UUID(),
    user_username VARCHAR(50) NOT NULL,
    user_email VARCHAR(100) NOT NULL,
    user_pwd VARCHAR(255) NOT NULL,
    user_phone VARCHAR(20),
    user_is_active BOOLEAN DEFAULT TRUE,
    user_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT PKY_GE_TUSER_USER PRIMARY KEY (user_user),
    CONSTRAINT UQ_GE_TUSER_USERNAME UNIQUE (user_username),
    CONSTRAINT UQ_GE_TUSER_EMAIL UNIQUE (user_email)
);

-- Indexes
CREATE INDEX GE_IUSER_USERNAME ON GE_TUSER(user_username);
CREATE INDEX GE_IUSER_EMAIL ON GE_TUSER(user_email);
CREATE INDEX GE_IUSER_ACTIVE ON GE_TUSER(user_is_active);
```

## 7. Additional Examples

### 7.1 Stored Procedure Example
```sql
--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Create insert user procedure
-- ----------- -------------- --------------- ---------------- ----------------
---
--
-- OBJECT TYPE: PROCEDURE
-- OBJECT NAME: GE_PINS_USER
-- DESCRIPTION: Inserts a new user into the system
--

CREATE OR REPLACE FUNCTION GE_PINS_USER(
    p_username VARCHAR(50),
    p_email VARCHAR(100),
    p_password_hash VARCHAR(255)
)
RETURNS UUID
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id UUID;
BEGIN
    INSERT INTO GE_TUSER (
        user_username,
        user_email,
        user_pwd
    ) VALUES (
        p_username,
        p_email,
        p_password_hash
    ) RETURNING user_user INTO v_user_id;
    
    RETURN v_user_id;
END;
$$;
```

### 7.2 View Example
```sql
--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Create users with roles view
-- ----------- -------------- --------------- ---------------- ----------------
---
--
-- OBJECT TYPE: VIEW
-- OBJECT NAME: GE_VUSER_WITH_ROLES
-- DESCRIPTION: View showing users with their assigned roles
--

CREATE OR REPLACE VIEW GE_VUSER_WITH_ROLES AS
SELECT 
    u.user_user,
    u.user_username,
    u.user_email,
    u.user_is_active,
    r.role_name,
    r.role_description
FROM GE_TUSER u
LEFT JOIN GE_TUSRO ur ON u.user_user = ur.usro_user
LEFT JOIN GE_TROLE r ON ur.usro_role = r.role_role
WHERE u.user_is_active = TRUE;
```

## 8. Migration Guidelines

### 8.1 When Updating Existing Tables
- Always use `DROP TABLE IF EXISTS` before `CREATE TABLE`
- Maintain backward compatibility when possible
- Update version numbers in affected files
- Document all changes in the version header

### 8.2 PostgreSQL Best Practices
- Use `CREATE OR REPLACE` for functions and views
- Use `IF NOT EXISTS` for indexes and constraints when appropriate
- Use `CASCADE` options carefully
- Test all constraints and triggers thoroughly