-- =====================================
-- USUARIOS BASE
-- =====================================
INSERT INTO usuarios (usuario, password, email, activo, bloqueado, intentos_fallidos, fecha_creacion, rol)
VALUES
    ('maria_paciente', '$2a$10$exampleHashedPassword1', 'maria@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
    ('roberto_prestador', '$2a$10$exampleHashedPassword2', 'roberto@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PRESTADOR'),
    ('laura_prestador', '$2a$10$exampleHashedPassword3', 'laura@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PRESTADOR'),
    ('juan_transportista', '$2a$10$exampleHashedPassword4', 'juan@email.com', true, false, 0, CURRENT_TIMESTAMP, 'TRANSPORTISTA'),
    ('carla_transportista', '$2a$10$exampleHashedPassword5', 'carla@email.com', true, false, 0, CURRENT_TIMESTAMP, 'TRANSPORTISTA'),
    ('marcos_transportista', '$2a$10$exampleHashedPassword6', 'marcos@email.com', true, false, 0, CURRENT_TIMESTAMP, 'TRANSPORTISTA'),
    ('carlos_paciente', '$2a$10$exampleHashedPassword7', 'carlos@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
    ('lucia_paciente', '$2a$10$exampleHashedPassword8', 'lucia@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
    ('pedro_paciente', '$2a$10$exampleHashedPassword9', 'pedro@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
    ('ana_paciente', '$2a$10$exampleHashedPassword10', 'ana.paciente@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE');

-- =====================================
-- ESPECIALIDADES
-- =====================================
INSERT INTO specialties (id, nombre) VALUES
                                         (1, 'Kinesiología'),
                                         (2, 'Terapia Ocupacional'),
                                         (3, 'Fisiatría'),
                                         (4, 'Transporte Sanitario'),
                                         (5, 'Psicología'),
                                         (6, 'Neurología');


-- =====================================
-- PRESTADORES
-- =====================================
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
      ('María', 'Gómez', '1990-04-10', 'Córdoba 500', '3516000001', '@maria_gomez',
       'maria@email.com', 1, 'HC-2024-001', 'OSDE', 'OSDE-001-12345', 'Motora'),

      ('Carlos', 'López', '1975-12-15', 'Av. Rafael Núñez 4587', '3516000002', '@carlos_lopez',
       'carlos@email.com', 7, 'HC-2024-002', 'SWISS MEDICAL', 'SM-002-67890', 'Visual'),

      ('Lucía', 'Martínez', '1988-07-22', 'Bv. Illia 2345', '3516000003', '@lucia_martinez',
       'lucia@email.com', 8, 'HC-2024-003', 'GALENO', 'GAL-003-54321', 'Auditiva'),

      ('Pedro', 'Rodríguez', '1965-03-30', 'Av. Vélez Sarsfield 1234', '3516000004', '@pedro_rodriguez',
       'pedro@email.com', 9, 'HC-2024-004', 'OSDE', 'OSDE-004-98765', 'Motora'),

      ('Ana', 'Silva', '1995-11-08', 'Calle 27 de Abril 567', '3516000005', '@ana_silva',
       'ana.paciente@email.com', 10, 'HC-2024-005', 'OMINT', 'OMI-005-45678', 'Cognitiva');

-- =====================================
-- TRANSPORTISTAS
-- =====================================
INSERT INTO transportistas (
    nombre, apellido, fecha_nacimiento, direccion, telefono, telegram,
    correo_electronico, usuario_id, zona_cobertura, activo
) VALUES
      (
          'Juan', 'Rodríguez', '1985-03-15', 'Av. Vélez Sarsfield 1200',
          '3515553333', '@juan_transport', 'juan@email.com', 4,
          'Zona Norte, Centro, Nueva Córdoba', true
      ),
      (
          'Carla', 'Martínez', '1990-07-22', 'Bv. Chacabuco 850',
          '3515554444', '@carla_transport', 'carla@email.com', 5,
          'Zona Sur, Centro, Alberdi', true
      ),
      (
          'Marcos', 'López', '1982-11-08', 'Av. Figueroa Alcorta 450',
          '3515555555', '@marcos_transport', 'marcos@email.com', 6,
          'Zona Este, Centro, Güemes', true
      );

-- =====================================
-- SERVICIOS (opcional)
-- =====================================
INSERT INTO services (paciente_id, prestador_id, especialidad, descripcion, estado, fecha_solicitud)
VALUES
    (1, 1, 'KINESIOLOGIA', 'Rehabilitación post operatoria rodilla derecha', 'PENDIENTE', CURRENT_TIMESTAMP),
    (2, 2, 'TERAPIA_OCUPACIONAL', 'Terapia para mejora de habilidades motoras finas', 'CONFIRMADO', CURRENT_TIMESTAMP);

-- =====================================
-- COMENTADO: SERVICIOS DE TRANSPORTE
-- Esta tabla no existe en el schema actual
-- =====================================
-- INSERT INTO transport_services (
--     paciente_id, transportista_id, origen, destino, fecha_servicio,
--     estado, tipo_vehiculo, observaciones
-- ) VALUES
--     (
--         1, 1, 'Córdoba 500', 'Av. Colón 1234',
--         CURRENT_TIMESTAMP + INTERVAL '1' DAY, 'PENDIENTE',
--         'Ambulancia', 'Paciente con movilidad reducida - requiere silla de ruedas'
--     ),
--     (
--         2, 2, 'Av. Rafael Núñez 4587', 'Bv. San Juan 345',
--         CURRENT_TIMESTAMP + INTERVAL '2' DAY, 'CONFIRMADO',
--         'Vehículo adaptado', 'Traslado para terapia ocupacional'
--     );