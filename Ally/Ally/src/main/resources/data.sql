-- Datos iniciales para patients
INSERT INTO patients (id, nombre, apellido, fecha_nacimiento, id_usuario, numero_historia_clinica, codigo_obra_social, nro_afiliado_obra_social, tipo_discapacidad)
VALUES (1, 'Juan', 'Pérez', '1990-05-15', 1, 'HC12345', 'OS001', 'AF123', 'Visual');

INSERT INTO patients (id, nombre, apellido, fecha_nacimiento, id_usuario, numero_historia_clinica, codigo_obra_social, nro_afiliado_obra_social, tipo_discapacidad)
VALUES (2, 'María', 'Gómez', '1985-08-22', 2, 'HC12346', 'OS002', 'AF124', 'Auditiva');

-- Datos iniciales para specialties
INSERT INTO specialties (id, nombre) VALUES (1, 'Cardiología');
INSERT INTO specialties (id, nombre) VALUES (2, 'Pediatría');
INSERT INTO specialties (id, nombre) VALUES (3, 'Dermatología');

-- Datos iniciales para providers
INSERT INTO providers (id, nombre, apellido, fecha_nacimiento, id_usuario, id_especialidad, estado)
VALUES (1, 'Carlos', 'López', '1980-03-10', 3, 1, 'ACTIVO');

INSERT INTO providers (id, nombre, apellido, fecha_nacimiento, id_usuario, id_especialidad, estado)
VALUES (2, 'Ana', 'Martínez', '1975-11-30', 4, 2, 'ACTIVO');