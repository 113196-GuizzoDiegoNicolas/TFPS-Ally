-- ===========================================
-- LIMPIAR DATOS EXISTENTES (AGREGADO PARA EVITAR ERRORES)
-- ===========================================
DELETE FROM payments;
DELETE FROM services;
DELETE FROM providers;
DELETE FROM patients;
DELETE FROM usuarios;
DELETE FROM metodos_pagos;
DELETE FROM specialties;

-- Reiniciar secuencias (para H2)
ALTER TABLE specialties ALTER COLUMN id RESTART WITH 1;
ALTER TABLE metodos_pagos ALTER COLUMN metodo_pago_id RESTART WITH 1;
ALTER TABLE usuarios ALTER COLUMN id RESTART WITH 1;
ALTER TABLE patients ALTER COLUMN id RESTART WITH 1;
ALTER TABLE providers ALTER COLUMN id RESTART WITH 1;
ALTER TABLE services ALTER COLUMN id RESTART WITH 1;
ALTER TABLE payments ALTER COLUMN id RESTART WITH 1;

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
-- MÉTODOS DE PAGO (Solo los 4 especificados)
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
-- Pacientes
('maria_gomez', 'password123', 'maria.gomez@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
('carlos_lopez', 'password123', 'carlos.lopez@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
('ana_silva', 'password123', 'ana.silva@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
('luciana_martinez', 'password123', 'luciana.martinez@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
-- Prestadores
('dr_roberto', 'password123', 'roberto.kinesiologo@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PRESTADOR'),
('dra_laura', 'password123', 'laura.psicologa@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PRESTADOR');

-- ===========================================
-- PACIENTES
-- ===========================================
INSERT INTO patients (nombre, apellido, fecha_nacimiento, direccion, telefono, correo_electronico, usuario_id, numero_historia_clinica, codigo_obra_social, nro_afiliado_obra_social, tipo_discapacidad) VALUES
                                                                                                                                                                                                             ('María', 'Gómez', '1990-04-10', 'Córdoba 500', '3516000001', 'maria.gomez@email.com', 1, 'HC-001', 'OSDE', 'OSDE-001-12345', 'Ninguna'),
                                                                                                                                                                                                             ('Carlos', 'López', '1975-12-15', 'Av. Rafael Núñez 4587', '3516000002', 'carlos.lopez@email.com', 2, 'HC-002', 'SWISS_MEDICAL', 'SM-002-67890', 'Motora'),
                                                                                                                                                                                                             ('Ana', 'Silva', '1995-11-08', 'Calle 27 de Abril 567', '3516000003', 'ana.silva@email.com', 3, 'HC-003', 'GALENO', 'GAL-003-54321', 'Ninguna'),
                                                                                                                                                                                                             ('Luciana', 'Martínez', '1988-07-22', 'Bv. Illia 2345', '3516000004', 'luciana.martinez@email.com', 4, 'HC-004', 'OMINT', 'OMI-004-98765', 'Auditiva');

-- ===========================================
-- PRESTADORES
-- ===========================================
INSERT INTO providers (nombre, apellido, correo_electronico, direccion, telefono, activo, usuario_id, codigo_especialidad, fecha_nacimiento) VALUES
                                                                                                                                                 ('Roberto', 'Pérez', 'roberto.kinesiologo@email.com', 'Av. Colón 1234', '3515551111', true, 5, 1, '1980-05-12'),
                                                                                                                                                 ('Laura', 'Sosa', 'laura.psicologa@email.com', 'Bv. San Juan 345', '3515552222', true, 6, 3, '1985-08-25');

-- ===========================================
-- SERVICIOS DE PRUEBA CON MONTO INCLUIDO
-- ===========================================
INSERT INTO services (fecha_solicitud, paciente_id, prestador_id, descripcion, especialidad, estado, monto) VALUES
-- Servicios con MERCADO_PAGO (Pendientes de pago)
(DATEADD('DAY', -2, CURRENT_TIMESTAMP), 1, 1, 'Consulta inicial de kinesiología', 'KINESIOLOGIA', 'PAGO_PENDIENTE', 18000.00),
(DATEADD('DAY', -1, CURRENT_TIMESTAMP), 2, 2, 'Terapia psicológica semanal', 'PSICOLOGIA', 'PAGO_PENDIENTE', 20000.00),

-- Servicios con CONTADO (Ya pagados)
(DATEADD('DAY', -5, CURRENT_TIMESTAMP), 3, 1, 'Sesión de rehabilitación', 'KINESIOLOGIA', 'ACEPTADO', 18000.00),
(DATEADD('DAY', -4, CURRENT_TIMESTAMP), 4, 2, 'Consulta psicológica', 'PSICOLOGIA', 'ACEPTADO', 20000.00),

-- Servicios con TRANSFERENCIA_BANCARIA (En proceso)
(DATEADD('DAY', -3, CURRENT_TIMESTAMP), 1, 2, 'Terapia grupal', 'PSICOLOGIA', 'ACEPTADO', 20000.00),
(DATEADD('DAY', -2, CURRENT_TIMESTAMP), 3, 1, 'Seguimiento kinesiológico', 'KINESIOLOGIA', 'ACEPTADO', 18000.00),

-- Servicios con OBRA_SOCIAL (Cobertura total)
(DATEADD('DAY', -6, CURRENT_TIMESTAMP), 2, 1, 'Rehabilitación por obra social', 'KINESIOLOGIA', 'ACEPTADO', 18000.00),
(DATEADD('DAY', -5, CURRENT_TIMESTAMP), 4, 2, 'Atención psicológica por cobertura', 'PSICOLOGIA', 'ACEPTADO', 20000.00),

-- Servicios de otras especialidades
(DATEADD('DAY', -3, CURRENT_TIMESTAMP), 1, 1, 'Consulta fonoaudiológica', 'FONOAUDIOLOGIA', 'ACEPTADO', 17000.00),
(DATEADD('DAY', -2, CURRENT_TIMESTAMP), 2, 2, 'Terapia ocupacional', 'TERAPIA_OCUPACIONAL', 'ACEPTADO', 19000.00),
(DATEADD('DAY', -1, CURRENT_TIMESTAMP), 3, 1, 'Asistencia terapéutica', 'ASISTENTE_TERAPEUTICO', 'ACEPTADO', 15000.00),
(CURRENT_TIMESTAMP, 4, 2, 'Transporte sanitario', 'TRANSPORTE_SANITARIO', 'ACEPTADO', 8000.00);

-- ===========================================
-- PAGOS DE EJEMPLO PARA CADA MÉTODO
-- ===========================================
INSERT INTO payments (servicio_id, monto, estado_pago, fecha_creacion, fecha_pago, id_transaccion, metodo_pago_id, mensaje_error) VALUES
-- 1. PAGO CON MERCADO_PAGO (Pendiente - para crear preferencia)
(1, 18000.00, 'PENDIENTE', DATEADD('DAY', -2, CURRENT_TIMESTAMP), NULL, 'MP_PREF_001',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'MERCADO_PAGO'),
 '{"email_pagador": "maria.gomez@email.com", "nombre_pagador": "María Gómez"}'),

-- 2. PAGO CON MERCADO_PAGO (Pendiente - otro servicio)
(2, 20000.00, 'PENDIENTE', DATEADD('DAY', -1, CURRENT_TIMESTAMP), NULL, 'MP_PREF_002',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'MERCADO_PAGO'),
 '{"email_pagador": "carlos.lopez@email.com", "nombre_pagador": "Carlos López"}'),

-- 3. PAGO CON CONTADO (Completado)
(3, 18000.00, 'COMPLETADO', DATEADD('DAY', -5, CURRENT_TIMESTAMP), DATEADD('DAY', -4, CURRENT_TIMESTAMP), 'CONT-001',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'CONTADO'),
 'Pago en efectivo al momento de la consulta'),

-- 4. PAGO CON CONTADO (Completado)
(4, 20000.00, 'COMPLETADO', DATEADD('DAY', -4, CURRENT_TIMESTAMP), DATEADD('DAY', -3, CURRENT_TIMESTAMP), 'CONT-002',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'CONTADO'),
 'Pago en efectivo'),

