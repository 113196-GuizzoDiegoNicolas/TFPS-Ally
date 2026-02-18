-- ===========================================
-- ESPECIALIDADES CON PRECIOS
-- ===========================================
INSERT INTO specialties (codigo, nombre, importe_consulta) VALUES
                                                               ('KINESIOLOGIA', 'Kinesiología', 18000.00),
                                                               ('FONOAUDIOLOGIA', 'Fonoaudiología', 17000.00),
                                                               ('PSICOLOGIA', 'Psicología', 20000.00),
                                                               ('TERAPIA_OCUPACIONAL', 'Terapia Ocupacional', 19000.00),
                                                               ('ASISTENTE_TERAPEUTICO', 'Asistente Terapéutico', 15000.00),
                                                               ('TRANSPORTE_SANITARIO', 'Transporte Sanitario', 8000.00);

-- ===========================================
-- MÉTODOS DE PAGO
-- ===========================================
INSERT INTO metodos_pagos (metodo_pago) VALUES
                                            ('CONTADO'),
                                            ('TRANSFERENCIA_BANCARIA'),
                                            ('MERCADO_PAGO'),
                                            ('OBRA_SOCIAL');

-- ===========================================
-- USUARIOS DE PRUEBA
-- ===========================================
INSERT INTO usuarios (usuario, password, email, activo, bloqueado, intentos_fallidos, fecha_creacion, rol) VALUES
                                                                                                               ('maria_gomez', 'password123', 'maria.gomez@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('carlos_lopez', 'password123', 'carlos.lopez@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('ana_silva', 'password123', 'ana.silva@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('luciana_martinez', 'password123', 'luciana.martinez@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('dr_roberto', 'password123', 'roberto.kinesiologo@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PRESTADOR'),
                                                                                                               ('dra_laura', 'password123', 'laura.psicologa@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PRESTADOR'),
                                                                                                               ('admin_ally', 'password123', 'admin@ally.com', true, false, 0, CURRENT_TIMESTAMP, 'ADMIN');

-- ===========================================
-- PACIENTES
-- ===========================================
INSERT INTO patients (nombre, apellido, fecha_nacimiento, direccion, telefono, correo_electronico, usuario_id,
                      numero_historia_clinica, codigo_obra_social, nro_afiliado_obra_social, tipo_discapacidad) VALUES
                                                                                                                    ('María', 'Gómez', '1990-04-10', 'Córdoba 500', '3516000001', 'maria.gomez@email.com',
                                                                                                                     (SELECT id FROM usuarios WHERE usuario = 'maria_gomez'), 'HC-001', 'OSDE', 'OSDE-001-12345', 'Ninguna'),
                                                                                                                    ('Carlos', 'López', '1975-12-15', 'Av. Rafael Núñez 4587', '3516000002', 'carlos.lopez@email.com',
                                                                                                                     (SELECT id FROM usuarios WHERE usuario = 'carlos_lopez'), 'HC-002', 'SWISS_MEDICAL', 'SM-002-67890', 'Motora'),
                                                                                                                    ('Ana', 'Silva', '1995-11-08', 'Calle 27 de Abril 567', '3516000003', 'ana.silva@email.com',
                                                                                                                     (SELECT id FROM usuarios WHERE usuario = 'ana_silva'), 'HC-003', 'GALENO', 'GAL-003-54321', 'Ninguna'),
                                                                                                                    ('Luciana', 'Martínez', '1988-07-22', 'Bv. Illia 2345', '3516000004', 'luciana.martinez@email.com',
                                                                                                                     (SELECT id FROM usuarios WHERE usuario = 'luciana_martinez'), 'HC-004', 'OMINT', 'OMI-004-98765', 'Auditiva');

-- ===========================================
-- PRESTADORES
-- ===========================================
INSERT INTO providers (nombre, apellido, correo_electronico, direccion, telefono, activo, usuario_id,
                       codigo_especialidad, fecha_nacimiento) VALUES
                                                                  ('Roberto', 'Pérez', 'roberto.kinesiologo@email.com', 'Av. Colón 1234', '3515551111', true,
                                                                   (SELECT id FROM usuarios WHERE usuario = 'dr_roberto'),
                                                                   (SELECT id FROM specialties WHERE codigo = 'KINESIOLOGIA'), '1980-05-12'),
                                                                  ('Laura', 'Sosa', 'laura.psicologa@email.com', 'Bv. San Juan 345', '3515552222', true,
                                                                   (SELECT id FROM usuarios WHERE usuario = 'dra_laura'),
                                                                   (SELECT id FROM specialties WHERE codigo = 'PSICOLOGIA'), '1985-08-25');

-- ===========================================
-- SERVICIOS DE PRUEBA CON MONTO INCLUIDO
-- ===========================================
INSERT INTO services (fecha_solicitud, paciente_id, prestador_id, descripcion, especialidad, estado, monto) VALUES
                                                                                                                -- Servicios con MERCADO_PAGO (Pendientes de pago)
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '2 days', 1, 1, 'Consulta inicial de kinesiología', 'KINESIOLOGIA', 'PAGO_PENDIENTE', 18000.00),
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '1 day', 2, 2, 'Terapia psicológica semanal', 'PSICOLOGIA', 'PAGO_PENDIENTE', 20000.00),
                                                                                                                -- Servicios con CONTADO (Ya pagados)
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '5 days', 3, 1, 'Sesión de rehabilitación', 'KINESIOLOGIA', 'ACEPTADO', 18000.00),
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '4 days', 4, 2, 'Consulta psicológica', 'PSICOLOGIA', 'ACEPTADO', 20000.00),
                                                                                                                -- Servicios con TRANSFERENCIA_BANCARIA (En proceso)
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '3 days', 1, 2, 'Terapia grupal', 'PSICOLOGIA', 'ACEPTADO', 20000.00),
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '2 days', 3, 1, 'Seguimiento kinesiológico', 'KINESIOLOGIA', 'ACEPTADO', 18000.00),
                                                                                                                -- Servicios con OBRA_SOCIAL (Cobertura total)
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '6 days', 2, 1, 'Rehabilitación por obra social', 'KINESIOLOGIA', 'ACEPTADO', 18000.00),
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '5 days', 4, 2, 'Atención psicológica por cobertura', 'PSICOLOGIA', 'ACEPTADO', 20000.00),
                                                                                                                -- Servicios de otras especialidades
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '3 days', 1, 1, 'Consulta fonoaudiológica', 'FONOAUDIOLOGIA', 'ACEPTADO', 17000.00),
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '2 days', 2, 2, 'Terapia ocupacional', 'TERAPIA_OCUPACIONAL', 'ACEPTADO', 19000.00),
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '1 day', 3, 1, 'Asistencia terapéutica', 'ASISTENTE_TERAPEUTICO', 'ACEPTADO', 15000.00),
                                                                                                                (CURRENT_TIMESTAMP, 4, 2, 'Transporte sanitario', 'TRANSPORTE_SANITARIO', 'ACEPTADO', 8000.00);

