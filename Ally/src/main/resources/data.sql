-- =====================================
-- USUARIOS BASE (CONTRASEÑAS EN TEXTO PLANO)
-- =====================================
INSERT INTO usuarios (usuario, password, email, activo, bloqueado, intentos_fallidos, fecha_creacion, rol)
VALUES
    ('maria_paciente', 'password123', 'maria@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
    ('roberto_prestador', 'password123', 'roberto@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PRESTADOR'),
    ('laura_prestador', 'password123', 'laura@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PRESTADOR'),
    ('juan_transportista', 'password123', 'juan@email.com', true, false, 0, CURRENT_TIMESTAMP, 'TRANSPORTISTA'),
    ('carla_transportista', 'password123', 'carla@email.com', true, false, 0, CURRENT_TIMESTAMP, 'TRANSPORTISTA'),
    ('marcos_transportista', 'password123', 'marcos@email.com', true, false, 0, CURRENT_TIMESTAMP, 'TRANSPORTISTA'),
    ('carlos_paciente', 'password123', 'carlos@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
    ('lucia_paciente', 'password123', 'lucia@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
    ('pedro_paciente', 'password123', 'pedro@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
    ('ana_paciente', 'password123', 'ana.paciente@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE');

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
-- SERVICIOS - MÚLTIPLES CASOS CON DIFERENTES ESTADOS
-- =====================================
INSERT INTO services (paciente_id, prestador_id, especialidad, descripcion, estado, estado_pago, fecha_solicitud)
VALUES
    -- Servicios PENDIENTES con PENDIENTE de pago
    (1, 1, 'KINESIOLOGIA', 'Rehabilitación post operatoria rodilla derecha', 'PENDIENTE', 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '5' DAY),
    (2, 2, 'TERAPIA_OCUPACIONAL', 'Terapia para mejora de habilidades motoras finas', 'PENDIENTE', 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '4' DAY),

    -- Servicios CONFIRMADOS con PENDIENTE de pago
    (3, 1, 'KINESIOLOGIA', 'Sesión de ejercicios para fortalecimiento muscular', 'ACEPTADO', 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '3' DAY),
    (4, 2, 'TERAPIA_OCUPACIONAL', 'Evaluación inicial de capacidades funcionales', 'ACEPTADO', 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '2' DAY),

    -- Servicios EN_CURSO con PROCESANDO pago
    (5, 1, 'KINESIOLOGIA', 'Tratamiento para lumbalgia crónica', 'EN_CURSO', 'PROCESANDO', CURRENT_TIMESTAMP - INTERVAL '1' DAY),
    (1, 2, 'TERAPIA_OCUPACIONAL', 'Terapia de integración sensorial', 'EN_CURSO', 'PROCESANDO', CURRENT_TIMESTAMP),

    -- Servicios COMPLETADOS con COMPLETADO pago
    (2, 1, 'KINESIOLOGIA', 'Rehabilitación de hombro post fractura', 'ACEPTADO', 'COMPLETADO', CURRENT_TIMESTAMP - INTERVAL '10' DAY),
    (3, 2, 'TERAPIA_OCUPACIONAL', 'Entrenamiento en actividades de la vida diaria', 'COMPLETADO', 'COMPLETADO', CURRENT_TIMESTAMP - INTERVAL '8' DAY),

    -- Servicios CANCELADOS con diferentes estados de pago
    (4, 1, 'KINESIOLOGIA', 'Sesión cancelada por enfermedad del paciente', 'CANCELADO', 'CANCELADO', CURRENT_TIMESTAMP - INTERVAL '7' DAY),
    (5, 2, 'TERAPIA_OCUPACIONAL', 'Cancelado por conflicto de horarios', 'CANCELADO', 'REEMBOLSADO', CURRENT_TIMESTAMP - INTERVAL '6' DAY),

    -- Servicios con pago FALLIDO
    (1, 1, 'KINESIOLOGIA', 'Sesión de electroterapia - pago rechazado', 'COMPLETADO', 'FALLIDO', CURRENT_TIMESTAMP - INTERVAL '3' DAY),

    -- Servicios con REEMBOLSADO
    (2, 2, 'TERAPIA_OCUPACIONAL', 'Terapia no realizada - reembolso procesado', 'CANCELADO', 'REEMBOLSADO', CURRENT_TIMESTAMP - INTERVAL '2' DAY),

    -- Más servicios para testing
    (3, 1, 'KINESIOLOGIA', 'Control de evolución post tratamiento', 'PENDIENTE', 'PENDIENTE', CURRENT_TIMESTAMP + INTERVAL '1' DAY),
    (4, 2, 'TERAPIA_OCUPACIONAL', 'Taller de habilidades sociales', 'ACEPTADO', 'PENDIENTE', CURRENT_TIMESTAMP + INTERVAL '2' DAY),
    (5, 1, 'KINESIOLOGIA', 'Masaje descontracturante', 'EN_CURSO', 'PROCESANDO', CURRENT_TIMESTAMP + INTERVAL '3' DAY);

-- =====================================
-- TURNOS (OPCIONAL - SI LA TABLA EXISTE)
-- =====================================
-- INSERT INTO turnos (paciente_id, prestador_id, transportista_id, fecha_hora, fecha_creacion, estado, observaciones)
-- VALUES
--     (1, 1, 1, CURRENT_TIMESTAMP + INTERVAL '1' DAY, CURRENT_TIMESTAMP, 'CONFIRMADO', 'Paciente requiere silla de ruedas'),
--     (2, 2, 2, CURRENT_TIMESTAMP + INTERVAL '2' DAY, CURRENT_TIMESTAMP, 'PENDIENTE', 'Traer estudios médicos'),
--     (3, 1, NULL, CURRENT_TIMESTAMP + INTERVAL '3' DAY, CURRENT_TIMESTAMP, 'CONFIRMADO', 'Control rutinario');