-- 5. PAGO CON TRANSFERENCIA_BANCARIA (Completado)
(5, 20000.00, 'COMPLETADO', DATEADD('DAY', -3, CURRENT_TIMESTAMP), DATEADD('DAY', -2, CURRENT_TIMESTAMP), 'TRANS-001',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'TRANSFERENCIA_BANCARIA'),
 'Transferencia bancaria confirmada - CBU: 0720123456789012345678'),

-- 6. PAGO CON TRANSFERENCIA_BANCARIA (Pendiente)
(6, 18000.00, 'PENDIENTE', DATEADD('DAY', -2, CURRENT_TIMESTAMP), NULL, 'TRANS-002',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'TRANSFERENCIA_BANCARIA'),
 'Esperando confirmación de transferencia'),

-- 7. PAGO CON OBRA_SOCIAL (Completado - cobertura total)
(7, 0.00, 'COMPLETADO', DATEADD('DAY', -6, CURRENT_TIMESTAMP), DATEADD('DAY', -5, CURRENT_TIMESTAMP), 'OS-001',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'OBRA_SOCIAL'),
 'Cobertura 100% por obra social - N° autorización: OSDE-20241204-001'),

-- 8. PAGO CON OBRA_SOCIAL (Completado - con copago)
(8, 5000.00, 'COMPLETADO', DATEADD('DAY', -5, CURRENT_TIMESTAMP), DATEADD('DAY', -4, CURRENT_TIMESTAMP), 'OS-002',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'OBRA_SOCIAL'),
 'Cobertura parcial - Copago paciente: $5000 - Autorización: OMINT-20241204-002');

