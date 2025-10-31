package Ally.Scafolding.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

/**
 * Represents a provider (medical or transport) in the database.
 * <p>
 *     This entity stores provider information such as specialty, status, contact details, and messaging.
 * </p>
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "providers")
@Audited
public class ProvidersEntity extends PersonsEntity {


    /**
     * Identificador único del prestador.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Especialidad del prestador.
     */
    @ManyToOne
    @JoinColumn(name = "id_especialidad", nullable = false)
    private SpecialtyEntity specialty;

    /**
     * Estado del prestador (activo o inactivo).
     */
    @Column(name = "estado", nullable = false)
    private String estado;
}

