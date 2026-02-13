-- ===========================================
-- RESET DE SECUENCIAS (EJECUTAR DESPUÉS)
-- ===========================================
SELECT setval('usuarios_id_seq', (SELECT COALESCE(MAX(id), 1) FROM usuarios));
SELECT setval('patients_id_seq', (SELECT COALESCE(MAX(id), 1) FROM patients));
SELECT setval('providers_id_seq', (SELECT COALESCE(MAX(id), 1) FROM providers));
SELECT setval('services_id_seq', (SELECT COALESCE(MAX(id), 1) FROM services));
SELECT setval('payments_id_seq', (SELECT COALESCE(MAX(id), 1) FROM payments));
SELECT setval('specialties_id_seq', (SELECT COALESCE(MAX(id), 1) FROM specialties));
SELECT setval('metodos_pagos_id_seq', (SELECT COALESCE(MAX(metodo_pago_id), 1) FROM metodos_pagos));