-- ===========================================
-- USUARIO ADMIN
-- ===========================================
INSERT INTO usuarios (
    usuario, password, email, activo, bloqueado, intentos_fallidos, fecha_creacion, rol
) VALUES (
             'admin_ally',
             'password123',
             'admin@ally.com',
             true,
             false,
             0,
             CURRENT_TIMESTAMP,
             'ADMIN'
         );

-- ===========================================
-- ACTUALIZAR SERVICIOS EXISTENTES SI NO TIENEN MONTO
-- ===========================================
-- Esta consulta actualiza los servicios que no tienen monto
-- asignando el monto según la especialidad
UPDATE services s
SET monto = (
    SELECT importe_consulta
    FROM specialties sp
    WHERE sp.codigo = s.especialidad
       OR sp.nombre = s.especialidad
)
WHERE s.monto IS NULL OR s.monto = 0;


-- ===========================================
-- Añadir datos de servicios distribuidos en los últimos 6 meses (varios estados)
-- ===========================================
INSERT INTO services (fecha_solicitud, paciente_id, prestador_id, descripcion, especialidad, estado, monto) VALUES
                                                                                                                (DATEADD('MONTH', -1, CURRENT_TIMESTAMP), 1, 1, 'Seguimiento mensual - kinesiología', 'KINESIOLOGIA', 'ACEPTO', 18000.00),
                                                                                                                (DATEADD('MONTH', -2, CURRENT_TIMESTAMP), 2, 2, 'Consulta psicológica - seguimiento', 'PSICOLOGIA', 'ACEPTO', 20000.00),
                                                                                                                (DATEADD('MONTH', -3, CURRENT_TIMESTAMP), 3, 1, 'Sesión fonoaudiología', 'FONOAUDIOLOGIA', 'RECHAZADO', 17000.00),
                                                                                                                (DATEADD('MONTH', -4, CURRENT_TIMESTAMP), 4, 2, 'Terapia ocupacional', 'TERAPIA_OCUPACIONAL', 'PAGO_PENDIENTE', 19000.00),
                                                                                                                (DATEADD('MONTH', -5, CURRENT_TIMESTAMP), 1, 1, 'Rehabilitación intensiva', 'KINESIOLOGIA', 'ACEPTO', 18000.00),
                                                                                                                (DATEADD('MONTH', -6, CURRENT_TIMESTAMP), 2, 2, 'Atención por obra social', 'TRANSPORTE_SANITARIO', 'ACEPTO', 8000.00);

-- ===========================================
-- Añadir pagos asociados a esos servicios (métodos variados)
-- ===========================================
INSERT INTO payments (servicio_id, monto, estado_pago, fecha_creacion, fecha_pago, id_transaccion, metodo_pago_id, mensaje_error) VALUES
                                                                                                                                      ((SELECT id FROM services WHERE descripcion = 'Seguimiento mensual - kinesiología' LIMIT 1), 18000.00, 'COMPLETADO',
    DATEADD('MONTH', -1, CURRENT_TIMESTAMP), DATEADD('HOUR', 1, DATEADD('MONTH', -1, CURRENT_TIMESTAMP)),
    'TXN_CONT_1001', (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'CONTADO'), 'Pago contado'),

                                                                                                                                      ((SELECT id FROM services WHERE descripcion = 'Consulta psicológica - seguimiento' LIMIT 1), 20000.00, 'COMPLETADO',
                                                                                                                                       DATEADD('MONTH', -2, CURRENT_TIMESTAMP), DATEADD('HOUR', 2, DATEADD('MONTH', -2, CURRENT_TIMESTAMP)),
                                                                                                                                       'MP_PREF_1002', (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'MERCADO_PAGO'), '{"email_pagador":"carlos.lopez@email.com"}'),

                                                                                                                                      ((SELECT id FROM services WHERE descripcion = 'Sesión fonoaudiología' LIMIT 1), 17000.00, 'FALLIDO',
                                                                                                                                       DATEADD('MONTH', -3, CURRENT_TIMESTAMP), NULL,
                                                                                                                                       'TXN_FAIL_1003', (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'MERCADO_PAGO'), 'Error en gateway'),

                                                                                                                                      ((SELECT id FROM services WHERE descripcion = 'Terapia ocupacional' LIMIT 1), 19000.00, 'PENDIENTE',
                                                                                                                                       DATEADD('MONTH', -4, CURRENT_TIMESTAMP), NULL,
                                                                                                                                       'TRANS_1004', (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'TRANSFERENCIA_BANCARIA'), 'Esperando acreditación'),

                                                                                                                                      ((SELECT id FROM services WHERE descripcion = 'Rehabilitación intensiva' LIMIT 1), 18000.00, 'COMPLETADO',
                                                                                                                                       DATEADD('MONTH', -5, CURRENT_TIMESTAMP), DATEADD('HOUR', 1, DATEADD('MONTH', -5, CURRENT_TIMESTAMP)),
                                                                                                                                       'OS_1005', (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'OBRA_SOCIAL'), 'Cobertura total');

