-- =====================================
-- ESPECIALIDADES CON VALORES POR HORA REALISTAS
-- =====================================
-- NOTA: La columna se llama "importe_consulta" no "valor_hora"
INSERT INTO specialties (codigo, nombre, importe_consulta)
VALUES
    ('KINESIOLOGIA', 'Kinesiología', 18000.00),           -- $18,000 por hora
    ('FONOAUDIOLOGIA', 'Fonoaudiología', 17000.00),       -- $17,000 por hora
    ('PSICOLOGIA', 'Psicología', 20000.00),               -- $20,000 por hora
    ('ASISTENTE_TERAPEUTICO', 'Asistente Terapéutico', 15000.00), -- $15,000 por hora
    ('TERAPIA_OCUPACIONAL', 'Terapia Ocupacional', 19000.00), -- $19,000 por hora
    ('TRANSPORTE_SANITARIO', 'Transporte Sanitario', 8000.00); -- $8,000 por hora

-- =====================================
-- MÉTODOS DE PAGO
-- =====================================
INSERT INTO metodos_pagos (metodo_pago)
VALUES
    ('TARJETA_CREDITO'),
    ('TARJETA_DEBITO'),
    ('TRANSFERENCIA_BANCARIA'),
    ('PAYPAL'),
    ('OBRA_SOCIAL'),
    ('CONTADO');

