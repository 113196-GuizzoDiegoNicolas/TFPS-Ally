package Ally.Scafolding.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "service_requests")
public class ServiceRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private PatientsEntity paciente;

    @ManyToOne
    @JoinColumn(name = "prestador_id", nullable = false)
    private ProvidersEntity prestador;
    @ManyToOne
    @JoinColumn(name = "transportista_id", nullable = true)
    private TransportersEntity transportista;
    private String especialidad;
    private String descripcion;
    private String tipo;
    private String modalidad;

    @Column(nullable = false)
    private String estado = "PENDIENTE";

    @Column(nullable = false)
    private boolean activo = true;

    private LocalDateTime fechaSolicitud = LocalDateTime.now();
}