-- ===========================================
-- PAGOS DE EJEMPLO PARA CADA MÉTODO
-- ===========================================
INSERT INTO payments (servicio_id, monto, estado_pago, fecha_creacion, fecha_pago, id_transaccion, metodo_pago_id, mensaje_error) VALUES
-- 1. PAGO CON MERCADO_PAGO (Pendiente - para crear preferencia)
(1, 18000.00, 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '2 days', NULL, 'MP_PREF_001',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'MERCADO_PAGO'),
 '{"email_pagador": "maria.gomez@email.com", "nombre_pagador": "María Gómez}'),

-- 2. PAGO CON MERCADO_PAGO (Pendiente - otro servicio)
(2, 20000.00, 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '1 day', NULL, 'MP_PREF_002',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'MERCADO_PAGO'),
 '{"email_pagador": "carlos.lopez@email.com", "nombre_pagador": "Carlos López"}'),

-- 3. PAGO CON CONTADO (Completado)
(3, 18000.00, 'COMPLETADO', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '4 days', 'CONT-001',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'CONTADO'),
 'Pago en efectivo al momento de la consulta'),

-- 4. PAGO CON CONTADO (Completado)
(4, 20000.00, 'COMPLETADO', CURRENT_TIMESTAMP - INTERVAL '4 days', CURRENT_TIMESTAMP - INTERVAL '3 days', 'CONT-002',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'CONTADO'),
 'Pago en efectivo'),