-- ===========================================
-- Consultas útiles para los gráficos del admin
-- ===========================================

-- 1) Conteo de servicios por estado (barras/pie)
SELECT s.estado, COUNT(*) AS cantidad
FROM services s
GROUP BY s.estado
ORDER BY cantidad DESC;

-- 2) Totales y cantidad de pagos por método de pago (barra)
SELECT mp.metodo_pago, COALESCE(SUM(p.monto),0) AS total_monto, COUNT(p.id) AS cantidad_pagos
FROM metodos_pagos mp
         LEFT JOIN payments p ON p.metodo_pago_id = mp.metodo_pago_id
GROUP BY mp.metodo_pago
ORDER BY total_monto DESC;

-- 3) Tendencia mensual de pagos (últimos 6 meses) - (línea)
-- Para H2, usamos EXTRACT y FORMATDATETIME
SELECT FORMATDATETIME(COALESCE(p.fecha_pago, p.fecha_creacion), 'yyyy-MM') AS mes,
       COALESCE(SUM(p.monto),0) AS total_mes,
       COUNT(p.id) AS cantidad_pagos
FROM payments p
WHERE COALESCE(p.fecha_pago, p.fecha_creacion) >= DATEADD('MONTH', -6, CURRENT_DATE)
GROUP BY FORMATDATETIME(COALESCE(p.fecha_pago, p.fecha_creacion), 'yyyy-MM')
ORDER BY mes;

-- 4) Servicios por especialidad (pie/bar)
SELECT s.especialidad, COUNT(*) AS cantidad_servicios
FROM services s
GROUP BY s.especialidad
ORDER BY cantidad_servicios DESC;

-- 5) Proveedores activos por especialidad (barra)
SELECT sp.nombre AS especialidad, COUNT(pv.id) AS cantidad_prestadores
FROM providers pv
         JOIN specialties sp ON pv.codigo_especialidad = sp.id
WHERE pv.activo = true
GROUP BY sp.nombre
ORDER BY cantidad_prestadores DESC;

-- 6) Usuarios nuevos por rol en los últimos 6 meses (stacked-bar por mes)
SELECT u.rol, FORMATDATETIME(u.fecha_creacion, 'yyyy-MM') AS mes, COUNT(*) AS cantidad
FROM usuarios u
WHERE u.fecha_creacion >= DATEADD('MONTH', -6, CURRENT_DATE)
GROUP BY u.rol, FORMATDATETIME(u.fecha_creacion, 'yyyy-MM')
ORDER BY mes, u.rol;

-- 7) Tasa de conversión: servicios con pago completado / total servicios (últimos 6 meses)
SELECT
    SUM(CASE WHEN p.estado_pago = 'COMPLETADO' THEN 1 ELSE 0 END) * 1.0 /
    NULLIF(COUNT(DISTINCT s.id),0) AS tasa_conversion
FROM services s
         LEFT JOIN payments p ON p.servicio_id = s.id
WHERE s.fecha_solicitud >= DATEADD('MONTH', -6, CURRENT_DATE);

UPDATE SERVICES SET estado='ACEPTADO' WHERE estado='ACEPTO';
UPDATE services
SET estado = 'PAGO_PENDIENTE'
WHERE estado = 'PENDIENTE';
-- ===========================================
-- 50 PACIENTES EXTRA (USUARIOS + PATIENTS)
-- Sin asumir IDs: se vincula usando SELECT por usuario
-- ===========================================

