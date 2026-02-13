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
@Table(name = "turnos")
public class TurnsEntity {

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
    @JoinColumn(name = "transportista_id")
    private TransportersEntity transportista;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "estado", nullable = false)
    private String estado = "PENDIENTE";

    private String observaciones;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
