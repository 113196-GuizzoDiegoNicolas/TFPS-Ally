package Ally.Scafolding.dtos.common.service;

import Ally.Scafolding.models.PagoEstado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDTO {
    private Long id;
    private Long pacienteId;
    private Long prestadorId;
    private String especialidad;
    private String descripcion;
    private String estado;
    private BigDecimal montoApagar;
    private String fechaSolicitud;

}
