package Ally.Scafolding.dtos.common.payment;
import Ally.Scafolding.models.PagoEstado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {
    private Long id;
    private Long servicioId;
    private Double monto;
    private PagoEstado estado;
    private LocalDateTime fechaPago;
    private String idTransaccion;
    private String mensaje;
    private String metodoPago;
}
