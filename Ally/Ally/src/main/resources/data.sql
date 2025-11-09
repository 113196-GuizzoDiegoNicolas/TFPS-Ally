-- data.sql CORREGIDO
INSERT INTO usuarios (id, usuario, password, email, activo, bloqueado, intentos_fallidos, fecha_creacion, rol)
VALUES
    (1, 'maria_paciente', '$2a$10$exampleHashedPassword1', 'maria@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
    (2, 'carlos_paciente', '$2a$10$exampleHashedPassword2', 'carlos@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
    (3, 'ana_paciente', '$2a$10$exampleHashedPassword3', 'ana@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
    (4, 'roberto_prestador', '$2a$10$exampleHashedPassword4', 'roberto@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PRESTADOR'),
    (5, 'laura_prestador', '$2a$10$exampleHashedPassword5', 'laura@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PRESTADOR'),
    (6, 'pedro_prestador', '$2a$10$exampleHashedPassword6', 'pedro@email.com', false, false, 0, CURRENT_TIMESTAMP, 'PRESTADOR'),
    (7, 'juan_transportista', '$2a$10$exampleHashedPassword7', 'juan@email.com', true, false, 0, CURRENT_TIMESTAMP, 'TRANSPORTISTA'),
    (8, 'miguel_transportista', '$2a$10$exampleHashedPassword8', 'miguel@email.com', true, false, 0, CURRENT_TIMESTAMP, 'TRANSPORTISTA'),
    (9, 'sofia_transportista', '$2a$10$exampleHashedPassword9', 'sofia@email.com', false, false, 0, CURRENT_TIMESTAMP, 'TRANSPORTISTA');

-- Especialidades médicas
INSERT INTO specialties (id, nombre)
VALUES
    (1, 'Cardiología'),
    (2, 'Pediatría'),
    (3, 'Dermatología'),
    (4, 'Traumatología'),
    (5, 'Oftalmología');