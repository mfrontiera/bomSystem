--liquibase formatted sql

--changeset system:2
INSERT INTO customers (
    id,
    version,
    birth_date,
    email,
    first_name,
    last_name,
    occupation,
    phone,
    role,
    vip
) VALUES (
             1,       -- ID (NUMBER(10))
             1,       -- VERSION (NUMBER(5))
             '1985-06-15',                     -- BIRTH_DATE (YYYY-MM-DD)
             'john.doe@example.com',           -- EMAIL (VARCHAR2(100))
             'John',                           -- FIRST_NAME (VARCHAR2(50))
             'Doe',                            -- LAST_NAME (VARCHAR2(50))
             'MORTARMAN',              -- OCCUPATION (INSURANCE_CLERK, MORTARMAN, BEER_COIL_CLEANER, SCALE_ATTENDANT)
             '+123456789',                     -- PHONE (VARCHAR2(50))
             'MANAGER',                        -- ROLE (Valid value from permitted list: WORKER, SUPERVISOR, MANAGER, EXTERNAL)
             1                                 -- VIP (1 = true, 0 = false)
         );
