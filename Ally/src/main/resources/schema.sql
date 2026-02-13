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
-- SERVICIOS (con fechas fijas para evitar problemas)
-- ===========================================
INSERT INTO services (fecha_solicitud, paciente_id, prestador_id, descripcion, especialidad, estado, monto) VALUES
                                                                                                                -- Servicios con MERCADO_PAGO (Pendientes de pago)
                                                                                                                (CURRENT_DATE - 2, 1, 1, 'Consulta inicial de kinesiología', 'KINESIOLOGIA', 'PAGO_PENDIENTE', 18000.00),
                                                                                                                (CURRENT_DATE - 1, 2, 2, 'Terapia psicológica semanal', 'PSICOLOGIA', 'PAGO_PENDIENTE', 20000.00),
                                                                                                                -- Servicios con CONTADO (Ya pagados)
                                                                                                                (CURRENT_DATE - 5, 3, 1, 'Sesión de rehabilitación', 'KINESIOLOGIA', 'ACEPTADO', 18000.00),
                                                                                                                (CURRENT_DATE - 4, 4, 2, 'Consulta psicológica', 'PSICOLOGIA', 'ACEPTADO', 20000.00),
                                                                                                                -- Servicios con TRANSFERENCIA_BANCARIA
                                                                                                                (CURRENT_DATE - 3, 1, 2, 'Terapia grupal', 'PSICOLOGIA', 'ACEPTADO', 20000.00),
                                                                                                                (CURRENT_DATE - 2, 3, 1, 'Seguimiento kinesiológico', 'KINESIOLOGIA', 'ACEPTADO', 18000.00),
                                                                                                                -- Servicios con OBRA_SOCIAL
                                                                                                                (CURRENT_DATE - 6, 2, 1, 'Rehabilitación por obra social', 'KINESIOLOGIA', 'ACEPTADO', 18000.00),
                                                                                                                (CURRENT_DATE - 5, 4, 2, 'Atención psicológica por cobertura', 'PSICOLOGIA', 'ACEPTADO', 20000.00),
                                                                                                                -- Otros servicios
                                                                                                                (CURRENT_DATE - 3, 1, 1, 'Consulta fonoaudiológica', 'FONOAUDIOLOGIA', 'ACEPTADO', 17000.00),
                                                                                                                (CURRENT_DATE - 2, 2, 2, 'Terapia ocupacional', 'TERAPIA_OCUPACIONAL', 'ACEPTADO', 19000.00),
                                                                                                                (CURRENT_DATE - 1, 3, 1, 'Asistencia terapéutica', 'ASISTENTE_TERAPEUTICO', 'ACEPTADO', 15000.00),
                                                                                                                (CURRENT_DATE, 4, 2, 'Transporte sanitario', 'TRANSPORTE_SANITARIO', 'ACEPTADO', 8000.00);

-- ===========================================
-- PAGOS
-- ===========================================
INSERT INTO payments (servicio_id, monto, estado_pago, fecha_creacion, fecha_pago, id_transaccion, metodo_pago_id, mensaje_error) VALUES
                                                                                                                                      (1, 18000.00, 'PENDIENTE', CURRENT_DATE - 2, NULL, 'MP_PREF_001',
                                                                                                                                       (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'MERCADO_PAGO'),
                                                                                                                                       '{"email_pagador": "maria.gomez@email.com", "nombre_pagador": "María Gómez"}'),
                                                                                                                                      (2, 20000.00, 'PENDIENTE', CURRENT_DATE - 1, NULL, 'MP_PREF_002',
                                                                                                                                       (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'MERCADO_PAGO'),
                                                                                                                                       '{"email_pagador": "carlos.lopez@email.com", "nombre_pagador": "Carlos López"}'),
                                                                                                                                      (3, 18000.00, 'COMPLETADO', CURRENT_DATE - 5, CURRENT_DATE - 4, 'CONT-001',
                                                                                                                                       (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'CONTADO'), NULL),
                                                                                                                                      (4, 20000.00, 'COMPLETADO', CURRENT_DATE - 4, CURRENT_DATE - 3, 'CONT-002',
                                                                                                                                       (SELECT metodo_pago_id FROM metodos_pagos WHERE metodo_pago = 'CONTADO'), NULL);