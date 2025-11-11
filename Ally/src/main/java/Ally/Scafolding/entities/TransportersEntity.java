package Ally.Scafolding.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

/**
 * Represents a transporter in the database.
 * <p>
 *     This entity stores transporter information including coverage zone and availability.
 * </p>
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transportistas")
public class TransportersEntity extends PersonsEntity {

    /**
     * Unique identifier for the transporter.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Coverage zone where the transporter operates.
     */
    @Column(name = "zona_cobertura", nullable = false)
    private String zonaCobertura;

    /**
     * Indicates if the transporter is currently active.
     */
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}

