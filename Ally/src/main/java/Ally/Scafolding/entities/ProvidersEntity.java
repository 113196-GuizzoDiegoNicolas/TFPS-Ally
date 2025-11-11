package Ally.Scafolding.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

/**
 * Represents a medical provider in the database.
 * <p>
 *     This entity stores medical provider information including specialty and availability.
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
     * Specialty code of the medical provider.
     */
    @ManyToOne
    @JoinColumn(name = "codigo_especialidad")
    private SpecialtyEntity especialidad;

    /**
     * Indicates if the provider is currently active.
     */
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}

