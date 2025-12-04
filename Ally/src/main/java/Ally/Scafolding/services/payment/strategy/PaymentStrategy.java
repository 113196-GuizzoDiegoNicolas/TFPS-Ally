package Ally.Scafolding.services.payment.strategy;

import Ally.Scafolding.entities.PaymentsEntity;
import Ally.Scafolding.dtos.common.payment.PaymentRequestDTO;

/**
 * Interfaz para el patrón Strategy de pagos
 */
public interface PaymentStrategy {

    /**
     * Procesa el pago según la estrategia específica
     */
    PaymentResult processPayment(PaymentRequestDTO request, PaymentsEntity pago);

    /**
     * Valida si la estrategia puede manejar el tipo de pago
     */
    boolean supports(String metodoPago);

    /**
     * Valida los datos específicos de la estrategia
     */
    void validate(PaymentRequestDTO request);
}