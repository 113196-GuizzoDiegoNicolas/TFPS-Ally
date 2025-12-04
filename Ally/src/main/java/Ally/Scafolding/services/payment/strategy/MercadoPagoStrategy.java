package Ally.Scafolding.services.payment.strategy;

import Ally.Scafolding.entities.PaymentsEntity;
import Ally.Scafolding.models.PagoEstado;
import Ally.Scafolding.dtos.common.payment.PaymentRequestDTO;
import Ally.Scafolding.services.payment.MercadoPagoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MercadoPagoStrategy implements PaymentStrategy {

    private final MercadoPagoService mercadoPagoService;

    @Value("${app.base-url:http://localhost:8080}")
    private String appBaseUrl;

    @Override
    public PaymentResult processPayment(PaymentRequestDTO request, PaymentsEntity pago) {
        log.info("💳 Procesando pago con Mercado Pago para servicio: {}", request.getServicioId());

        try {
            // 1. Validar datos requeridos
            validatePaymentRequest(request);

            // 2. Preparar URLs de retorno
            String returnUrl = prepareReturnUrl(request);
            String cancelUrl = prepareCancelUrl(request);

            log.debug("🌐 Return URL: {}", returnUrl);
            log.debug("🌐 Cancel URL: {}", cancelUrl);

            // 3. Crear preferencia en Mercado Pago (CORREGIDO - usar método con parámetros correctos)
            Map<String, Object> preferenceData = mercadoPagoService.createPreference(
                    request.getServicioId(),
                    request.getMonto(),
                    request.getEmailPagador(),
                    request.getNombrePagador() != null ? request.getNombrePagador() : "Cliente"
            );

            // 4. Extraer datos de la preferencia
            String preferenceId = (String) preferenceData.get("id");
            String paymentUrl = (String) preferenceData.get("init_point");

            if (preferenceId == null || paymentUrl == null) {
                throw new RuntimeException("No se pudo crear la preferencia de pago");
            }

            // 5. Actualizar entidad de pago
            updatePaymentEntity(pago, preferenceId, preferenceData);

            log.info("✅ Preferencia Mercado Pago creada: {}. URL: {}",
                    preferenceId,
                    paymentUrl.length() > 50 ? paymentUrl.substring(0, 50) + "..." : paymentUrl);

            // 6. Construir resultado
            return buildSuccessResult(preferenceId, paymentUrl, preferenceData);

        } catch (IllegalArgumentException e) {
            log.warn("⚠️ Validación fallida para Mercado Pago: {}", e.getMessage());
            return PaymentResult.failure("Validación fallida: " + e.getMessage());

        } catch (Exception e) {
            log.error("❌ Error procesando pago con Mercado Pago: {}", e.getMessage(), e);
            return handleError(pago, e.getMessage());
        }
    }

    // ============ MÉTODOS PRIVADOS ============

    private void validatePaymentRequest(PaymentRequestDTO request) {
        if (request.getServicioId() == null) {
            throw new IllegalArgumentException("ID de servicio es requerido");
        }

        if (request.getMonto() == null || request.getMonto().doubleValue() <= 0) {
            throw new IllegalArgumentException("Monto inválido para Mercado Pago");
        }

        if (request.getEmailPagador() == null || request.getEmailPagador().trim().isEmpty()) {
            throw new IllegalArgumentException("Email del pagador es requerido para Mercado Pago");
        }

        // Validar formato de email
        if (!isValidEmail(request.getEmailPagador())) {
            throw new IllegalArgumentException("Email inválido");
        }

        // Validar montos mínimos/máximos
        double montoMinimo = 1.00;
        double montoMaximo = 1000000.00;

        if (request.getMonto().doubleValue() < montoMinimo) {
            throw new IllegalArgumentException("Monto mínimo para Mercado Pago: $1.00");
        }

        if (request.getMonto().doubleValue() > montoMaximo) {
            throw new IllegalArgumentException("Monto máximo para Mercado Pago: $1,000,000.00");
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private String prepareReturnUrl(PaymentRequestDTO request) {
        // Prioridad: URL del request, sino URL por defecto
        if (request.getReturnUrl() != null && !request.getReturnUrl().trim().isEmpty()) {
            return request.getReturnUrl();
        }
        return appBaseUrl + "/payment/success";
    }

    private String prepareCancelUrl(PaymentRequestDTO request) {
        // Prioridad: URL del request, sino URL por defecto
        if (request.getCancelUrl() != null && !request.getCancelUrl().trim().isEmpty()) {
            return request.getCancelUrl();
        }
        return appBaseUrl + "/payment/cancel";
    }

    private void updatePaymentEntity(PaymentsEntity pago, String preferenceId,
                                     Map<String, Object> preferenceData) {
        // Para Mercado Pago, el pago queda pendiente hasta que el usuario complete el pago
        pago.setEstadoPago(PagoEstado.PENDIENTE);
        pago.setFechaCreacion(LocalDateTime.now());
        pago.setIdTransaccion(preferenceId);
        pago.setMensajeError(null);

        // Guardar datos adicionales en mensajeError o en un campo adicional
        String datosAdicionales = String.format(
                "Preference ID: %s, URL: %s, External Ref: %s",
                preferenceId,
                preferenceData.get("init_point"),
                preferenceData.get("external_reference")
        );

        // Si PaymentsEntity tiene campo para datos adicionales, usarlo
        // Sino, usar mensajeError temporalmente
        pago.setMensajeError(datosAdicionales);

        log.debug("📝 Pago actualizado con preferenceId: {}", preferenceId);
    }

    private PaymentResult buildSuccessResult(String preferenceId, String paymentUrl,
                                             Map<String, Object> preferenceData) {
        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("preference_id", preferenceId);
        additionalData.put("payment_url", paymentUrl);
        additionalData.put("sandbox_url", preferenceData.get("sandbox_init_point"));
        additionalData.put("external_reference", preferenceData.get("external_reference"));

        // Agregar otros datos si existen
        if (preferenceData.get("expires") != null) {
            additionalData.put("expires", preferenceData.get("expires"));
        }

        if (preferenceData.get("expiration_date_to") != null) {
            additionalData.put("expiration_date", preferenceData.get("expiration_date_to"));
        }

        // Verificar si es simulado
        if (preferenceData.get("simulated") != null && (Boolean) preferenceData.get("simulated")) {
            additionalData.put("simulated", true);
            additionalData.put("message", "Modo desarrollo - Transacción simulada");
        }

        return PaymentResult.success(
                "Preferencia de pago Mercado Pago creada exitosamente. Redirija al usuario a la URL de pago.",
                preferenceId,
                additionalData
        );
    }

    private PaymentResult handleError(PaymentsEntity pago, String errorMessage) {
        pago.setEstadoPago(PagoEstado.FALLIDO);
        pago.setMensajeError("Error Mercado Pago: " + errorMessage);

        return PaymentResult.failure("Error procesando pago con Mercado Pago: " + errorMessage);
    }

    @Override
    public boolean supports(String metodoPago) {
        return "MERCADO_PAGO".equalsIgnoreCase(metodoPago);
    }

    @Override
    public void validate(PaymentRequestDTO request) {
        // Validaciones específicas para Mercado Pago
        validatePaymentRequest(request);

        // Validación adicional: email válido
        if (!isValidEmail(request.getEmailPagador())) {
            throw new IllegalArgumentException("Email inválido. Formato esperado: usuario@dominio.com");
        }

        // Validar que el nombre no sea requerido pero si está, que no sea vacío
        if (request.getNombrePagador() != null && request.getNombrePagador().trim().isEmpty()) {
            throw new IllegalArgumentException("Si se proporciona nombre, no puede estar vacío");
        }
    }
}