-- 5. PAGO CON TRANSFERENCIA_BANCARIA (Completado)
(5, 20000.00, 'COMPLETADO', CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'TRANS-001',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'TRANSFERENCIA_BANCARIA'),
 'Transferencia bancaria confirmada - CBU: 0720123456789012345678'),

-- 6. PAGO CON TRANSFERENCIA_BANCARIA (Pendiente)
(6, 18000.00, 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '2 days', NULL, 'TRANS-002',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'TRANSFERENCIA_BANCARIA'),
 'Esperando confirmación de transferencia'),

-- 7. PAGO CON OBRA_SOCIAL (Completado - cobertura total)
(7, 0.00, 'COMPLETADO', CURRENT_TIMESTAMP - INTERVAL '6 days', CURRENT_TIMESTAMP - INTERVAL '5 days', 'OS-001',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'OBRA_SOCIAL'),
 'Cobertura 100% por obra social - N° autorización: OSDE-20241204-001'),

-- 8. PAGO CON OBRA_SOCIAL (Completado - con copago)
(8, 5000.00, 'COMPLETADO', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '4 days', 'OS-002',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'OBRA_SOCIAL'),
 'Cobertura parcial - Copago paciente: $5000 - Autorización: OMINT-20241204-002');

-- ===========================================
-- ACTUALIZAR SERVICIOS EXISTENTES SI NO TIENEN MONTO
-- ===========================================
UPDATE services s
SET monto = (
    SELECT importe_consulta
    FROM specialties sp
    WHERE sp.codigo = s.especialidad
       OR sp.nombre = s.especialidad
)
WHERE s.monto IS NULL OR s.monto = 0;

-- ===========================================
-- Añadir datos de servicios distribuidos en los últimos 6 meses
-- ===========================================
INSERT INTO services (fecha_solicitud, paciente_id, prestador_id, descripcion, especialidad, estado, monto) VALUES
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '1 month', 1, 1, 'Seguimiento mensual - kinesiología', 'KINESIOLOGIA', 'ACEPTO', 18000.00),
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '2 months', 2, 2, 'Consulta psicológica - seguimiento', 'PSICOLOGIA', 'ACEPTO', 20000.00),
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '3 months', 3, 1, 'Sesión fonoaudiología', 'FONOAUDIOLOGIA', 'RECHAZADO', 17000.00),
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '4 months', 4, 2, 'Terapia ocupacional', 'TERAPIA_OCUPACIONAL', 'PAGO_PENDIENTE', 19000.00),
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '5 months', 1, 1, 'Rehabilitación intensiva', 'KINESIOLOGIA', 'ACEPTO', 18000.00),
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '6 months', 2, 2, 'Atención por obra social', 'TRANSPORTE_SANITARIO', 'ACEPTO', 8000.00);