-- 1) USUARIOS (PACIENTE)
INSERT INTO usuarios (usuario, password, email, activo, bloqueado, intentos_fallidos, fecha_creacion, rol) VALUES
                                                                                                               ('paciente_001', 'password123', 'paciente_001@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_002', 'password123', 'paciente_002@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_003', 'password123', 'paciente_003@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_004', 'password123', 'paciente_004@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_005', 'password123', 'paciente_005@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_006', 'password123', 'paciente_006@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_007', 'password123', 'paciente_007@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_008', 'password123', 'paciente_008@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_009', 'password123', 'paciente_009@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_010', 'password123', 'paciente_010@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),

                                                                                                               ('paciente_011', 'password123', 'paciente_011@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_012', 'password123', 'paciente_012@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_013', 'password123', 'paciente_013@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_014', 'password123', 'paciente_014@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_015', 'password123', 'paciente_015@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_016', 'password123', 'paciente_016@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_017', 'password123', 'paciente_017@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_018', 'password123', 'paciente_018@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_019', 'password123', 'paciente_019@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_020', 'password123', 'paciente_020@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),

                                                                                                               ('paciente_021', 'password123', 'paciente_021@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_022', 'password123', 'paciente_022@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_023', 'password123', 'paciente_023@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_024', 'password123', 'paciente_024@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_025', 'password123', 'paciente_025@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_026', 'password123', 'paciente_026@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_027', 'password123', 'paciente_027@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_028', 'password123', 'paciente_028@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_029', 'password123', 'paciente_029@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_030', 'password123', 'paciente_030@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),

                                                                                                               ('paciente_031', 'password123', 'paciente_031@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_032', 'password123', 'paciente_032@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_033', 'password123', 'paciente_033@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_034', 'password123', 'paciente_034@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_035', 'password123', 'paciente_035@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_036', 'password123', 'paciente_036@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_037', 'password123', 'paciente_037@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_038', 'password123', 'paciente_038@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_039', 'password123', 'paciente_039@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_040', 'password123', 'paciente_040@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),

                                                                                                               ('paciente_041', 'password123', 'paciente_041@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_042', 'password123', 'paciente_042@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_043', 'password123', 'paciente_043@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_044', 'password123', 'paciente_044@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_045', 'password123', 'paciente_045@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_046', 'password123', 'paciente_046@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_047', 'password123', 'paciente_047@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_048', 'password123', 'paciente_048@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_049', 'password123', 'paciente_049@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE'),
                                                                                                               ('paciente_050', 'password123', 'paciente_050@email.com', true, false, 0, CURRENT_TIMESTAMP, 'PACIENTE');

-- 2) PATIENTS (50 filas) vinculados por usuario
-- Nota: tipo_discapacidad variado para tus filtros/reportes.
INSERT INTO patients (nombre, apellido, fecha_nacimiento, direccion, telefono, correo_electronico, usuario_id,
                      numero_historia_clinica, codigo_obra_social, nro_afiliado_obra_social, tipo_discapacidad)
VALUES
    ('Sofía','Fernández','1992-03-14','Av. Colón 110','3517000001','paciente_001@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_001'), 'HC-005','OSDE','OSDE-005-10001','Ninguna'),
    ('Mateo','García','1986-07-22','Bv. San Juan 220','3517000002','paciente_002@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_002'), 'HC-006','SWISS_MEDICAL','SM-006-10002','Motora'),
    ('Valentina','Luna','1999-01-09','27 de Abril 330','3517000003','paciente_003@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_003'), 'HC-007','GALENO','GAL-007-10003','Auditiva'),
    ('Joaquín','Ruiz','1979-11-05','Av. Rafael Núñez 440','3517000004','paciente_004@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_004'), 'HC-008','OMINT','OMI-008-10004','Visual'),
    ('Camila','Paz','1994-06-18','Bv. Illia 550','3517000005','paciente_005@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_005'), 'HC-009','OSDE','OSDE-009-10005','Ninguna'),

    ('Thiago','Ramos','1983-02-27','Av. Sabattini 120','3517000006','paciente_006@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_006'), 'HC-010','GALENO','GAL-010-10006','Motora'),
    ('Martina','Sosa','1996-12-02','Av. Vélez Sarsfield 980','3517000007','paciente_007@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_007'), 'HC-011','OMINT','OMI-011-10007','Ninguna'),
    ('Benjamín','Navarro','1972-09-10','Duarte Quirós 410','3517000008','paciente_008@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_008'), 'HC-012','SWISS_MEDICAL','SM-012-10008','Intelectual'),
    ('Lucía','Herrera','1989-04-30','Santa Rosa 700','3517000009','paciente_009@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_009'), 'HC-013','OSDE','OSDE-013-10009','Auditiva'),
    ('Agustín','Molina','2000-08-21','General Paz 333','3517000010','paciente_010@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_010'), 'HC-014','GALENO','GAL-014-10010','Ninguna'),

    ('Mía','Ortega','1991-10-12','Ituzaingó 245','3517000011','paciente_011@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_011'), 'HC-015','OMINT','OMI-015-10011','Visual'),
    ('Franco','Castro','1984-05-03','Av. Alem 88','3517000012','paciente_012@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_012'), 'HC-016','OSDE','OSDE-016-10012','Motora'),
    ('Renata','Vega','1997-02-08','Chacabuco 610','3517000013','paciente_013@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_013'), 'HC-017','SWISS_MEDICAL','SM-017-10013','Ninguna'),
    ('Tomás','Aguirre','1978-06-25','Av. Patria 760','3517000014','paciente_014@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_014'), 'HC-018','GALENO','GAL-018-10014','Auditiva'),
    ('Catalina','Domínguez','1993-09-16','Colón 1540','3517000015','paciente_015@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_015'), 'HC-019','OMINT','OMI-019-10015','Ninguna'),

    ('Nicolás','Peralta','1981-01-28','San Martín 90','3517000016','paciente_016@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_016'), 'HC-020','OSDE','OSDE-020-10016','Motora'),
    ('Emma','Suárez','1998-11-19','Tucumán 111','3517000017','paciente_017@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_017'), 'HC-021','GALENO','GAL-021-10017','Ninguna'),
    ('Lautaro','Campos','1987-03-06','Av. Fuerza Aérea 212','3517000018','paciente_018@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_018'), 'HC-022','SWISS_MEDICAL','SM-022-10018','Visual'),
    ('Isabella','Ríos','1995-07-13','Sarmiento 420','3517000019','paciente_019@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_019'), 'HC-023','OMINT','OMI-023-10019','Intelectual'),
    ('Felipe','Torres','1974-12-01','Belgrano 510','3517000020','paciente_020@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_020'), 'HC-024','OSDE','OSDE-024-10020','Auditiva'),

    ('Abril','Giménez','1990-05-20','Av. Donato Álvarez 66','3517000021','paciente_021@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_021'), 'HC-025','GALENO','GAL-025-10021','Ninguna'),
    ('Bruno','Silva','1982-08-14','Rivera Indarte 250','3517000022','paciente_022@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_022'), 'HC-026','SWISS_MEDICAL','SM-026-10022','Motora'),
    ('Julia','Acosta','1999-09-09','Alvear 900','3517000023','paciente_023@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_023'), 'HC-027','OSDE','OSDE-027-10023','Ninguna'),
    ('Simón','Figueroa','1977-02-02','Av. Olmos 333','3517000024','paciente_024@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_024'), 'HC-028','OMINT','OMI-028-10024','Visual'),
    ('Olivia','Ibarra','1994-04-17','Dean Funes 800','3517000025','paciente_025@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_025'), 'HC-029','GALENO','GAL-029-10025','Auditiva'),

    ('Ramiro','Ponce','1988-06-07','Av. Costanera 120','3517000026','paciente_026@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_026'), 'HC-030','SWISS_MEDICAL','SM-030-10026','Ninguna'),
    ('Victoria','Méndez','1996-01-23','Cañada 450','3517000027','paciente_027@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_027'), 'HC-031','OSDE','OSDE-031-10027','Motora'),
    ('Dante','López','1973-10-29','Av. Gauss 155','3517000028','paciente_028@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_028'), 'HC-032','OMINT','OMI-032-10028','Ninguna'),
    ('Alma','Romero','1992-12-15','Matienzo 300','3517000029','paciente_029@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_029'), 'HC-033','GALENO','GAL-033-10029','Intelectual'),
    ('Ignacio','Serrano','1985-05-11','Av. América 101','3517000030','paciente_030@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_030'), 'HC-034','SWISS_MEDICAL','SM-034-10030','Visual'),

    ('Pilar','Quiroga','1997-07-28','Obispo Trejo 70','3517000031','paciente_031@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_031'), 'HC-035','OSDE','OSDE-035-10031','Ninguna'),
    ('Facundo','Cabrera','1976-03-19','Av. Castro Barros 200','3517000032','paciente_032@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_032'), 'HC-036','OMINT','OMI-036-10032','Motora'),
    ('Lola','Rey','1991-11-03','9 de Julio 650','3517000033','paciente_033@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_033'), 'HC-037','GALENO','GAL-037-10033','Auditiva'),
    ('Ezequiel','Núñez','1983-09-24','Av. Circunvalación 500','3517000034','paciente_034@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_034'), 'HC-038','SWISS_MEDICAL','SM-038-10034','Ninguna'),
    ('Ambar','Sánchez','1998-02-26','Pueyrredón 140','3517000035','paciente_035@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_035'), 'HC-039','OSDE','OSDE-039-10035','Visual'),

    ('León','Morales','1979-06-02','Av. O''Higgins 320','3517000036','paciente_036@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_036'), 'HC-040','OMINT','OMI-040-10036','Ninguna'),
    ('Zoe','Paredes','1995-01-30','Av. Carcano 77','3517000037','paciente_037@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_037'), 'HC-041','GALENO','GAL-041-10037','Motora'),
    ('Julián','Vidal','1986-10-08','Av. Malvinas 430','3517000038','paciente_038@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_038'), 'HC-042','SWISS_MEDICAL','SM-042-10038','Ninguna'),
    ('Clara','Roldán','1993-03-12','Bajada Pucará 500','3517000039','paciente_039@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_039'), 'HC-043','OSDE','OSDE-043-10039','Auditiva'),
    ('Santiago','Arias','1980-12-20','Av. Perón 950','3517000040','paciente_040@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_040'), 'HC-044','OMINT','OMI-044-10040','Intelectual'),

    ('Noa','Medina','2001-04-01','Av. Sagrada Familia 120','3517000041','paciente_041@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_041'), 'HC-045','GALENO','GAL-045-10041','Ninguna'),
    ('Gael','Benítez','1989-08-27','Mariano Fragueiro 225','3517000042','paciente_042@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_042'), 'HC-046','SWISS_MEDICAL','SM-046-10042','Motora'),
    ('Aitana','Ferreyra','1996-06-05','Av. La Voz del Interior 123','3517000043','paciente_043@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_043'), 'HC-047','OSDE','OSDE-047-10043','Ninguna'),
    ('Bautista','Vázquez','1975-02-11','Av. Monseñor Pablo Cabrera 800','3517000044','paciente_044@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_044'), 'HC-048','OMINT','OMI-048-10044','Visual'),
    ('Malena','Correa','1992-09-29','La Rioja 302','3517000045','paciente_045@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_045'), 'HC-049','GALENO','GAL-049-10045','Auditiva'),

    ('Tiago','Santiago','1984-01-07','Av. Santa Fe 555','3517000046','paciente_046@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_046'), 'HC-050','OSDE','OSDE-050-10046','Ninguna'),
    ('Florencia','Márquez','1999-12-22','Av. Corrientes 123','3517000047','paciente_047@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_047'), 'HC-051','SWISS_MEDICAL','SM-051-10047','Motora'),
    ('Lucas','Guzmán','1978-07-15','Av. Chacabuco 321','3517000048','paciente_048@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_048'), 'HC-052','OMINT','OMI-052-10048','Ninguna'),
    ('Jazmín','Martínez','1991-05-09','Av. Estrada 456','3517000049','paciente_049@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_049'), 'HC-053','GALENO','GAL-053-10049','Intelectual'),
    ('Alan','Prieto','1987-11-26','Av. Colón 2222','3517000050','paciente_050@email.com',
     (SELECT id FROM usuarios WHERE usuario='paciente_050'), 'HC-054','OSDE','OSDE-054-10050','Visual');


