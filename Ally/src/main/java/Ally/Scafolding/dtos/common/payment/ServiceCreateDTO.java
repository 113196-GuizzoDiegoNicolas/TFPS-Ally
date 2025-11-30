package Ally.Scafolding.dtos.common.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCreateDTO {
    private Long pacienteId;
    private Long prestadorId;
    private Long transportistaId;
    private String especialidad;
    private String descripcion;
}
