package Ally.Scafolding.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

/**
 * Represents a medical provider in the database.
 * <p>
 * This entity stores medical provider information including specialty and availability.
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codigo_especialidad")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private SpecialtyEntity especialidad;

    /**
     * Indicates if the provider is currently active.
     */
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    /**
     * Bank account details (CBU) of the provider.
     */
    @Column(name = "cbu_bancaria", nullable = true)
    private String CBUBancaria;

}
