package Ally.Scafolding.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "patient_locations")
public class PatientLocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referencia al paciente SIN modificar PatientsEntity
    @ManyToOne(optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private PatientsEntity paciente;

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lng;

    private String addressText;

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}
