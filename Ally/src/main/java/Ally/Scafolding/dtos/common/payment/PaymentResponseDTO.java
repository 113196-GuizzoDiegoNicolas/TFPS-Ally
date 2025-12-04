package Ally.Scafolding.dtos.common.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Respuesta del procesamiento de pago")
public class PaymentResponseDTO {

    @Schema(description = "ID del pago en el sistema", example = "456")
    private Long pagoId;

    @Schema(description = "Estado del pago",
            example = "PROCESANDO",
            allowableValues = {"PENDIENTE", "PROCESANDO", "COMPLETADO", "FALLIDO", "REEMBOLSADO"})
    private String estado;

    @Schema(description = "Mensaje descriptivo", example = "Pago procesado exitosamente")
    private String mensaje;

    @Schema(description = "ID de transacción externa", example = "MP-123456789")
    private String transaccionId;

    @Schema(description = "Fecha y hora del pago")
    private LocalDateTime fechaPago;

    @Schema(description = "Monto pagado", example = "25000.50")
    private BigDecimal monto;

    @Schema(description = "Método de pago utilizado", example = "MERCADO_PAGO")
    private String metodoPago;

    @Schema(description = "Detalles del servicio pagado", example = "Servicio #123 - Cardiología")
    private String servicioDetalle;

    // Campos específicos para Mercado Pago
    @Schema(description = "URL de pago generada (para Mercado Pago)")
    private String paymentUrl;

    @Schema(description = "ID de preferencia de Mercado Pago")
    private String preferenceId;

    @Schema(description = "ID de pago de Mercado Pago")
    private String mercadoPagoPaymentId;

    // Campos para obra social
    @Schema(description = "Número de autorización de obra social")
    private String numeroAutorizacion;

    @Schema(description = "Monto cubierto por la obra social")
    private BigDecimal coberturaAseguradora;

    @Schema(description = "Monto de copago del paciente")
    private BigDecimal copagoPaciente;

    // Datos adicionales
    @Schema(description = "Datos adicionales de la transacción")
    private Map<String, Object> datosAdicionales;

    @Schema(description = "Código de error si falló")
    private String errorCode;

    @Schema(description = "Si es éxito o no")
    private Boolean success;
}