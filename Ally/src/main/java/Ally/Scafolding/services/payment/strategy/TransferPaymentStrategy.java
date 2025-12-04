package Ally.Scafolding.services.payment.strategy;

import Ally.Scafolding.entities.*;
import Ally.Scafolding.models.PagoEstado;
import Ally.Scafolding.dtos.common.payment.PaymentRequestDTO;
import Ally.Scafolding.repositories.ProvidersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferPaymentStrategy implements PaymentStrategy {

    private final ProvidersRepository providersRepository;

    @Value("${payment.transfer.commission:1.5}")
    private double commissionPercentage;

    @Override
    public PaymentResult processPayment(PaymentRequestDTO request, PaymentsEntity pago) {
        log.info("🏦 Procesando transferencia bancaria para servicio: {}", request.getServicioId());

        try {
            // 1. Obtener servicio
            ServiceEntity servicio = pago.getServicio();
            if (servicio == null) {
                throw new IllegalStateException("El pago no tiene un servicio asociado");
            }

            // 2. Obtener prestador por ID
            ProvidersEntity prestador = obtenerPrestador(servicio.getPrestadorId());

            if (prestador == null) {
                throw new IllegalStateException("No se encontró prestador para el servicio");
            }

            // 3. Obtener CBU destino
            String cbuDestino = obtenerCbuDestino(request, prestador);

            // 4. Validar CBU
            validarCbu(cbuDestino);

            // 5. Calcular comisión
            BigDecimal comision = calcularComision(pago, request.getMonto());

            // 6. Procesar transferencia
            return procesarTransferencia(request, pago, prestador, cbuDestino, comision);

        } catch (IllegalArgumentException | IllegalStateException e) {
            log.warn("⚠️ Validación fallida para transferencia: {}", e.getMessage());
            return handleValidationError(pago, e.getMessage());

        } catch (Exception e) {
            log.error("❌ Error en transferencia bancaria: {}", e.getMessage(), e);
            return handleGeneralError(pago, e.getMessage());
        }
    }

    // ============ MÉTODOS PRIVADOS ============

    private ProvidersEntity obtenerPrestador(Long prestadorId) {
        if (prestadorId == null) {
            throw new IllegalArgumentException("ID de prestador no proporcionado");
        }

        try {
            Optional<ProvidersEntity> prestadorOpt = providersRepository.findById(prestadorId);
            return prestadorOpt.orElseThrow(() ->
                    new IllegalStateException("Prestador no encontrado con ID: " + prestadorId)
            );
        } catch (Exception e) {
            log.error("Error buscando prestador {}: {}", prestadorId, e.getMessage());
            throw new IllegalStateException("Error al buscar prestador: " + e.getMessage());
        }
    }

    private String obtenerCbuDestino(PaymentRequestDTO request, ProvidersEntity prestador) {
        // Prioridad: CBU del request, luego CBU del prestador
        if (request.getCbuDestino() != null && !request.getCbuDestino().trim().isEmpty()) {
            return request.getCbuDestino();
        }

        String cbuPrestador = prestador.getCBUBancaria();
        if (cbuPrestador == null || cbuPrestador.trim().isEmpty()) {
            throw new IllegalStateException("El prestador no tiene CBU configurado");
        }

        return cbuPrestador;
    }

    private void validarCbu(String cbu) {
        if (cbu == null || cbu.trim().isEmpty()) {
            throw new IllegalArgumentException("CBU no proporcionado");
        }

        // Validar formato básico de CBU (22 dígitos)
        String cbuLimpio = cbu.replaceAll("[^0-9]", "");

        if (cbuLimpio.length() != 22) {
            throw new IllegalArgumentException("CBU inválido. Debe tener 22 dígitos. Recibido: " + cbuLimpio.length());
        }

        // Validar dígito verificador (simplificado)
        if (!cbuLimpio.matches("\\d{22}")) {
            throw new IllegalArgumentException("CBU inválido. Solo debe contener números");
        }
    }

    private BigDecimal calcularComision(PaymentsEntity pago, BigDecimal monto) {
        BigDecimal porcentajeComision = BigDecimal.valueOf(commissionPercentage)
                .divide(BigDecimal.valueOf(100), 4, java.math.RoundingMode.HALF_UP);

        BigDecimal comision = monto.multiply(porcentajeComision);

        // Guardar comisión en el pago si el campo existe
        try {
            pago.getClass().getMethod("setComision", BigDecimal.class);
            pago.setComision(comision);
        } catch (NoSuchMethodException e) {
            log.debug("PaymentsEntity no tiene campo comision, omitiendo");
        }

        return comision;
    }

    private PaymentResult procesarTransferencia(PaymentRequestDTO request, PaymentsEntity pago,
                                                ProvidersEntity prestador, String cbuDestino,
                                                BigDecimal comision) {

        // Simular procesamiento de transferencia
        String transactionId = "TRF-" + System.currentTimeMillis();
        String authorizationCode = "AUTH-TRF-" + System.currentTimeMillis();

        // Calcular monto neto (monto - comisión)
        BigDecimal montoNeto = request.getMonto().subtract(comision);

        // Marcar como completado
        pago.setEstadoPago(PagoEstado.COMPLETADO);
        pago.setFechaPago(LocalDateTime.now());
        pago.setFechaProcesamiento(LocalDateTime.now());
        pago.setIdTransaccion(transactionId);

        // Intentar setear código de autorización si el campo existe
        try {
            pago.getClass().getMethod("setCodigoAutorizacion", String.class);
            pago.setCodigoAutorizacion(authorizationCode);
        } catch (NoSuchMethodException e) {
            log.debug("PaymentsEntity no tiene campo codigoAutorizacion, omitiendo");
        }

        pago.setMensajeError(null);

        // Enmascarar CBU para logs
        String cbuMasked = cbuDestino.substring(0, 4) + "..." + cbuDestino.substring(cbuDestino.length() - 4);
        log.info("✅ Transferencia procesada al CBU: {}", cbuMasked);

        // Construir datos adicionales
        Map<String, Object> additionalData = buildAdditionalData(prestador, cbuDestino, comision, montoNeto);

        return PaymentResult.success(
                String.format("Transferencia bancaria procesada exitosamente al CBU: %s. Comisión: %.2f%%, Monto neto: $%.2f",
                        cbuMasked, commissionPercentage, montoNeto),
                transactionId,
                additionalData
        );
    }

    private Map<String, Object> buildAdditionalData(ProvidersEntity prestador, String cbuDestino,
                                                    BigDecimal comision, BigDecimal montoNeto) {
        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("cbu_destino", cbuDestino);
        additionalData.put("comision_porcentaje", commissionPercentage);
        additionalData.put("comision_monto", comision);
        additionalData.put("monto_neto", montoNeto);
        additionalData.put("fecha_estimada_acreditacion",
                LocalDateTime.now().plusDays(1).toString()); // 24-48hs hábiles

        // ⚠️ CORRECCIÓN: Obtener datos del prestador de forma segura
        try {
            // Usar reflection para acceder a métodos heredados
            String nombreCompleto = obtenerNombreCompletoPrestador(prestador);
            additionalData.put("prestador_nombre", nombreCompleto);

            // Intentar obtener DNI si el método existe
            try {
                java.lang.reflect.Method getDniMethod = prestador.getClass().getMethod("getDni");
                String dni = (String) getDniMethod.invoke(prestador);
                if (dni != null) {
                    additionalData.put("prestador_dni", dni);
                }
            } catch (Exception e) {
                log.debug("No se pudo obtener DNI del prestador: {}", e.getMessage());
            }

        } catch (Exception e) {
            log.warn("Error obteniendo datos del prestador: {}", e.getMessage());
        }

        // Información bancaria si está disponible
        if (prestador.getCBUBancaria() != null) {
            additionalData.put("cbu_prestador", prestador.getCBUBancaria());
        }

        // Otros datos del prestador
        if (prestador.getMatricula() != null) {
            additionalData.put("prestador_matricula", prestador.getMatricula());
        }

        return additionalData;
    }

    /**
     * Obtiene nombre completo del prestador de forma segura
     */
    private String obtenerNombreCompletoPrestador(ProvidersEntity prestador) {
        try {
            // Intentar obtener nombre y apellido por separado
            StringBuilder nombreCompleto = new StringBuilder();

            // Nombre
            try {
                java.lang.reflect.Method getNombreMethod = prestador.getClass().getMethod("getNombre");
                String nombre = (String) getNombreMethod.invoke(prestador);
                if (nombre != null) {
                    nombreCompleto.append(nombre);
                }
            } catch (Exception e) {
                // Ignorar si no existe
            }

            // Apellido
            try {
                java.lang.reflect.Method getApellidoMethod = prestador.getClass().getMethod("getApellido");
                String apellido = (String) getApellidoMethod.invoke(prestador);
                if (apellido != null) {
                    if (nombreCompleto.length() > 0) {
                        nombreCompleto.append(" ");
                    }
                    nombreCompleto.append(apellido);
                }
            } catch (Exception e) {
                // Ignorar si no existe
            }

            // Si no se pudo obtener, usar un valor por defecto
            if (nombreCompleto.length() == 0) {
                nombreCompleto.append("Prestador ID: ").append(prestador.getId());
            }

            return nombreCompleto.toString();

        } catch (Exception e) {
            log.warn("Error obteniendo nombre completo: {}", e.getMessage());
            return "Prestador";
        }
    }

    private PaymentResult handleValidationError(PaymentsEntity pago, String mensaje) {
        pago.setEstadoPago(PagoEstado.FALLIDO);
        pago.setMensajeError("Validación fallida: " + mensaje);
        return PaymentResult.failure("Validación fallida: " + mensaje);
    }

    private PaymentResult handleGeneralError(PaymentsEntity pago, String mensaje) {
        pago.setEstadoPago(PagoEstado.FALLIDO);
        pago.setMensajeError("Error en transferencia: " + mensaje);
        return PaymentResult.failure("Error al procesar transferencia: " + mensaje);
    }

    @Override
    public boolean supports(String metodoPago) {
        return "TRANSFERENCIA_BANCARIA".equalsIgnoreCase(metodoPago);
    }

    @Override
    public void validate(PaymentRequestDTO request) {
        // Validar montos mínimos/máximos
        double montoMinimoTransferencia = 1.00;
        if (request.getMonto().doubleValue() < montoMinimoTransferencia) {
            throw new IllegalArgumentException(
                    String.format("Monto mínimo para transferencia: $%.2f", montoMinimoTransferencia));
        }

        double montoMaximoTransferencia = 1000000.00;
        if (request.getMonto().doubleValue() > montoMaximoTransferencia) {
            throw new IllegalArgumentException(
                    String.format("Monto máximo para transferencia: $%.2f", montoMaximoTransferencia));
        }

        // Si se proporciona CBU, validar formato
        if (request.getCbuDestino() != null && !request.getCbuDestino().trim().isEmpty()) {
            String cbuLimpio = request.getCbuDestino().replaceAll("[^0-9]", "");
            if (cbuLimpio.length() != 22) {
                throw new IllegalArgumentException("CBU inválido. Debe tener 22 dígitos");
            }
        }
    }
}