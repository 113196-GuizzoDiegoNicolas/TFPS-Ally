package Ally.Scafolding.dtos.common.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCreateDTO {
    private Long pacienteId;
    private Long prestadorId;
    private Long transportistaId;
    private String especialidad;
    private String descripcion;
    private BigDecimal  montoApagar;
}
