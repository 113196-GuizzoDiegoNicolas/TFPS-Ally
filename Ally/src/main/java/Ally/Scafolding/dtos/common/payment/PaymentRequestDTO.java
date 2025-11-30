package Ally.Scafolding.dtos.common.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitar un pago asociado a un servicio.
 *
 * Contiene el identificador del servicio y del método de pago a utilizar,
 * el monto a debitar y, cuando aplique, los datos de tarjeta para pagos con tarjeta.
 *
 * Uso típico:
 * - Cuando el pago se realiza por un método registrado solo se requiere {@link #servicioId},
 *   {@link #metodoPagoId} y {@link #monto}.
 * - Cuando se procesa un pago con tarjeta, se deben proporcionar {@link #tokenTarjeta}
 *   (si existe token), o bien {@link #numeroTarjeta}, {@link #fechaExpiracion} y {@link #cvv}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {
    /**
     * Id del servicio asociado al pago.
     */
    private Long servicioId;

    /**
     * Id del método de pago a utilizar (tarjeta, wallet, etc.).
     */
    private Long metodoPagoId;

    /**
     * Monto a pagar en la moneda y formato acordado por la API.
     */
    private Double monto;

    /**
     * Token de tarjeta (cuando se usa tokenización / vaulting).
     * Preferible frente a enviar datos sensibles en crudo.
     */
    private String tokenTarjeta; // Para pagos con tarjeta

    /**
     * Número de tarjeta en formato PAN (cuando no se usa token).
     * Evitar enviar si existe token; aplicar cifrado/seguridad según normativa.
     */
    private String numeroTarjeta;

    /**
     * Fecha de expiración de la tarjeta (MM/AA o MM/YYYY según convención).
     */
    private String fechaExpiracion;

    /**
     * Código de seguridad CVV de la tarjeta.
     * Manipular como dato sensible y no persistir en claro.
     */
    private String cvv;
}