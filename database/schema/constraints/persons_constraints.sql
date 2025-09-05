--
-- VERSION 1.0.0
--
--
-- VERSION     REQUEST NRO    USER            DATE             CHANGES
-- ----------- -------------- --------------- ---------------- ----------------
-- 1.0.0                      JMVELEZ         15/01/2025       Creación constraints tabla personas
-- ----------- -------------- --------------- ---------------- ----------------
--
--
-- OBJECT TYPE: CONSTRAINTS
-- OBJECT NAME: persons_constraints
-- DESCRIPTION: Primary key, unique and check constraints for persons table
--

ALTER TABLE persons ADD CONSTRAINT pk_persons_id PRIMARY KEY (persons_id);
ALTER TABLE persons ADD CONSTRAINT fk_persons_users FOREIGN KEY (users_id) REFERENCES users(users_id);
ALTER TABLE persons ADD CONSTRAINT uq_persons_ident UNIQUE (ident_type, ident_number);
ALTER TABLE persons ADD CONSTRAINT ck_persons_first_name CHECK (LENGTH(TRIM(first_name)) > 0);
ALTER TABLE persons ADD CONSTRAINT ck_persons_last_name CHECK (LENGTH(TRIM(last_name)) > 0);
ALTER TABLE persons ADD CONSTRAINT ck_persons_gender CHECK (gender IN ('M', 'F', 'O'));