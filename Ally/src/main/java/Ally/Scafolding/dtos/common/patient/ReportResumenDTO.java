package Ally.Scafolding.dtos.common.patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResumenDTO {
    private long totalServicios;
    private long aceptados;
    private long pendientes;
    private long rechazados;
    private BigDecimal totalPagado;
}