-- ===========================================
-- Añadir pagos asociados a esos servicios
-- ===========================================
INSERT INTO payments (servicio_id, monto, estado_pago, fecha_creacion, fecha_pago, id_transaccion, metodo_pago_id, mensaje_error) VALUES
                                                                                                                                      ((SELECT id FROM services WHERE descripcion = 'Seguimiento mensual - kinesiología' LIMIT 1), 18000.00, 'COMPLETADO',
            CURRENT_TIMESTAMP - INTERVAL '1 month', CURRENT_TIMESTAMP - INTERVAL '1 month' + INTERVAL '1 hour',
    'TXN_CONT_1001', (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'CONTADO'), 'Pago contado'),

    ((SELECT id FROM services WHERE descripcion = 'Consulta psicológica - seguimiento' LIMIT 1), 20000.00, 'COMPLETADO',
    CURRENT_TIMESTAMP - INTERVAL '2 months', CURRENT_TIMESTAMP - INTERVAL '2 months' + INTERVAL '2 hours',
    'MP_PREF_1002', (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'MERCADO_PAGO'), '{"email_pagador":"carlos.lopez@email.com"}'),

    ((SELECT id FROM services WHERE descripcion = 'Sesión fonoaudiología' LIMIT 1), 17000.00, 'FALLIDO',
    CURRENT_TIMESTAMP - INTERVAL '3 months', NULL,
    'TXN_FAIL_1003', (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'MERCADO_PAGO'), 'Error en gateway'),

    ((SELECT id FROM services WHERE descripcion = 'Terapia ocupacional' LIMIT 1), 19000.00, 'PENDIENTE',
    CURRENT_TIMESTAMP - INTERVAL '4 months', NULL,
    'TRANS_1004', (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'TRANSFERENCIA_BANCARIA'), 'Esperando acreditación'),

    ((SELECT id FROM services WHERE descripcion = 'Rehabilitación intensiva' LIMIT 1), 18000.00, 'COMPLETADO',
    CURRENT_TIMESTAMP - INTERVAL '5 months', CURRENT_TIMESTAMP - INTERVAL '5 months' + INTERVAL '1 hour',
    'OS_1005', (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'OBRA_SOCIAL'), 'Cobertura total');

-- ===========================================
-- CORRECCIÓN DE ESTADOS
-- ===========================================
UPDATE services SET estado = 'ACEPTADO' WHERE estado = 'ACEPTO';
UPDATE services SET estado = 'PAGO_PENDIENTE' WHERE estado = 'PENDIENTE';

-- ===========================================
-- 50 PACIENTES EXTRA (USUARIOS + PATIENTS)
-- ===========================================
-- 1) USUARIOS (PACIENTE)
INSERT INTO usuarios (usuario, password, email, activo, bloqueado, intentos_fallidos, fecha_creacion, rol) VALUES
                                                                                                               ('paciente_001', 'password123', 'paciente_001@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               -- ... (resto de tus 50 usuarios, OK) ...
                                                                                                               ('paciente_050', 'password123', 'paciente_050@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE');

-- 2) PATIENTS (50 filas) - TU CÓDIGO ESTÁ CORRECTO, LO MANTENGO

-- ===========================================
-- 100 SERVICIOS EXTRA (CORREGIDOS CON INTERVAL)
-- ===========================================
INSERT INTO services (fecha_solicitud, paciente_id, prestador_id, descripcion, especialidad, estado, monto) VALUES
                                                                                                                -- Servicios con INTERVAL en lugar de DATEADD
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '3 days', (SELECT id FROM patients WHERE correo_electronico='paciente_001@email.com'), 1, 'AUTO_SVC_001 - Kinesiología inicial', 'KINESIOLOGIA', 'ACEPTADO', 18000.00),
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '10 days', (SELECT id FROM patients WHERE correo_electronico='paciente_001@email.com'), 2, 'AUTO_SVC_002 - Psicología mensual', 'PSICOLOGIA', 'PAGO_PENDIENTE', 20000.00),
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '7 days', (SELECT id FROM patients WHERE correo_electronico='paciente_002@email.com'), 1, 'AUTO_SVC_003 - Fonoaudiología evaluación', 'FONOAUDIOLOGIA', 'ACEPTADO', 17000.00),
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '18 days', (SELECT id FROM patients WHERE correo_electronico='paciente_002@email.com'), 2, 'AUTO_SVC_004 - Terapia ocupacional', 'TERAPIA_OCUPACIONAL', 'ACEPTADO', 19000.00),
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '12 days', (SELECT id FROM patients WHERE correo_electronico='paciente_003@email.com'), 1, 'AUTO_SVC_005 - Transporte sanitario', 'TRANSPORTE_SANITARIO', 'ACEPTADO', 8000.00),
                                                                                                                (CURRENT_TIMESTAMP - INTERVAL '25 days', (SELECT id FROM patients WHERE correo_electronico='paciente_003@email.com'), 2, 'AUTO_SVC_006 - Asistente terapéutico', 'ASISTENTE_TERAPEUTICO', 'RECHAZADO', 15000.00),
                                                                                                                -- ... (continuar con el resto, cambiando todos los DATEADD por INTERVAL)