-- =====================================
-- USUARIOS BASE (CONTRASEÑAS EN TEXTO PLANO)
-- =====================================
INSERT INTO usuarios (usuario, password, email, activo, bloqueado, intentos_fallidos, fecha_creacion, rol)
VALUES
    ('maria_paciente', 'password123', 'maria@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
    ('roberto_prestador', 'password123', 'roberto@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PRESTADOR'),
    ('laura_prestador', 'password123', 'laura@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PRESTADOR'),
    ('ana_fonoaudiologa', 'password123', 'ana.fono@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PRESTADOR'),
    ('carlos_psicologo', 'password123', 'carlos.psi@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PRESTADOR'),
    ('juan_transportista', 'password123', 'juan@email.com', true, false, 0, CURRENT_TIMESTAMP, 'TRANSPORTISTA'),
    ('carla_transportista', 'password123', 'carla@email.com', true, false, 0, CURRENT_TIMESTAMP, 'TRANSPORTISTA'),
    ('marcos_transportista', 'password123', 'marcos@email.com', true, false, 0, CURRENT_TIMESTAMP, 'TRANSPORTISTA'),
    ('carlos_paciente', 'password123', 'carlos@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
    ('lucia_paciente', 'password123', 'lucia@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
    ('pedro_paciente', 'password123', 'pedro@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
    ('ana_paciente', 'password123', 'ana.paciente@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE');

-- =====================================
-- PRESTADORES (USANDO USUARIOS EXISTENTES)
-- =====================================
INSERT INTO providers (nombre, apellido, correo_electronico, direccion, telefono, activo, usuario_id, codigo_especialidad, fecha_nacimiento)
VALUES
    ('Roberto', 'Pérez', 'roberto@email.com', 'Av. Colón 1234', '3515551111', true, 2, 1, '1980-05-12'),  -- Kinesiología
    ('Laura', 'Sosa', 'laura@email.com', 'Bv. San Juan 345', '3515552222', true, 3, 5, '1985-08-25'),    -- Terapia Ocupacional
    ('Ana', 'Gutiérrez', 'ana.fono@email.com', 'Av. Vélez Sarsfield 789', '3515556666', true, 4, 2, '1990-03-15'), -- Fonoaudiología
    ('Carlos', 'Mendoza', 'carlos.psi@email.com', 'Bv. Illia 456', '3515557777', true, 5, 3, '1982-11-20'); -- Psicología

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
       'carlos@email.com', 9, 'HC-2024-002', 'SWISS MEDICAL', 'SM-002-67890', 'Visual'),

      ('Lucía', 'Martínez', '1988-07-22', 'Bv. Illia 2345', '3516000003', '@lucia_martinez',
       'lucia@email.com', 10, 'HC-2024-003', 'GALENO', 'GAL-003-54321', 'Auditiva'),

      ('Pedro', 'Rodríguez', '1965-03-30', 'Av. Vélez Sarsfield 1234', '3516000004', '@pedro_rodriguez',
       'pedro@email.com', 11, 'HC-2024-004', 'OSDE', 'OSDE-004-98765', 'Motora'),

      ('Ana', 'Silva', '1995-11-08', 'Calle 27 de Abril 567', '3516000005', '@ana_silva',
       'ana.paciente@email.com', 12, 'HC-2024-005', 'OMINT', 'OMI-005-45678', 'Cognitiva');

-- =====================================
-- TRANSPORTISTAS
-- =====================================
INSERT INTO transportistas (
    nombre, apellido, fecha_nacimiento, direccion, telefono, telegram,
    correo_electronico, usuario_id, zona_cobertura, activo
) VALUES
      (
          'Juan', 'Rodríguez', '1985-03-15', 'Av. Vélez Sarsfield 1200',
          '3515553333', '@juan_transport', 'juan@email.com', 6,
          'Zona Norte, Centro, Nueva Córdoba', true
      ),
      (
          'Carla', 'Martínez', '1990-07-22', 'Bv. Chacabuco 850',
          '3515554444', '@carla_transport', 'carla@email.com', 7,
          'Zona Sur, Centro, Alberdi', true
      ),
      (
          'Marcos', 'López', '1982-11-08', 'Av. Figueroa Alcorta 450',
          '3515555555', '@marcos_transport', 'marcos@email.com', 8,
          'Zona Este, Centro, Güemes', true
      );

-- =====================================
-- SERVICIOS
-- =====================================
-- NOTA: La tabla services NO tiene columnas: estado_pago, precio, id_metodo_pago
-- Columnas reales: id, fecha_solicitud, paciente_id, prestador_id, transportista_id, descripcion, especialidad, estado
INSERT INTO services (fecha_solicitud, paciente_id, prestador_id, transportista_id, descripcion, especialidad, estado)
VALUES
    -- KINESIOLOGÍA
    (CURRENT_TIMESTAMP - INTERVAL '5' DAY, 1, 1, NULL, 'Rehabilitación post operatoria rodilla derecha - 1 sesión', 'KINESIOLOGIA', 'PENDIENTE'),
    (CURRENT_TIMESTAMP - INTERVAL '3' DAY, 3, 1, NULL, 'Sesión de ejercicios para fortalecimiento muscular - 1 sesión', 'KINESIOLOGIA', 'ACEPTADO'),
    (CURRENT_TIMESTAMP - INTERVAL '1' DAY, 5, 1, NULL, 'Tratamiento para lumbalgia crónica - 2 sesiones', 'KINESIOLOGIA', 'EN_CURSO'),

    -- FONOAUDIOLOGÍA
    (CURRENT_TIMESTAMP - INTERVAL '4' DAY, 2, 3, NULL, 'Evaluación y terapia del lenguaje - 1 sesión', 'FONOAUDIOLOGIA', 'PENDIENTE'),
    (CURRENT_TIMESTAMP - INTERVAL '2' DAY, 4, 3, NULL, 'Rehabilitación de trastornos de voz - 2 sesiones', 'FONOAUDIOLOGIA', 'ACEPTADO'),

    -- PSICOLOGÍA
    (CURRENT_TIMESTAMP, 1, 4, NULL, 'Terapia cognitivo-conductual - 1 sesión', 'PSICOLOGIA', 'EN_CURSO'),
    (CURRENT_TIMESTAMP - INTERVAL '8' DAY, 3, 4, NULL, 'Acompañamiento terapéutico - 3 sesiones', 'PSICOLOGIA', 'COMPLETADO'),

    -- TERAPIA OCUPACIONAL
    (CURRENT_TIMESTAMP - INTERVAL '6' DAY, 2, 2, NULL, 'Terapia para mejora de habilidades motoras finas - 1 sesión', 'TERAPIA_OCUPACIONAL', 'PENDIENTE'),
    (CURRENT_TIMESTAMP - INTERVAL '2' DAY, 4, 2, NULL, 'Evaluación inicial de capacidades funcionales - 1 sesión', 'TERAPIA_OCUPACIONAL', 'ACEPTADO'),

    -- Servicios completados
    (CURRENT_TIMESTAMP - INTERVAL '10' DAY, 2, 1, NULL, 'Rehabilitación de hombro post fractura - 3 sesiones', 'KINESIOLOGIA', 'ACEPTADO'),

    -- Servicios cancelados
    (CURRENT_TIMESTAMP - INTERVAL '7' DAY, 4, 1, NULL, 'Sesión cancelada por enfermedad del paciente', 'KINESIOLOGIA', 'CANCELADO'),

    -- Servicios futuros
    (CURRENT_TIMESTAMP + INTERVAL '1' DAY, 3, 1, NULL, 'Control de evolución post tratamiento', 'KINESIOLOGIA', 'PENDIENTE'),
    (CURRENT_TIMESTAMP + INTERVAL '2' DAY, 4, 2, NULL, 'Taller de habilidades sociales - 2 sesiones', 'TERAPIA_OCUPACIONAL', 'ACEPTADO');

-- =====================================
-- PAGOS DE EJEMPLO
-- =====================================
-- NOTA: Las columnas son diferentes a las que intentabas insertar
-- Columnas reales: id, comision, importe, monto, monto_copago, monto_cubierto_os, monto_neto, fecha_actualizacion, fecha_confirmacion,
-- fecha_creacion, fecha_pago, fecha_procesamiento, metodo_pago_id, servicio_id, version, codigo_autorizacion,
-- numero_autorizacion_os, referencia_externa, datos_adicionales, id_transaccion, mensaje_error, estado_pago
INSERT INTO payments (servicio_id, monto, estado_pago, fecha_pago, fecha_procesamiento, id_transaccion, metodo_pago_id)
VALUES
    -- Pagos COMPLETADOS exitosos
    (7, 60000.00, 'COMPLETADO', CURRENT_TIMESTAMP - INTERVAL '7' DAY, CURRENT_TIMESTAMP - INTERVAL '7' DAY, 'TX_PSI60000', 1),
    (10, 54000.00, 'COMPLETADO', CURRENT_TIMESTAMP - INTERVAL '9' DAY, CURRENT_TIMESTAMP - INTERVAL '9' DAY, 'TX_KIN54000', 5),

    -- Pagos PENDIENTES (no en PROCESANDO)
    (3, 36000.00, 'PENDIENTE', NULL, NULL, 'TX_KIN36000', 3),
    (6, 20000.00, 'PENDIENTE', NULL, NULL, 'TX_PSI20000', 6),

    -- Pagos FALLIDOS
    (11, 18000.00, 'FALLIDO', CURRENT_TIMESTAMP - INTERVAL '3' DAY, CURRENT_TIMESTAMP - INTERVAL '3' DAY, 'TX_KIN18000F', 2),

    -- Pagos REEMBOLSADOS
    (12, 19000.00, 'REEMBOLSADO', CURRENT_TIMESTAMP - INTERVAL '5' DAY, CURRENT_TIMESTAMP - INTERVAL '4' DAY, 'TX_TER19000R', 1),

    -- Pagos para servicios nuevos
    (2, 18000.00, 'PENDIENTE', NULL, NULL, 'MP_PREF_001', 1),
    (5, 34000.00, 'PENDIENTE', NULL, NULL, 'MP_PREF_002', 1),
    (8, 19000.00, 'PENDIENTE', NULL, NULL, 'MP_PREF_003', 2);

-- =====================================
-- TURNOS
-- =====================================
INSERT INTO turnos (paciente_id, prestador_id, transportista_id, fecha_hora, fecha_creacion, estado, observaciones)
VALUES
    (1, 1, 1, CURRENT_TIMESTAMP + INTERVAL '1' DAY, CURRENT_TIMESTAMP, 'CONFIRMADO', 'Paciente requiere silla de ruedas'),
    (2, 2, 2, CURRENT_TIMESTAMP + INTERVAL '2' DAY, CURRENT_TIMESTAMP, 'PENDIENTE', 'Traer estudios médicos'),
    (3, 1, NULL, CURRENT_TIMESTAMP + INTERVAL '3' DAY, CURRENT_TIMESTAMP, 'CONFIRMADO', 'Control rutinario');