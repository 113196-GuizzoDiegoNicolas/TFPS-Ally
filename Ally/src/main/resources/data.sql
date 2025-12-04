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
-- SERVICIOS DE PRUEBA PARA DIFERENTES MÉTODOS DE PAGO
-- ===========================================
INSERT INTO services (fecha_solicitud, paciente_id, prestador_id, descripcion, especialidad, estado) VALUES
-- Servicios con MERCADO_PAGO (Pendientes de pago)
(CURRENT_TIMESTAMP - INTERVAL '2' DAY, 1, 1, 'Consulta inicial de kinesiología', 'KINESIOLOGIA', 'PAGO_PENDIENTE'),
(CURRENT_TIMESTAMP - INTERVAL '1' DAY, 2, 2, 'Terapia psicológica semanal', 'PSICOLOGIA', 'PAGO_PENDIENTE'),

-- Servicios con CONTADO (Ya pagados)
(CURRENT_TIMESTAMP - INTERVAL '5' DAY, 3, 1, 'Sesión de rehabilitación', 'KINESIOLOGIA', 'ACEPTADO'),
(CURRENT_TIMESTAMP - INTERVAL '4' DAY, 4, 2, 'Consulta psicológica', 'PSICOLOGIA', 'ACEPTADO'),

-- Servicios con TRANSFERENCIA_BANCARIA (En proceso)
(CURRENT_TIMESTAMP - INTERVAL '3' DAY, 1, 2, 'Terapia grupal', 'PSICOLOGIA', 'ACEPTADO'),
(CURRENT_TIMESTAMP - INTERVAL '2' DAY, 3, 1, 'Seguimiento kinesiológico', 'KINESIOLOGIA', 'ACEPTADO'),

-- Servicios con OBRA_SOCIAL (Cobertura total)
(CURRENT_TIMESTAMP - INTERVAL '6' DAY, 2, 1, 'Rehabilitación por obra social', 'KINESIOLOGIA', 'ACEPTADO'),
(CURRENT_TIMESTAMP - INTERVAL '5' DAY, 4, 2, 'Atención psicológica por cobertura', 'PSICOLOGIA', 'ACEPTADO');

-- ===========================================
-- PAGOS DE EJEMPLO PARA CADA MÉTODO
-- ===========================================
INSERT INTO payments (servicio_id, monto, estado_pago, fecha_creacion, fecha_pago, id_transaccion, metodo_pago_id, mensaje_error) VALUES
-- 1. PAGO CON MERCADO_PAGO (Pendiente - para crear preferencia)
(1, 18000.00, 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '2' DAY, NULL, 'MP_PREF_001',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'MERCADO_PAGO'),
 '{"email_pagador": "maria.gomez@email.com", "nombre_pagador": "María Gómez"}'),

-- 2. PAGO CON MERCADO_PAGO (Pendiente - otro servicio)
(2, 20000.00, 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '1' DAY, NULL, 'MP_PREF_002',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'MERCADO_PAGO'),
 '{"email_pagador": "carlos.lopez@email.com", "nombre_pagador": "Carlos López"}'),

-- 3. PAGO CON CONTADO (Completado)
(3, 18000.00, 'COMPLETADO', CURRENT_TIMESTAMP - INTERVAL '5' DAY, CURRENT_TIMESTAMP - INTERVAL '4' DAY, 'CONT-001',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'CONTADO'),
 'Pago en efectivo al momento de la consulta'),

-- 4. PAGO CON CONTADO (Completado)
(4, 20000.00, 'COMPLETADO', CURRENT_TIMESTAMP - INTERVAL '4' DAY, CURRENT_TIMESTAMP - INTERVAL '3' DAY, 'CONT-002',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'CONTADO'),
 'Pago en efectivo'),

-- 5. PAGO CON TRANSFERENCIA_BANCARIA (Completado)
(5, 20000.00, 'COMPLETADO', CURRENT_TIMESTAMP - INTERVAL '3' DAY, CURRENT_TIMESTAMP - INTERVAL '2' DAY, 'TRANS-001',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'TRANSFERENCIA_BANCARIA'),
 'Transferencia bancaria confirmada - CBU: 0720123456789012345678'),

-- 6. PAGO CON TRANSFERENCIA_BANCARIA (Pendiente)
(6, 18000.00, 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '2' DAY, NULL, 'TRANS-002',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'TRANSFERENCIA_BANCARIA'),
 'Esperando confirmación de transferencia'),

-- 7. PAGO CON OBRA_SOCIAL (Completado - cobertura total)
(7, 0.00, 'COMPLETADO', CURRENT_TIMESTAMP - INTERVAL '6' DAY, CURRENT_TIMESTAMP - INTERVAL '5' DAY, 'OS-001',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'OBRA_SOCIAL'),
 'Cobertura 100% por obra social - N° autorización: OSDE-20241204-001'),

-- 8. PAGO CON OBRA_SOCIAL (Completado - con copago)
(8, 5000.00, 'COMPLETADO', CURRENT_TIMESTAMP - INTERVAL '5' DAY, CURRENT_TIMESTAMP - INTERVAL '4' DAY, 'OS-002',
 (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'OBRA_SOCIAL'),
 'Cobertura parcial - Copago paciente: $5000 - Autorización: OMINT-20241204-002');

-- ===========================================
-- USUARIO ADMIN
-- ===========================================
INSERT INTO usuarios (
    usuario, password, email, activo, bloqueado, intentos_fallidos, fecha_creacion, rol
) VALUES (
             'admin_ally',
             'password123',              -- misma clave que el resto
             'admin@ally.com',
             true,
             false,
             0,
             CURRENT_TIMESTAMP,
             'ADMIN'
         );