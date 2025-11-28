package Ally.Scafolding.entities;

import Ally.Scafolding.models.PagoEstado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "services")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pacienteId;

    // Prestador o Transportista (uno de los dos será null)
    private Long prestadorId;
    private Long transportistaId;

    private String especialidad;

    @Column(length = 500)
    private String descripcion;

    private String estado; // PENDIENTE, ACEPTADA
    private LocalDateTime fechaSolicitud;

    @Enumerated(EnumType.STRING)
    private PagoEstado estadoPago;
}