-- ===========================================
-- RESET DE SECUENCIAS PARA POSTGRESQL (VERSIÓN GENÉRICA)
-- ===========================================
-- PostgreSQL usa nombres de secuencia por defecto: tabla_columna_seq
-- Esta versión funciona incluso si los nombres no son exactamente esos
    DO $$
DECLARE
seq_name text;
    max_id bigint;
BEGIN
    -- Usuarios
SELECT pg_get_serial_sequence('usuarios', 'id') INTO seq_name;
IF seq_name IS NOT NULL THEN
        EXECUTE 'SELECT setval(''' || seq_name || ''', (SELECT COALESCE(MAX(id), 1) FROM usuarios))';
END IF;

    -- Patients
SELECT pg_get_serial_sequence('patients', 'id') INTO seq_name;
IF seq_name IS NOT NULL THEN
        EXECUTE 'SELECT setval(''' || seq_name || ''', (SELECT COALESCE(MAX(id), 1) FROM patients))';
END IF;

    -- Providers
SELECT pg_get_serial_sequence('providers', 'id') INTO seq_name;
IF seq_name IS NOT NULL THEN
        EXECUTE 'SELECT setval(''' || seq_name || ''', (SELECT COALESCE(MAX(id), 1) FROM providers))';
END IF;

    -- Services
SELECT pg_get_serial_sequence('services', 'id') INTO seq_name;
IF seq_name IS NOT NULL THEN
        EXECUTE 'SELECT setval(''' || seq_name || ''', (SELECT COALESCE(MAX(id), 1) FROM services))';
END IF;

    -- Payments
SELECT pg_get_serial_sequence('payments', 'id') INTO seq_name;
IF seq_name IS NOT NULL THEN
        EXECUTE 'SELECT setval(''' || seq_name || ''', (SELECT COALESCE(MAX(id), 1) FROM payments))';
END IF;

    -- Specialties
SELECT pg_get_serial_sequence('specialties', 'id') INTO seq_name;
IF seq_name IS NOT NULL THEN
        EXECUTE 'SELECT setval(''' || seq_name || ''', (SELECT COALESCE(MAX(id), 1) FROM specialties))';
END IF;

    -- Metodos pagos
SELECT pg_get_serial_sequence('metodos_pagos', 'metodo_pago_id') INTO seq_name;
IF seq_name IS NOT NULL THEN
        EXECUTE 'SELECT setval(''' || seq_name || ''', (SELECT COALESCE(MAX(metodo_pago_id), 1) FROM metodos_pagos))';
END IF;
END $$;
-- ===========================================
-- UBICACIONES DE PACIENTES (DEMO)
-- ===========================================
INSERT INTO patient_locations (paciente_id, lat, lng, address_text, updated_at) VALUES
                                                                                    (1, -31.4167, -64.1833, 'Centro, Córdoba', CURRENT_TIMESTAMP),
                                                                                    (2, -31.3789, -64.2331, 'Cerro de las Rosas, Córdoba', CURRENT_TIMESTAMP),
                                                                                    (3, -31.4201, -64.1888, 'Nueva Córdoba, Córdoba', CURRENT_TIMESTAMP),
                                                                                    (4, -31.4040, -64.1750, 'General Paz, Córdoba', CURRENT_TIMESTAMP);