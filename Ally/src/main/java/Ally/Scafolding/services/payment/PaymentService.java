package Ally.Scafolding.services.payment;

import Ally.Scafolding.entities.*;
import Ally.Scafolding.models.PagoEstado;
import Ally.Scafolding.models.MetodoPago;
import Ally.Scafolding.dtos.common.payment.PaymentRequestDTO;
import Ally.Scafolding.dtos.common.payment.PaymentResponseDTO;
import Ally.Scafolding.repositories.*;
import Ally.Scafolding.services.payment.strategy.PaymentResult;
import Ally.Scafolding.services.payment.strategy.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final List<PaymentStrategy> paymentStrategies;
    private final PaymentsRepository paymentsRepository;
    private final ServiceRepository serviceRepository;
    private final PatientsRepository patientsRepository;
    private final ProvidersRepository providersRepository;
    private final MetodosPagosRepository metodosPagosRepository;

    /**
     * Procesa un nuevo pago
     */
    public PaymentResponseDTO processPayment(PaymentRequestDTO request) {
        log.info("💰 Iniciando procesamiento de pago para servicio: {}", request.getServicioId());

        try {
            // 1. Validaciones básicas
            validatePaymentRequest(request);

            // 2. Obtener el servicio
            ServiceEntity servicio = serviceRepository.findById(request.getServicioId())
                    .orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado: " + request.getServicioId()));

            // 3. Validar estado del servicio
            validateServiceForPayment(servicio);

            // 4. Obtener paciente y prestador por ID (CORREGIDO)
            PatientsEntity paciente = obtenerPaciente(servicio.getPacienteId());
            ProvidersEntity prestador = obtenerPrestador(servicio.getPrestadorId());

            if (prestador == null) {
                throw new IllegalStateException("El servicio no tiene un prestador asignado");
            }

            // 5. Calcular monto
            BigDecimal montoFinal = calculateAmount(request, servicio, prestador);

            // 6. Crear entidad de pago
            PaymentsEntity pago = createPaymentEntity(request, servicio, prestador, montoFinal);
            pago = paymentsRepository.save(pago);

            // 7. Buscar estrategia adecuada
            PaymentStrategy strategy = findStrategy(request.getMetodoPago());

            // 8. Validar con la estrategia
            strategy.validate(request);

            // 9. Procesar pago con la estrategia
            PaymentResult result = strategy.processPayment(request, pago);

            // 10. Actualizar estado del pago
            updatePaymentStatus(pago, result);

            // 11. Actualizar estado del servicio si es necesario
            updateServiceStatus(servicio, pago);

            log.info("✅ Pago {} procesado exitosamente", pago.getId());

            // 12. Construir y retornar respuesta
            return buildResponseDTO(pago, result, servicio);

        } catch (IllegalArgumentException | IllegalStateException e) {
            log.warn("⚠️ Error de validación en pago: {}", e.getMessage());
            return buildErrorResponse("VALIDACION_ERROR", e.getMessage());

        } catch (Exception e) {
            log.error("❌ Error procesando pago: {}", e.getMessage(), e);
            return buildErrorResponse("INTERNAL_ERROR", "Error interno: " + e.getMessage());
        }
    }

    /**
     * Crea una preferencia de pago en MercadoPago
     */
    public PaymentResponseDTO createMercadoPagoPreference(PaymentRequestDTO paymentRequestDTO) {
        log.info("🛒 Creando preferencia de MercadoPago para servicio: {}",
                paymentRequestDTO.getServicioId());

        try {
            // 1. Validar la solicitud
            validatePaymentRequest(paymentRequestDTO);

            // 2. Obtener el servicio
            ServiceEntity servicio = serviceRepository.findById(paymentRequestDTO.getServicioId())
                    .orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado"));

            // 3. Obtener prestador para información adicional
            ProvidersEntity prestador = obtenerPrestador(servicio.getPrestadorId());

            // 4. Calcular monto (usar el mismo método que processPayment)
            BigDecimal montoFinal = calculateAmount(paymentRequestDTO, servicio, prestador);

            // 5. Simulación de creación de preferencia en MercadoPago
            // EN PRODUCCIÓN: Aquí integrarías con el SDK real de MercadoPago

            String preferenceId = "mp_pref_" + System.currentTimeMillis();
            String initPoint = "https://www.mercadopago.com.ar/checkout/v1/redirect?pref_id=" + preferenceId;
            String sandboxInitPoint = "https://sandbox.mercadopago.com.ar/checkout/v1/redirect?pref_id=" + preferenceId;

            // 6. Crear y guardar el pago como PENDIENTE
            PaymentsEntity pago = new PaymentsEntity();
            pago.setServicio(servicio);
            pago.setMonto(montoFinal.doubleValue());
            pago.setFechaCreacion(LocalDateTime.now());
            pago.setEstadoPago(PagoEstado.PENDIENTE);
            pago.setIdTransaccion(preferenceId);

            // Configurar método de pago como MERCADO_PAGO
            MetodoPago metodoPagoEnum = MetodoPago.MERCADO_PAGO;
            MetodosPagosEntity metodoPagoEntity = metodosPagosRepository.findByMetodoPago(metodoPagoEnum)
                    .orElseGet(() -> {
                        MetodosPagosEntity newMethod = new MetodosPagosEntity();
                        newMethod.setMetodoPago(metodoPagoEnum);
                        // NO tiene campo descripcion, solo id y metodoPago
                        return metodosPagosRepository.save(newMethod);
                    });

            pago.setMetodoPago(metodoPagoEntity);

            // Guardar datos adicionales para MercadoPago
            Map<String, String> datosMercadoPago = new HashMap<>();
            datosMercadoPago.put("preference_id", preferenceId);
            datosMercadoPago.put("init_point", initPoint);
            datosMercadoPago.put("sandbox_init_point", sandboxInitPoint);

            if (paymentRequestDTO.getEmailPagador() != null) {
                datosMercadoPago.put("payer_email", paymentRequestDTO.getEmailPagador());
            }
            if (paymentRequestDTO.getNombrePagador() != null) {
                datosMercadoPago.put("payer_name", paymentRequestDTO.getNombrePagador());
            }

// Convertir a Map<String, Object> si es necesario
            Map<String, Object> datosAdicionales = new HashMap<>(datosMercadoPago);

// 7. Construir y retornar respuesta
            return PaymentResponseDTO.builder()
                    .pagoId(pago.getId())
                    .estado("PREFERENCE_CREATED")
                    .mensaje("Preferencia de MercadoPago creada exitosamente")
                    .transaccionId(preferenceId)
                    .fechaPago(null) // Aún no hay pago
                    .monto(montoFinal)
                    .metodoPago("MERCADO_PAGO")
                    .servicioDetalle("Servicio #" + servicio.getId())
                    .success(true)
                    .datosAdicionales(datosAdicionales) // Pasar el Map<String, Object>
                    .build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.warn("⚠️ Error de validación en preferencia MercadoPago: {}", e.getMessage());
            return buildErrorResponse("VALIDACION_ERROR", e.getMessage());

        } catch (Exception e) {
            log.error("❌ Error creando preferencia MercadoPago: {}", e.getMessage(), e);
            return buildErrorResponse("MERCADOPAGO_ERROR", "Error creando preferencia: " + e.getMessage());
        }
    }

    // ============ MÉTODOS PRIVADOS DE APOYO ============

    private void validatePaymentRequest(PaymentRequestDTO request) {
        if (request.getServicioId() == null) {
            throw new IllegalArgumentException("ID de servicio es requerido");
        }
        if (request.getMetodoPago() == null || request.getMetodoPago().trim().isEmpty()) {
            throw new IllegalArgumentException("Método de pago es requerido");
        }
        if (request.getMonto() == null || request.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monto inválido. Debe ser mayor a 0");
        }
    }

    private void validateServiceForPayment(ServiceEntity servicio) {
        String estado = servicio.getEstado();
        if (!"ACEPTADO".equals(estado) && !"PAGO_PENDIENTE".equals(estado)) {
            throw new IllegalStateException("El servicio debe estar ACEPTADO o PAGO_PENDIENTE para poder pagarse");
        }

        // Verificar si ya tiene un pago exitoso
        List<PaymentsEntity> pagosServicio = paymentsRepository.findByServicioId(servicio.getId());
        boolean tienePagoExitoso = pagosServicio.stream()
                .anyMatch(p -> PagoEstado.COMPLETADO.equals(p.getEstadoPago()));

        if (tienePagoExitoso) {
            throw new IllegalStateException("El servicio ya tiene un pago completado");
        }
    }

    private PatientsEntity obtenerPaciente(Long pacienteId) {
        if (pacienteId == null) {
            return null;
        }
        return patientsRepository.findById(pacienteId).orElse(null);
    }

    private ProvidersEntity obtenerPrestador(Long prestadorId) {
        if (prestadorId == null) {
            return null;
        }
        return providersRepository.findById(prestadorId).orElse(null);
    }

    private BigDecimal calculateAmount(PaymentRequestDTO request, ServiceEntity servicio, ProvidersEntity prestador) {
        // Prioridad: monto del request
        if (request.getMonto() != null && request.getMonto().compareTo(BigDecimal.ZERO) > 0) {
            return request.getMonto();
        }

        // Si no hay monto en request, intentar obtener de la especialidad del prestador
        if (prestador != null && prestador.getEspecialidad() != null) {
            try {
                // Verificar si SpecialtyEntity tiene importeConsulta
                Object especialidad = prestador.getEspecialidad();
                java.lang.reflect.Method getImporteMethod = especialidad.getClass().getMethod("getImporteConsulta");
                BigDecimal importe = (BigDecimal) getImporteMethod.invoke(especialidad);
                if (importe != null && importe.compareTo(BigDecimal.ZERO) > 0) {
                    return importe;
                }
            } catch (Exception e) {
                log.debug("No se pudo obtener importe de la especialidad: {}", e.getMessage());
            }
        }

        throw new IllegalArgumentException("No se pudo determinar el monto del servicio");
    }

    private PaymentsEntity createPaymentEntity(PaymentRequestDTO request, ServiceEntity servicio,
                                               ProvidersEntity prestador, BigDecimal monto) {
        PaymentsEntity pago = new PaymentsEntity();
        pago.setServicio(servicio);
        pago.setMonto(monto.doubleValue());
        pago.setFechaCreacion(LocalDateTime.now());
        pago.setEstadoPago(PagoEstado.PENDIENTE);

        // Obtener método de pago desde enum
        MetodoPago metodoPagoEnum = MetodoPago.valueOf(request.getMetodoPago().toUpperCase());

        // Buscar o crear método de pago en la base de datos
        MetodosPagosEntity metodoPagoEntity = metodosPagosRepository.findByMetodoPago(metodoPagoEnum)
                .orElseGet(() -> {
                    MetodosPagosEntity newMethod = new MetodosPagosEntity();
                    newMethod.setMetodoPago(metodoPagoEnum);
                    // NO tiene campo descripcion, solo tiene id y metodoPago
                    return metodosPagosRepository.save(newMethod);
                });

        pago.setMetodoPago(metodoPagoEntity);

        // Guardar datos adicionales si es necesario
        if ("MERCADO_PAGO".equalsIgnoreCase(request.getMetodoPago()) && request.getEmailPagador() != null) {
            String datosAdicionales = "Email pagador: " + request.getEmailPagador();
            if (request.getNombrePagador() != null) {
                datosAdicionales += ", Nombre: " + request.getNombrePagador();
            }
            pago.setMensajeError(datosAdicionales);
        }

        return pago;
    }

    private PaymentStrategy findStrategy(String metodoPago) {
        return paymentStrategies.stream()
                .filter(strategy -> strategy.supports(metodoPago))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Método de pago no soportado: " + metodoPago));
    }

    private void updatePaymentStatus(PaymentsEntity pago, PaymentResult result) {
        if (result.isSuccess()) {
            pago.setEstadoPago(PagoEstado.COMPLETADO);
            pago.setFechaPago(LocalDateTime.now());
            pago.setFechaProcesamiento(LocalDateTime.now());
            pago.setIdTransaccion(result.getTransactionId());

            // Intentar setear código de autorización si existe
            if (result.getAuthorizationCode() != null) {
                try {
                    pago.getClass().getMethod("setCodigoAutorizacion", String.class);
                    pago.setCodigoAutorizacion(result.getAuthorizationCode());
                } catch (NoSuchMethodException e) {
                    // Ignorar si no existe el campo
                }
            }
        } else {
            pago.setEstadoPago(PagoEstado.FALLIDO);
            pago.setMensajeError(result.getErrorMessage());
        }
        paymentsRepository.save(pago);
    }

    private void updateServiceStatus(ServiceEntity servicio, PaymentsEntity pago) {
        if (PagoEstado.COMPLETADO.equals(pago.getEstadoPago())) {
            servicio.setEstado("PAGADO");
            serviceRepository.save(servicio);
        }
    }

    private PaymentResponseDTO buildResponseDTO(PaymentsEntity pago, PaymentResult result, ServiceEntity servicio) {
        PaymentResponseDTO.PaymentResponseDTOBuilder builder = PaymentResponseDTO.builder()
                .pagoId(pago.getId())
                .estado(pago.getEstadoPago().name())
                .mensaje(result.getMessage())
                .transaccionId(pago.getIdTransaccion())
                .fechaPago(pago.getFechaPago())
                .monto(BigDecimal.valueOf(pago.getMonto()))
                .metodoPago(pago.getMetodoPago().getMetodoPago().name())
                .servicioDetalle("Servicio #" + servicio.getId())
                .success(result.isSuccess());

        // Agregar datos adicionales del resultado - CORREGIDO
        if (result.getAdditionalData() != null) {
            builder.datosAdicionales(result.getAdditionalData()); // Cambiado de additionalData() a datosAdicionales()
        }

        return builder.build();
    }

    private PaymentResponseDTO buildErrorResponse(String errorCode, String message) {
        return PaymentResponseDTO.builder()
                .success(false)
                .estado("ERROR")
                .mensaje(message)
                .errorCode(errorCode)
                .build();
    }

    private String convertMapToJsonString(Map<String, String> map) {
        // Conversión simple de mapa a string JSON
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!first) {
                json.append(",");
            }
            json.append("\"").append(entry.getKey()).append("\":\"")
                    .append(entry.getValue()).append("\"");
            first = false;
        }
        json.append("}");
        return json.toString();
    }

    /**
     * Obtiene el historial de pagos de un servicio
     */
    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getPaymentHistory(Long servicioId) {
        List<PaymentsEntity> pagos = paymentsRepository.findByServicioId(servicioId);

        return pagos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un pago por ID
     */
    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentById(Long pagoId) {
        PaymentsEntity pago = paymentsRepository.findById(pagoId)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado: " + pagoId));

        return convertToDTO(pago);
    }

    /**
     * Reembolsa un pago
     */
    @Transactional
    public PaymentResponseDTO refundPayment(Long pagoId, String motivo) {
        try {
            PaymentsEntity pago = paymentsRepository.findById(pagoId)
                    .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado"));

            // Validar que el pago esté completado
            if (pago.getEstadoPago() != PagoEstado.COMPLETADO) {
                throw new IllegalStateException("Solo se pueden reembolsar pagos COMPLETADOS");
            }

            // Actualizar estado local
            pago.setEstadoPago(PagoEstado.REEMBOLSADO);
            pago.setMensajeError("Reembolsado - Motivo: " + motivo);

            // Si existe fecha_actualizacion, actualizarla
            try {
                pago.getClass().getMethod("setFechaActualizacion", LocalDateTime.class);
                pago.setFechaActualizacion(LocalDateTime.now());
            } catch (NoSuchMethodException e) {
                // Ignorar si no existe
            }

            paymentsRepository.save(pago);

            log.info("✅ Pago {} reembolsado exitosamente", pagoId);

            return PaymentResponseDTO.builder()
                    .success(true)
                    .pagoId(pagoId)
                    .estado("REEMBOLSADO")
                    .mensaje("Pago reembolsado exitosamente")
                    .build();

        } catch (Exception e) {
            log.error("❌ Error reembolsando pago {}: {}", pagoId, e.getMessage(), e);
            return buildErrorResponse("REFUND_ERROR", "Error reembolsando pago: " + e.getMessage());
        }
    }

    /**
     * Convierte entidad a DTO
     */
    private PaymentResponseDTO convertToDTO(PaymentsEntity pago) {
        return PaymentResponseDTO.builder()
                .pagoId(pago.getId())
                .estado(pago.getEstadoPago().name())
                .transaccionId(pago.getIdTransaccion())
                .fechaPago(pago.getFechaPago())
                .monto(BigDecimal.valueOf(pago.getMonto()))
                .metodoPago(pago.getMetodoPago().getMetodoPago().name())
                .servicioDetalle(pago.getServicio() != null ?
                        "Servicio #" + pago.getServicio().getId() : "")
                .success(PagoEstado.COMPLETADO.equals(pago.getEstadoPago()))
                .build();
    }
}