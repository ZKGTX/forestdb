CREATE TABLE IF NOT EXISTS subjects (
id SERIAL PRIMARY KEY,
name TEXT NOT NULL,
last_update VARCHAR (200)
);

CREATE TABLE IF NOT EXISTS risks (
id SERIAL PRIMARY KEY,
name TEXT NOT NULL,
subject_id INT NOT NULL,
last_update VARCHAR (200),
CONSTRAINT fk_subject FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS measures (
id SERIAL PRIMARY KEY,
name TEXT NOT NULL,
risk_id INT NOT NULL,
last_update VARCHAR (200),
CONSTRAINT fk_risk FOREIGN KEY (risk_id) REFERENCES risks (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS actions (
id SERIAL PRIMARY KEY,
name TEXT NOT NULL,
measure_id INT NOT NULL,
CONSTRAINT fk_measure FOREIGN KEY (measure_id) REFERENCES measures (id) ON DELETE CASCADE,
commentary TEXT,
original BOOLEAN,
last_update VARCHAR (200)
);

CREATE TABLE IF NOT EXISTS reporting_years (
id SERIAL PRIMARY KEY,
year INT NOT NULL,
planned_work_amount CHARACTER VARYING(200) NOT NULL,
actual_work_amount CHARACTER VARYING(200) NOT NULL,
work_measuring_units CHARACTER VARYING(50) NOT NULL,
planned_work_cost CHARACTER VARYING(200) NOT NULL,
actual_work_cost CHARACTER VARYING(200) NOT NULL,
cost_measuring_units CHARACTER VARYING(50) NOT NULL,
commentary TEXT,
action_id INT NOT NULL,
last_update VARCHAR (200),
CONSTRAINT fk_action FOREIGN KEY (action_id) REFERENCES actions (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users (
id SERIAL PRIMARY KEY,
email VARCHAR(100),
first_name VARCHAR(50),
last_name VARCHAR(50),
subject_name VARCHAR(200),
password TEXT,
password_confirm TEXT,
reset_password_token VARCHAR (50)
);

CREATE TABLE IF NOT EXISTS roles (
id SERIAL PRIMARY KEY,
name VARCHAR (50) NOT NULL
);

CREATE TABLE IF NOT EXISTS users_roles (
user_id INT NOT NULL,
role_id INT NOT NULL,
PRIMARY KEY(user_id, role_id),
CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);