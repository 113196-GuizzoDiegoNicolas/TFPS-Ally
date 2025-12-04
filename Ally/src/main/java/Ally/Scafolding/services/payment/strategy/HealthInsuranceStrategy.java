package Ally.Scafolding.services.payment.strategy;

import Ally.Scafolding.entities.*;
import Ally.Scafolding.models.PagoEstado;
import Ally.Scafolding.dtos.common.payment.PaymentRequestDTO;
import Ally.Scafolding.repositories.PatientsRepository;
import Ally.Scafolding.repositories.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HealthInsuranceStrategy implements PaymentStrategy {

    private final PatientsRepository patientsRepository;
    private final SpecialtyRepository specialtyRepository;

    @Override
    public PaymentResult processPayment(PaymentRequestDTO request, PaymentsEntity pago) {
        log.info("⚕️ Procesando pago con obra social para servicio: {}", request.getServicioId());

        try {
            // 1. Validar datos básicos
            validateBasicData(request, pago);

            // 2. Obtener servicio del pago
            ServiceEntity servicio = pago.getServicio();
            if (servicio == null) {
                throw new IllegalStateException("El pago no tiene un servicio asociado");
            }

            // 3. Buscar paciente por ID
            PatientsEntity paciente = findPacienteById(servicio.getPacienteId());
            if (paciente == null) {
                throw new IllegalStateException("No se encontró paciente con ID: " + servicio.getPacienteId());
            }

            // 4. Validar datos de obra social del paciente
            validateObraSocialData(paciente, request);

            // 5. Obtener código de obra social y número de afiliado
            String codigoObraSocial = getCodigoObraSocial(paciente, request);
            String numeroAfiliado = getNumeroAfiliado(paciente, request);

            // 6. Simular autorización
            Map<String, Object> autorizacion = simularAutorizacionObraSocial(
                    codigoObraSocial,
                    numeroAfiliado,
                    request.getMonto(),
                    getEspecialidadNombre(servicio)
            );

            boolean autorizado = (boolean) autorizacion.get("autorizado");

            if (autorizado) {
                // 7. Procesar pago exitoso
                return processSuccessfulPayment(
                        request,
                        pago,
                        servicio,
                        codigoObraSocial,
                        numeroAfiliado,
                        autorizacion
                );
            } else {
                // 8. Manejar rechazo
                String motivoRechazo = (String) autorizacion.get("motivoRechazo");
                throw new RuntimeException("Obra social no autorizó el pago: " + motivoRechazo);
            }

        } catch (IllegalArgumentException | IllegalStateException e) {
            log.warn("⚠️ Validación fallida para obra social: {}", e.getMessage());
            return handleValidationError(pago, e.getMessage());

        } catch (Exception e) {
            log.error("❌ Error en pago con obra social: {}", e.getMessage(), e);
            return handleGeneralError(pago, e.getMessage());
        }
    }

    // ============ MÉTODOS PRIVADOS DE APOYO ============

    private void validateBasicData(PaymentRequestDTO request, PaymentsEntity pago) {
        if (request.getMonto() == null || request.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monto inválido para obra social");
        }
    }

    private PatientsEntity findPacienteById(Long pacienteId) {
        if (pacienteId == null) {
            throw new IllegalArgumentException("ID de paciente no proporcionado");
        }

        try {
            return patientsRepository.findById(pacienteId)
                    .orElseThrow(() -> new IllegalStateException("Paciente no encontrado con ID: " + pacienteId));
        } catch (Exception e) {
            log.error("Error buscando paciente {}: {}", pacienteId, e.getMessage());
            throw new IllegalStateException("Error al buscar paciente: " + e.getMessage());
        }
    }

    private void validateObraSocialData(PatientsEntity paciente, PaymentRequestDTO request) {
        // Verificar si el paciente tiene obra social configurada
        boolean tieneObraSocial = paciente.getCodigoObraSocial() != null &&
                !paciente.getCodigoObraSocial().trim().isEmpty();

        boolean tieneAfiliado = paciente.getNroAfiliadoObraSocial() != null &&
                !paciente.getNroAfiliadoObraSocial().trim().isEmpty();

        // Si no tiene en el paciente, verificar si viene en el request
        boolean requestTieneDatos = request.getCodigoObraSocial() != null ||
                request.getNumeroAfiliado() != null;

        if (!tieneObraSocial && !requestTieneDatos) {
            throw new IllegalStateException("El paciente no tiene obra social configurada y no se proporcionaron datos en la solicitud");
        }
    }

    private String getCodigoObraSocial(PatientsEntity paciente, PaymentRequestDTO request) {
        // Prioridad: request, luego paciente
        if (request.getCodigoObraSocial() != null && !request.getCodigoObraSocial().trim().isEmpty()) {
            return request.getCodigoObraSocial();
        }

        String codigo = paciente.getCodigoObraSocial();
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalStateException("Código de obra social no disponible");
        }

        return codigo;
    }

    private String getNumeroAfiliado(PatientsEntity paciente, PaymentRequestDTO request) {
        // Prioridad: request, luego paciente
        if (request.getNumeroAfiliado() != null && !request.getNumeroAfiliado().trim().isEmpty()) {
            return request.getNumeroAfiliado();
        }

        String afiliado = paciente.getNroAfiliadoObraSocial();
        if (afiliado == null || afiliado.trim().isEmpty()) {
            throw new IllegalStateException("Número de afiliado no disponible");
        }

        return afiliado;
    }

    private String getEspecialidadNombre(ServiceEntity servicio) {
        if (servicio.getEspecialidad() != null && !servicio.getEspecialidad().trim().isEmpty()) {
            return servicio.getEspecialidad();
        }

        // Si tienes SpecialtyEntity relacionada, podrías buscarla aquí
        // Por ahora, retornar un valor por defecto
        return "Servicio médico";
    }

    private PaymentResult processSuccessfulPayment(PaymentRequestDTO request, PaymentsEntity pago,
                                                   ServiceEntity servicio, String codigoObraSocial,
                                                   String numeroAfiliado, Map<String, Object> autorizacion) {

        String authNumber = (String) autorizacion.get("numeroAutorizacion");

        // Usar porcentaje de cobertura por defecto (70%)
        BigDecimal porcentajeCobertura = new BigDecimal("70");
        BigDecimal montoCobertura = calcularMontoCobertura(request.getMonto(), porcentajeCobertura);
        BigDecimal copago = calcularCopago(request.getMonto(), montoCobertura);

        // Actualizar entidad de pago
        updatePaymentEntity(pago, authNumber, montoCobertura, copago);

        // Actualizar servicio si es posible (necesitarías agregar campos a ServiceEntity)
        updateServiceIfPossible(servicio, montoCobertura, copago);

        log.info("✅ Pago con obra social autorizado. Obra Social: {}, Cobertura: {}, Copago: {}",
                codigoObraSocial, montoCobertura, copago);

        // Construir respuesta
        return buildSuccessResponse(authNumber, codigoObraSocial, numeroAfiliado,
                porcentajeCobertura, montoCobertura, copago);
    }

    private BigDecimal calcularMontoCobertura(BigDecimal montoTotal, BigDecimal porcentajeCobertura) {
        return montoTotal
                .multiply(porcentajeCobertura)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularCopago(BigDecimal montoTotal, BigDecimal montoCobertura) {
        return montoTotal.subtract(montoCobertura);
    }

    private void updatePaymentEntity(PaymentsEntity pago, String authNumber,
                                     BigDecimal montoCobertura, BigDecimal copago) {
        pago.setEstadoPago(PagoEstado.COMPLETADO);
        pago.setFechaPago(LocalDateTime.now());
        pago.setFechaProcesamiento(LocalDateTime.now());
        pago.setIdTransaccion(authNumber);
        pago.setCodigoAutorizacion(authNumber);
        pago.setMensajeError(null);
        pago.setNumeroAutorizacionOs(authNumber);
        pago.setMontoCubiertoOs(montoCobertura);
        pago.setMontoCopago(copago);
    }

    private void updateServiceIfPossible(ServiceEntity servicio,
                                         BigDecimal montoCobertura, BigDecimal copago) {
        // Si ServiceEntity tuviera estos campos, los actualizaríamos aquí
        // Por ahora, solo logueamos
        log.debug("Servicio actualizado con cobertura: {} y copago: {}", montoCobertura, copago);
    }

    private PaymentResult buildSuccessResponse(String authNumber, String codigoObraSocial,
                                               String numeroAfiliado, BigDecimal porcentajeCobertura,
                                               BigDecimal montoCobertura, BigDecimal copago) {

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("obra_social", codigoObraSocial);
        additionalData.put("codigo_obra_social", codigoObraSocial);
        additionalData.put("numero_afiliado", numeroAfiliado);
        additionalData.put("numero_autorizacion", authNumber);
        additionalData.put("porcentaje_cobertura", porcentajeCobertura);
        additionalData.put("monto_cobertura", montoCobertura);
        additionalData.put("monto_copago", copago);
        additionalData.put("requiere_factura", true);
        additionalData.put("tipo_factura", "A");

        return PaymentResult.success(
                String.format("Pago con obra social %s autorizado exitosamente. Cobertura: $%.2f, Copago: $%.2f",
                        codigoObraSocial, montoCobertura, copago),
                authNumber,
                additionalData
        );
    }

    private PaymentResult handleValidationError(PaymentsEntity pago, String mensaje) {
        pago.setEstadoPago(PagoEstado.FALLIDO);
        pago.setMensajeError("Validación fallida: " + mensaje);
        return PaymentResult.failure("Validación fallida: " + mensaje);
    }

    private PaymentResult handleGeneralError(PaymentsEntity pago, String mensaje) {
        pago.setEstadoPago(PagoEstado.FALLIDO);
        pago.setMensajeError("Error con obra social: " + mensaje);
        return PaymentResult.failure("Error con obra social: " + mensaje);
    }

    private Map<String, Object> simularAutorizacionObraSocial(String codigoObraSocial, String nroAfiliado,
                                                              BigDecimal monto, String especialidad) {
        Map<String, Object> resultado = new HashMap<>();

        // Simulación: 90% de autorizaciones exitosas
        boolean autorizado = Math.random() < 0.9;

        resultado.put("autorizado", autorizado);
        resultado.put("numeroAutorizacion", "AUTH-OS-" + System.currentTimeMillis());

        if (!autorizado) {
            String[] motivos = {
                    "Afiliado sin cobertura para esta especialidad",
                    "Monto excede el máximo cubierto",
                    "Falta autorización previa",
                    "Afiliado en estado moroso",
                    "Cobertura suspendida temporalmente"
            };
            resultado.put("motivoRechazo", motivos[(int) (Math.random() * motivos.length)]);
        }

        // Log de simulación
        log.debug("Simulación obra social - Código: {}, Afiliado: {}, Especialidad: {}, Autorizado: {}",
                codigoObraSocial, nroAfiliado, especialidad, autorizado);

        return resultado;
    }

    @Override
    public boolean supports(String metodoPago) {
        return "OBRA_SOCIAL".equalsIgnoreCase(metodoPago);
    }

    @Override
    public void validate(PaymentRequestDTO request) {
        // Validación para método obra social
        if (request.getNumeroAfiliado() == null && request.getCodigoObraSocial() == null) {
            throw new IllegalArgumentException("Para obra social se requiere número de afiliado o código de obra social");
        }

        // Validar que la obra social esté en nuestro sistema
        if (request.getCodigoObraSocial() != null) {
            String[] obrasSocialesValidas = {"OSDE", "SWISS_MEDICAL", "GALENO", "OMINT", "MEDICUS", "IOMA", "PAMI"};
            boolean valida = false;
            for (String os : obrasSocialesValidas) {
                if (os.equalsIgnoreCase(request.getCodigoObraSocial())) {
                    valida = true;
                    break;
                }
            }
            if (!valida) {
                throw new IllegalArgumentException("Obra social no reconocida: " + request.getCodigoObraSocial());
            }
        }

        // Validar monto mínimo
        if (request.getMonto() != null && request.getMonto().compareTo(new BigDecimal("1.00")) < 0) {
            throw new IllegalArgumentException("Monto mínimo para obra social: $1.00");
        }
    }
}