-- =====================================
-- USUARIOS BASE
-- =====================================
INSERT INTO usuarios (id, usuario, password, email, activo, bloqueado, intentos_fallidos, fecha_creacion, rol)
VALUES
    (1, 'maria_paciente', '$2a$10$exampleHashedPassword1', 'maria@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
    (2, 'roberto_prestador', '$2a$10$exampleHashedPassword2', 'roberto@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PRESTADOR'),
    (3, 'laura_prestador', '$2a$10$exampleHashedPassword3', 'laura@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PRESTADOR'),
    (4, 'juan_transportista', '$2a$10$exampleHashedPassword4', 'juan@email.com', true, false, 0, CURRENT_TIMESTAMP, 'TRANSPORTISTA');
ALTER TABLE usuarios ALTER COLUMN id RESTART WITH 8;
-- =====================================
-- ESPECIALIDADES
-- =====================================
INSERT INTO specialties (id, codigo, nombre)
VALUES
    (1, 'KINESIOLOGIA', 'Kinesiología'),
    (2, 'TERAPIA_OCUPACIONAL', 'Terapia Ocupacional'),
    (3, 'FISIATRIA', 'Fisiatría'),
    (4, 'TRANSPORTE_SANITARIO', 'Transporte Sanitario'),
    (5, 'PSICOLOGIA', 'Psicología'),
    (6, 'NEUROLOGIA', 'Neurología');

-- =====================================
-- PRESTADORES
-- =====================================
INSERT INTO providers (id, nombre, apellido, correo_electronico, direccion, telefono, activo, usuario_id, codigo_especialidad, fecha_nacimiento)
VALUES
    (1, 'Roberto', 'Pérez', 'roberto@email.com', 'Av. Colón 1234', '3515551111', true, 2, 1, '1980-05-12'),
    (2, 'Laura', 'Sosa', 'laura@email.com', 'Bv. San Juan 345', '3515552222', true, 3, 2, '1985-08-25');


-- =====================================
-- PACIENTES
-- =====================================
INSERT INTO patients (
    id, nombre, apellido, fecha_nacimiento, direccion, telefono, telegram,
    correo_electronico, usuario_id, numero_historia_clinica, codigo_obra_social,
    nro_afiliado_obra_social, tipo_discapacidad
) VALUES
    (1, 'María', 'Gómez', '1990-04-10', 'Córdoba 500', '3516000001', '@maria',
     'maria@email.com', 1, 'H1234', 'OSDE', 'A001', 'Motora');


-- =====================================
-- SERVICIOS (opcional)
-- =====================================
INSERT INTO services (id, paciente_id, prestador_id, especialidad, descripcion, estado, fecha_solicitud)
VALUES
    (1, 1, 1, 'KINESIOLOGIA', 'Rehabilitación post operatoria rodilla derecha', 'PENDIENTE', CURRENT_TIMESTAMP);
