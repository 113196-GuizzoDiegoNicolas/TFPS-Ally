package Ally.Scafolding.services.payment.strategy;

import Ally.Scafolding.entities.PaymentsEntity;
import Ally.Scafolding.models.PagoEstado;
import Ally.Scafolding.dtos.common.payment.PaymentRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CashPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResult processPayment(PaymentRequestDTO request, PaymentsEntity pago) {
        log.info("Procesando pago en contado para servicio: {}", request.getServicioId());

        try {
            // En pago contado, se marca como completado inmediatamente
            pago.setEstadoPago(PagoEstado.COMPLETADO);
            pago.setFechaPago(LocalDateTime.now());
            pago.setFechaProcesamiento(LocalDateTime.now());
            pago.setMensajeError(null);

            // Generar ID de transacción simulado
            String transactionId = "CASH-" + System.currentTimeMillis();
            pago.setIdTransaccion(transactionId);
            pago.setCodigoAutorizacion("CASH-AUTH-" + System.currentTimeMillis());

            log.info("Pago en contado procesado exitosamente. Transacción: {}", transactionId);

            Map<String, Object> additionalData = new HashMap<>();
            additionalData.put("tipo_comprobante", "RECIBO");
            additionalData.put("medio_pago", "EFECTIVO");
            additionalData.put("requiere_fiscal", true);

            return PaymentResult.success(
                    "Pago en contado procesado exitosamente. Se debe presentar el comprobante físico.",
                    transactionId,
                    additionalData
            );

        } catch (Exception e) {
            log.error("Error procesando pago en contado: {}", e.getMessage(), e);
            return PaymentResult.failure("Error procesando pago en contado: " + e.getMessage());
        }
    }

    @Override
    public boolean supports(String metodoPago) {
        return "CONTADO".equalsIgnoreCase(metodoPago);
    }

    @Override
    public void validate(PaymentRequestDTO request) {
        // Validaciones específicas para pago en contado
        if (request.getMonto() == null || request.getMonto().doubleValue() <= 0) {
            throw new IllegalArgumentException("Monto inválido para pago en contado");
        }

        // Puede haber límites para pagos en efectivo
        double montoMaximoEfectivo = 100000.00; // $100,000
        if (request.getMonto().doubleValue() > montoMaximoEfectivo) {
            throw new IllegalArgumentException(
                    String.format("Para montos mayores a $%.2f se recomienda otro método de pago",
                            montoMaximoEfectivo));
        }
    }
}