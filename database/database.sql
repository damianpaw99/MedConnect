DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;

CREATE TABLE IF NOT EXISTS employees (
	pesel BIGINT PRIMARY KEY,
	name VARCHAR(50),
	surname VARCHAR(50),
	birth_date DATE,
	phone BIGINT,
	email VARCHAR(255),
	password VARCHAR(255),
	occupation VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS patients (
	pesel BIGINT PRIMARY KEY,
	name VARCHAR(50),
	surname VARCHAR(50),
	birth_date DATE,
	phone BIGINT,
	email VARCHAR(255),
	password VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS doctors (
	pesel BIGINT PRIMARY KEY,
	name VARCHAR(50),
	surname VARCHAR(50),
	birth_date DATE,
	phone BIGINT,
	email VARCHAR(255),
	password VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS specs (
	id serial PRIMARY KEY,
	name varchar(50) UNIQUE
);

CREATE TABLE IF NOT EXISTS doctors_specs (
	pesel BIGINT NOT NULL,
	id INT NOT NULL,
	PRIMARY KEY (pesel, id),
	FOREIGN KEY (pesel) REFERENCES doctors(pesel),
	FOREIGN KEY (id) REFERENCES specs(id)
);

CREATE TABLE IF NOT EXISTS appointments (
	id serial PRIMARY KEY,
	patient_pesel BIGINT,
	doctor_pesel BIGINT NOT NULL,
	worker_pesel BIGINT NOT NULL,
	date_godzina TIMESTAMP NOT NULL,
	FOREIGN KEY (patient_pesel) REFERENCES patients(pesel),
	FOREIGN KEY (doctor_pesel) REFERENCES doctors(pesel),
	FOREIGN KEY (worker_pesel) REFERENCES employees(pesel)
);

CREATE TABLE IF NOT EXISTS results (
	id serial PRIMARY KEY,
	patient_pesel BIGINT NOT NULL,
	type VARCHAR(50) NOT NULL,
	photos VARCHAR(255),
	insert_date TIMESTAMP NOT NULL,
	description TEXT,
	FOREIGN KEY (patient_pesel) REFERENCES patients(pesel)
);

CREATE TABLE IF NOT EXISTS login_logs(
	id serial PRIMARY KEY,
    pesel BIGINT NOT NULL,
    ip VARCHAR(50) NOT NULL,
    date_of_action TIMESTAMP NOT NULL,
    success BOOLEAN NOT NULL
);


CREATE PROCEDURE add_patient(
	pesel BIGINT,
	name VARCHAR(50),
	surname VARCHAR(50),
	birth_date DATE,
	phone BIGINT,
	email VARCHAR(255),
	password VARCHAR(255)
)
LANGUAGE SQL
AS $$
INSERT INTO patients VALUES (pesel, name, surname, birth_date, phone, email, password);
$$;

CREATE PROCEDURE add_employee(
	pesel BIGINT,
	name VARCHAR(50),
	surname VARCHAR(50),
	birth_date DATE,
	phone BIGINT,
	email VARCHAR(255),
	password VARCHAR(50),
	occupation VARCHAR(50)
)
LANGUAGE SQL
AS $$
INSERT INTO employees VALUES (pesel, name, surname, birth_date, phone, email, password, occupation);
$$;

CREATE PROCEDURE add_doctor(
	pesel BIGINT,
	name VARCHAR(50),
	surname VARCHAR(50),
	birth_date DATE,
	phone BIGINT,
	email VARCHAR(255),
	password VARCHAR(255)
)
LANGUAGE SQL
AS $$
INSERT INTO doctors VALUES (pesel, name, surname, birth_date, phone, email, password);
$$;

CREATE PROCEDURE assign_spec_to_doctor(
	pesel BIGINT,
	spec_name varchar(50)
)
LANGUAGE SQL
AS $$
INSERT INTO specs (name) VALUES (spec_name) ON CONFLICT (name) DO NOTHING;
INSERT INTO doctors_specs VALUES (pesel, (SELECT id FROM specs WHERE name like spec_name)) ON CONFLICT (pesel,id) DO NOTHING;
$$;


CREATE PROCEDURE change_doctor_password(
peselInput BIGINT,
hashedPassword varchar(255)
)
LANGUAGE SQL
AS $$
UPDATE doctors set password = hashedPassword WHERE pesel = peselInput;
$$;


CREATE PROCEDURE change_employees_password(
peselInput BIGINT,
hashedPassword varchar(255)
)
LANGUAGE SQL
AS $$
UPDATE employees set password = hashedPassword WHERE pesel = peselInput;
$$;

CREATE PROCEDURE change_patients_password(
peselInput BIGINT,
hashedPassword varchar(255)
)
LANGUAGE SQL
AS $$
UPDATE patients set password = hashedPassword WHERE pesel = peselInput;
$$;

CREATE VIEW free_appointments AS
SELECT ap.id, ap.date_godzina,d.name,d.surname, string_agg(s.name,', ')  FROM appointments ap
JOIN doctors d ON d.pesel=ap.doctor_pesel
join doctors_specs ds ON ds.pesel=d.pesel
join specs s on ds.id=s.id
WHERE ap.patient_pesel is null and ap.date_godzina>now()
GROUP BY ap.id, d.pesel;

CREATE VIEW all_appointments AS
SELECT ap.id, ap.date_godzina,d.name,d.surname, p.name AS patient_name, p.surname AS patient_surname, ap.patient_pesel, ap.doctor_pesel FROM appointments ap
JOIN doctors d ON d.pesel=ap.doctor_pesel
JOIN patients p ON p.pesel=ap.patient_pesel
ORDER BY ap.date_godzina;

CREATE VIEW all_results AS
SELECT r.id, r.patient_pesel, r.type, r.photos, r.insert_date, r.description FROM results r
ORDER BY r.insert_date DESC;

CREATE VIEW all_appointments_admin AS
SELECT ap.id, ap.date_godzina,d.name,d.surname, ap.patient_pesel, ap.doctor_pesel FROM appointments ap
JOIN doctors d ON d.pesel=ap.doctor_pesel
ORDER BY ap.date_godzina;
