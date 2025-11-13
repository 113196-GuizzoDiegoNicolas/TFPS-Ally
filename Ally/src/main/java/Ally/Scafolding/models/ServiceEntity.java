package Ally.Scafolding.models;

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
    private Long prestadorId;
    private String especialidad;

    @Column(length = 500)
    private String descripcion;

    private String estado; // PENDIENTE, ACEPTADA, EN_CURSO, FINALIZADA
    private LocalDateTime fechaSolicitud;
}