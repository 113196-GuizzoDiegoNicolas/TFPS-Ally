-- ===========================================
-- CONSULTAS PARA GRÁFICOS ADMIN (POSTGRESQL)
-- ===========================================

-- 1) Conteo de servicios por estado
SELECT s.estado, COUNT(*) AS cantidad
FROM services s
GROUP BY s.estado
ORDER BY cantidad DESC;

-- 2) Totales y cantidad de pagos por método de pago
SELECT mp.metodo_pago, COALESCE(SUM(p.monto), 0) AS total_monto, COUNT(p.id) AS cantidad_pagos
FROM metodos_pagos mp
         LEFT JOIN payments p ON p.metodo_pago_id = mp.metodo_pago_id
GROUP BY mp.metodo_pago
ORDER BY total_monto DESC;

-- 3) Tendencia mensual de pagos (últimos 6 meses) - VERSIÓN POSTGRESQL
SELECT TO_CHAR(COALESCE(p.fecha_pago, p.fecha_creacion), 'YYYY-MM') AS mes,
       COALESCE(SUM(p.monto), 0) AS total_mes,
       COUNT(p.id) AS cantidad_pagos
FROM payments p
WHERE COALESCE(p.fecha_pago, p.fecha_creacion) >= CURRENT_DATE - INTERVAL '6 months'
GROUP BY TO_CHAR(COALESCE(p.fecha_pago, p.fecha_creacion), 'YYYY-MM')
ORDER BY mes;

-- 4) Servicios por especialidad
SELECT s.especialidad, COUNT(*) AS cantidad_servicios
FROM services s
GROUP BY s.especialidad
ORDER BY cantidad_servicios DESC;

-- 5) Proveedores activos por especialidad
SELECT sp.nombre AS especialidad, COUNT(pv.id) AS cantidad_prestadores
FROM providers pv
         JOIN specialties sp ON pv.codigo_especialidad = sp.id
WHERE pv.activo = true
GROUP BY sp.nombre
ORDER BY cantidad_prestadores DESC;

-- 6) Usuarios nuevos por rol en los últimos 6 meses - VERSIÓN POSTGRESQL
SELECT u.rol, TO_CHAR(u.fecha_creacion, 'YYYY-MM') AS mes, COUNT(*) AS cantidad
FROM usuarios u
WHERE u.fecha_creacion >= CURRENT_DATE - INTERVAL '6 months'
GROUP BY u.rol, TO_CHAR(u.fecha_creacion, 'YYYY-MM')
ORDER BY mes, u.rol;

-- 7) Tasa de conversión
SELECT
    SUM(CASE WHEN p.estado_pago = 'COMPLETADO' THEN 1 ELSE 0 END)::float /
    NULLIF(COUNT(DISTINCT s.id), 0) AS tasa_conversion
FROM services s
         LEFT JOIN payments p ON p.servicio_id = s.id
WHERE s.fecha_solicitud >= CURRENT_DATE - INTERVAL '6 months';