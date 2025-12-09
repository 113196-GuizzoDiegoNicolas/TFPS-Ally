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