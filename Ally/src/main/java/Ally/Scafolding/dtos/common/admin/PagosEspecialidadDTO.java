package Ally.Scafolding.dtos.common.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagosEspecialidadDTO {
    private String especialidad;
    private Long cantidad;
}
