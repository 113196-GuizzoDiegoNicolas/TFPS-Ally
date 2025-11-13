-- =====================================
-- USUARIOS BASE
-- =====================================
INSERT INTO usuarios (usuario, password, email, activo, bloqueado, intentos_fallidos, fecha_creacion, rol)
VALUES
    ('maria_paciente', '$2a$10$exampleHashedPassword1', 'maria@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
    ('roberto_prestador', '$2a$10$exampleHashedPassword2', 'roberto@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PRESTADOR'),
    ('laura_prestador', '$2a$10$exampleHashedPassword3', 'laura@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PRESTADOR'),
    ('juan_transportista', '$2a$10$exampleHashedPassword4', 'juan@email.com', true, false, 0, CURRENT_TIMESTAMP, 'TRANSPORTISTA');

-- =====================================
-- ESPECIALIDADES
-- =====================================
INSERT INTO specialties (codigo, nombre)
VALUES
    ('KINESIOLOGIA', 'Kinesiología'),
    ('TERAPIA_OCUPACIONAL', 'Terapia Ocupacional'),
    ('FISIATRIA', 'Fisiatría'),
    ('TRANSPORTE_SANITARIO', 'Transporte Sanitario'),
    ('PSICOLOGIA', 'Psicología'),
    ('NEUROLOGIA', 'Neurología');

-- =====================================
-- PRESTADORES
-- =====================================
-- Nota: los usuario_id deben coincidir con los IDs reales generados arriba
-- Si estás en H2 y el autoincrement empieza en 1, los IDs se asignarán en ese orden
-- (1 = María paciente, 2 = Roberto prestador, 3 = Laura prestador, 4 = Juan transportista)
INSERT INTO providers (nombre, apellido, correo_electronico, direccion, telefono, activo, usuario_id, codigo_especialidad, fecha_nacimiento)
VALUES
    ('Roberto', 'Pérez', 'roberto@email.com', 'Av. Colón 1234', '3515551111', true, 2, 1, '1980-05-12'),
    ('Laura', 'Sosa', 'laura@email.com', 'Bv. San Juan 345', '3515552222', true, 3, 2, '1985-08-25');

-- =====================================
-- PACIENTES
-- =====================================
INSERT INTO patients (
    nombre, apellido, fecha_nacimiento, direccion, telefono, telegram,
    correo_electronico, usuario_id, numero_historia_clinica, codigo_obra_social,
    nro_afiliado_obra_social, tipo_discapacidad
) VALUES
    ('María', 'Gómez', '1990-04-10', 'Córdoba 500', '3516000001', '@maria',
     'maria@email.com', 1, 'H1234', 'OSDE', 'A001', 'Motora');

-- =====================================
-- SERVICIOS (opcional)
-- =====================================
INSERT INTO services (paciente_id, prestador_id, especialidad, descripcion, estado, fecha_solicitud)
VALUES
    (1, 1, 'KINESIOLOGIA', 'Rehabilitación post operatoria rodilla derecha', 'PENDIENTE', CURRENT_TIMESTAMP);