-- ==========================================================
-- 100 SERVICIOS EXTRA (últimos 6 meses) PARA 50 PACIENTES
-- + 100 PAGOS ASOCIADOS (métodos variados)
-- Sin depender de IDs: se relaciona por correo/descripcion
-- ==========================================================

-- ==========================
-- 1) SERVICIOS (100)
-- ==========================
INSERT INTO services (fecha_solicitud, paciente_id, prestador_id, descripcion, especialidad, estado, monto) VALUES

-- 1..50 (2 servicios por paciente, distribuidos)
(DATEADD('DAY', -3,  CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_001@email.com'), 1, 'AUTO_SVC_001 - Kinesiología inicial', 'KINESIOLOGIA', 'ACEPTADO', 18000.00),
(DATEADD('DAY', -10, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_001@email.com'), 2, 'AUTO_SVC_002 - Psicología mensual', 'PSICOLOGIA', 'PAGO_PENDIENTE', 20000.00),

(DATEADD('DAY', -7,  CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_002@email.com'), 1, 'AUTO_SVC_003 - Fonoaudiología evaluación', 'FONOAUDIOLOGIA', 'ACEPTADO', 17000.00),
(DATEADD('DAY', -18, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_002@email.com'), 2, 'AUTO_SVC_004 - Terapia ocupacional', 'TERAPIA_OCUPACIONAL', 'ACEPTADO', 19000.00),

(DATEADD('DAY', -12, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_003@email.com'), 1, 'AUTO_SVC_005 - Transporte sanitario', 'TRANSPORTE_SANITARIO', 'ACEPTADO', 8000.00),
(DATEADD('DAY', -25, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_003@email.com'), 2, 'AUTO_SVC_006 - Asistente terapéutico', 'ASISTENTE_TERAPEUTICO', 'RECHAZADO', 15000.00),

(DATEADD('DAY', -15, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_004@email.com'), 1, 'AUTO_SVC_007 - Psicología seguimiento', 'PSICOLOGIA', 'ACEPTADO', 20000.00),
(DATEADD('DAY', -35, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_004@email.com'), 1, 'AUTO_SVC_008 - Kinesiología seguimiento', 'KINESIOLOGIA', 'PAGO_PENDIENTE', 18000.00),

(DATEADD('DAY', -20, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_005@email.com'), 1, 'AUTO_SVC_009 - Terapia ocupacional inicial', 'TERAPIA_OCUPACIONAL', 'ACEPTADO', 19000.00),
(DATEADD('DAY', -42, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_005@email.com'), 2, 'AUTO_SVC_010 - Fonoaudiología', 'FONOAUDIOLOGIA', 'ACEPTADO', 17000.00),

(DATEADD('DAY', -9,  CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_006@email.com'), 1, 'AUTO_SVC_011 - Kinesiología', 'KINESIOLOGIA', 'ACEPTADO', 18000.00),
(DATEADD('DAY', -55, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_006@email.com'), 2, 'AUTO_SVC_012 - Psicología', 'PSICOLOGIA', 'ACEPTADO', 20000.00),

(DATEADD('DAY', -28, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_007@email.com'), 2, 'AUTO_SVC_013 - Transporte sanitario', 'TRANSPORTE_SANITARIO', 'PAGO_PENDIENTE', 8000.00),
(DATEADD('DAY', -60, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_007@email.com'), 1, 'AUTO_SVC_014 - Asistente terapéutico', 'ASISTENTE_TERAPEUTICO', 'ACEPTADO', 15000.00),

(DATEADD('DAY', -22, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_008@email.com'), 1, 'AUTO_SVC_015 - Fonoaudiología', 'FONOAUDIOLOGIA', 'RECHAZADO', 17000.00),
(DATEADD('DAY', -75, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_008@email.com'), 2, 'AUTO_SVC_016 - Terapia ocupacional', 'TERAPIA_OCUPACIONAL', 'ACEPTADO', 19000.00),

(DATEADD('DAY', -33, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_009@email.com'), 1, 'AUTO_SVC_017 - Psicología', 'PSICOLOGIA', 'ACEPTADO', 20000.00),
(DATEADD('DAY', -85, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_009@email.com'), 1, 'AUTO_SVC_018 - Kinesiología', 'KINESIOLOGIA', 'PAGO_PENDIENTE', 18000.00),

(DATEADD('DAY', -40, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_010@email.com'), 2, 'AUTO_SVC_019 - Terapia ocupacional', 'TERAPIA_OCUPACIONAL', 'ACEPTADO', 19000.00),
(DATEADD('DAY', -95, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_010@email.com'), 1, 'AUTO_SVC_020 - Transporte sanitario', 'TRANSPORTE_SANITARIO', 'ACEPTADO', 8000.00),

-- 21..50 (misma lógica, más antigüedad para cubrir 6 meses)
(DATEADD('DAY', -48, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_011@email.com'), 1, 'AUTO_SVC_021 - Kinesiología', 'KINESIOLOGIA', 'ACEPTADO', 18000.00),
(DATEADD('DAY', -110, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_011@email.com'), 2, 'AUTO_SVC_022 - Psicología', 'PSICOLOGIA', 'ACEPTADO', 20000.00),

(DATEADD('DAY', -52, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_012@email.com'), 1, 'AUTO_SVC_023 - Fonoaudiología', 'FONOAUDIOLOGIA', 'ACEPTADO', 17000.00),
(DATEADD('DAY', -120, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_012@email.com'), 2, 'AUTO_SVC_024 - Terapia ocupacional', 'TERAPIA_OCUPACIONAL', 'PAGO_PENDIENTE', 19000.00),

(DATEADD('DAY', -58, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_013@email.com'), 2, 'AUTO_SVC_025 - Asistente terapéutico', 'ASISTENTE_TERAPEUTICO', 'RECHAZADO', 15000.00),
(DATEADD('DAY', -130, CURRENT_TIMESTAMP), (SELECT id FROM patients WHERE correo_electronico='paciente_013@email.com'), 1, 'AUTO_SVC_026 - Transporte sanitario', 'TRANSPORTE_SANITARIO', 'ACEPTADO', 8000.00);