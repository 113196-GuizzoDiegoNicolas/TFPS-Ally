package Ally.Scafolding.dtos.common.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Solicitud de procesamiento de pago")
public class PaymentRequestDTO {

    @NotNull(message = "El servicio es requerido")
    @Schema(description = "ID del servicio a pagar", example = "123")
    private Long servicioId;

    @NotBlank(message = "El método de pago es requerido")
    @Schema(description = "Método de pago",
            example = "MERCADO_PAGO",
            allowableValues = {"CONTADO", "TRANSFERENCIA_BANCARIA", "OBRA_SOCIAL", "MERCADO_PAGO"})
    private String metodoPago;

    @NotNull(message = "El monto es requerido")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    @Schema(description = "Monto total a pagar", example = "25000.50")
    private BigDecimal monto;

    @Schema(description = "Email del pagador (requerido para Mercado Pago)",
            example = "cliente@example.com")
    @Email(message = "Email debe ser válido")
    private String emailPagador;

    @Schema(description = "Nombre del pagador", example = "Juan Pérez")
    private String nombrePagador;

    @Schema(description = "CBU destino para transferencias", example = "0720321188000034567890")
    private String cbuDestino;

    @Schema(description = "Número de afiliado para obra social", example = "AF123456789")
    private String numeroAfiliado;

    @Schema(description = "Código de obra social", example = "OSDE")
    private String codigoObraSocial;

    @Schema(description = "Datos adicionales específicos del método de pago")
    private Map<String, Object> datosAdicionales;

    @Schema(description = "URL de retorno después del pago (para Mercado Pago)")
    private String returnUrl;

    @Schema(description = "URL de cancelación (para Mercado Pago)")
    private String cancelUrl;
}