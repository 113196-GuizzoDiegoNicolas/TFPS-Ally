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
    ('ana_paciente', 'password123', 'ana.paciente@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
    -- NUEVOS USUARIOS PARA PRESTADORES
    ('ana_fonoaudiologa', 'password123', 'ana.fono@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PRESTADOR'),
    ('carlos_psicologo', 'password123', 'carlos.psi@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PRESTADOR');
-- =====================================
-- ESPECIALIDADES CON VALORES POR HORA REALISTAS
-- =====================================
INSERT INTO specialties (codigo, nombre, valor_hora)
VALUES
    ('KINESIOLOGIA', 'Kinesiología', 18000.00),           -- $18,000 por hora
    ('FONOAUDIOLOGIA', 'Fonoaudiología', 17000.00),       -- $17,000 por hora
    ('PSICOLOGIA', 'Psicología', 20000.00),               -- $20,000 por hora
    ('ASISTENTE_TERAPEUTICO', 'Asistente Terapéutico', 15000.00), -- $15,000 por hora
    ('TERAPIA_OCUPACIONAL', 'Terapia Ocupacional', 19000.00), -- $19,000 por hora (agregado)
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
-- PRESTADORES (USANDO USUARIOS EXISTENTES)
-- =====================================
INSERT INTO providers (nombre, apellido, correo_electronico, direccion, telefono, activo, usuario_id, codigo_especialidad, fecha_nacimiento)
VALUES
    ('Roberto', 'Pérez', 'roberto@email.com', 'Av. Colón 1234', '3515551111', true, 2, 1, '1980-05-12'),  -- Kinesiología
    ('Laura', 'Sosa', 'laura@email.com', 'Bv. San Juan 345', '3515552222', true, 3, 5, '1985-08-25'),    -- Terapia Ocupacional
    ('Ana', 'Gutiérrez', 'ana.fono@email.com', 'Av. Vélez Sarsfield 789', '3515556666', true, 4, 2, '1990-03-15'), -- Fonoaudiología (usando usuario 4)
    ('Carlos', 'Mendoza', 'carlos.psi@email.com', 'Bv. Illia 456', '3515557777', true, 5, 3, '1982-11-20'); -- Psicología (usando usuario 5)
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
-- SERVICIOS - CON PRECIOS BASADOS EN VALORES REALES
-- =====================================
INSERT INTO services (paciente_id, prestador_id, especialidad, descripcion, estado, estado_pago, fecha_solicitud, precio, id_metodo_pago)
VALUES
    -- KINESIOLOGÍA ($18,000/hora)
    (1, 1, 'KINESIOLOGIA', 'Rehabilitación post operatoria rodilla derecha - 1 sesión', 'PENDIENTE', 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '5' DAY, 18000.00, 1),
    (3, 1, 'KINESIOLOGIA', 'Sesión de ejercicios para fortalecimiento muscular - 1 sesión', 'ACEPTADO', 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '3' DAY, 18000.00, 2),
    (5, 1, 'KINESIOLOGIA', 'Tratamiento para lumbalgia crónica - 2 sesiones', 'EN_CURSO', 'PROCESANDO', CURRENT_TIMESTAMP - INTERVAL '1' DAY, 36000.00, 3),

    -- FONOAUDIOLOGÍA ($17,000/hora)
    (2, 3, 'FONOAUDIOLOGIA', 'Evaluación y terapia del lenguaje - 1 sesión', 'PENDIENTE', 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '4' DAY, 17000.00, 4),
    (4, 3, 'FONOAUDIOLOGIA', 'Rehabilitación de trastornos de voz - 2 sesiones', 'ACEPTADO', 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '2' DAY, 34000.00, 5),

    -- PSICOLOGÍA ($20,000/hora)
    (1, 4, 'PSICOLOGIA', 'Terapia cognitivo-conductual - 1 sesión', 'EN_CURSO', 'PROCESANDO', CURRENT_TIMESTAMP, 20000.00, 6),
    (3, 4, 'PSICOLOGIA', 'Acompañamiento terapéutico - 3 sesiones', 'COMPLETADO', 'COMPLETADO', CURRENT_TIMESTAMP - INTERVAL '8' DAY, 60000.00, 1),

    -- TERAPIA OCUPACIONAL ($19,000/hora)
    (2, 2, 'TERAPIA_OCUPACIONAL', 'Terapia para mejora de habilidades motoras finas - 1 sesión', 'PENDIENTE', 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '6' DAY, 19000.00, 2),
    (4, 2, 'TERAPIA_OCUPACIONAL', 'Evaluación inicial de capacidades funcionales - 1 sesión', 'ACEPTADO', 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '2' DAY, 19000.00, 3),

    -- ASISTENTE TERAPÉUTICO ($15,000/hora)
    (5, 1, 'ASISTENTE_TERAPEUTICO', 'Acompañamiento en actividades diarias - 4 horas', 'EN_CURSO', 'PROCESANDO', CURRENT_TIMESTAMP - INTERVAL '1' DAY, 60000.00, 4),

    -- Servicios completados
    (2, 1, 'KINESIOLOGIA', 'Rehabilitación de hombro post fractura - 3 sesiones', 'ACEPTADO', 'COMPLETADO', CURRENT_TIMESTAMP - INTERVAL '10' DAY, 54000.00, 5),

    -- Servicios cancelados
    (4, 1, 'KINESIOLOGIA', 'Sesión cancelada por enfermedad del paciente', 'CANCELADO', 'CANCELADO', CURRENT_TIMESTAMP - INTERVAL '7' DAY, 18000.00, 6),
    (5, 2, 'TERAPIA_OCUPACIONAL', 'Cancelado por conflicto de horarios', 'CANCELADO', 'REEMBOLSADO', CURRENT_TIMESTAMP - INTERVAL '6' DAY, 19000.00, 1),

    -- Servicios con problemas de pago
    (1, 1, 'KINESIOLOGIA', 'Sesión de electroterapia - pago rechazado', 'COMPLETADO', 'FALLIDO', CURRENT_TIMESTAMP - INTERVAL '3' DAY, 18000.00, 2),

    -- Servicios futuros
    (3, 1, 'KINESIOLOGIA', 'Control de evolución post tratamiento', 'PENDIENTE', 'PENDIENTE', CURRENT_TIMESTAMP + INTERVAL '1' DAY, 18000.00, 3),
    (4, 2, 'TERAPIA_OCUPACIONAL', 'Taller de habilidades sociales - 2 sesiones', 'ACEPTADO', 'PENDIENTE', CURRENT_TIMESTAMP + INTERVAL '2' DAY, 38000.00, 4);

-- =====================================
-- PAGOS DE EJEMPLO
-- =====================================
INSERT INTO payments (servicio_id, monto, estado, fecha_pago, fecha_procesamiento, id_transaccion, metodo_pago_id)
VALUES
    -- Pagos COMPLETADOS exitosos (montos altos)
    (7, 60000.00, 'COMPLETADO', CURRENT_TIMESTAMP - INTERVAL '7' DAY, CURRENT_TIMESTAMP - INTERVAL '7' DAY, 'TX_PSI60000', 1),
    (11, 54000.00, 'COMPLETADO', CURRENT_TIMESTAMP - INTERVAL '9' DAY, CURRENT_TIMESTAMP - INTERVAL '9' DAY, 'TX_KIN54000', 5),

    -- Pagos en PROCESANDO
    (3, 36000.00, 'PROCESANDO', CURRENT_TIMESTAMP - INTERVAL '1' DAY, NULL, 'TX_KIN36000', 3),
    (6, 20000.00, 'PROCESANDO', CURRENT_TIMESTAMP, NULL, 'TX_PSI20000', 6),
    (10, 60000.00, 'PROCESANDO', CURRENT_TIMESTAMP, NULL, 'TX_AST60000', 4),

    -- Pagos FALLIDOS
    (14, 18000.00, 'FALLIDO', CURRENT_TIMESTAMP - INTERVAL '3' DAY, CURRENT_TIMESTAMP - INTERVAL '3' DAY, 'TX_KIN18000F', 2),

    -- Pagos REEMBOLSADOS
    (13, 19000.00, 'REEMBOLSADO', CURRENT_TIMESTAMP - INTERVAL '5' DAY, CURRENT_TIMESTAMP - INTERVAL '4' DAY, 'TX_TER19000R', 1);

-- =====================================
-- TURNOS (OPCIONAL)
-- =====================================
-- INSERT INTO turnos (paciente_id, prestador_id, transportista_id, fecha_hora, fecha_creacion, estado, observaciones)
-- VALUES
--     (1, 1, 1, CURRENT_TIMESTAMP + INTERVAL '1' DAY, CURRENT_TIMESTAMP, 'CONFIRMADO', 'Paciente requiere silla de ruedas'),
--     (2, 2, 2, CURRENT_TIMESTAMP + INTERVAL '2' DAY, CURRENT_TIMESTAMP, 'PENDIENTE', 'Traer estudios médicos'),
--     (3, 1, NULL, CURRENT_TIMESTAMP + INTERVAL '3' DAY, CURRENT_TIMESTAMP, 'CONFIRMADO', 'Control rutinario');