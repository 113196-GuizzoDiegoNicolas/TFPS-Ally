package Ally.Scafolding.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a medical appointment in the database.
 * <p>
 *     This entity connects patients with medical providers and optionally with transporters.
 * </p>
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "turnos")
public class TurnsEntity {

    /**
     * Unique identifier for the appointment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Patient associated with the appointment.
     */
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private PatientsEntity paciente;

    /**
     * Medical provider associated with the appointment.
     */
    @ManyToOne
    @JoinColumn(name = "prestador_id", nullable = false)
    private ProvidersEntity prestador;

    /**
     * Transporter assigned to the appointment (optional).
     */
    @ManyToOne
    @JoinColumn(name = "transportista_id")
    private TransportersEntity transportista;

    /**
     * Date and time of the appointment.
     */
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    /**
     * Current status of the appointment.
     */
    @Column(name = "estado", nullable = false)
    private String estado = "PENDIENTE"; // PENDIENTE, CONFIRMADO, COMPLETADO, CANCELADO

    /**
     * Additional observations for the appointment.
     */
    @Column(name = "observaciones")
    private String observaciones;

    /**
     * Date and time when the appointment was created.
     */
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}

