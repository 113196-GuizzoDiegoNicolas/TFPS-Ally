package Ally.Scafolding.dtos.common.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMetricsDTO {
    private long pacientes;
    private long prestadores;
    private long transportistas;
    private long admins;
}
