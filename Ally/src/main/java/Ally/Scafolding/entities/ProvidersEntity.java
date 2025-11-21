package Ally.Scafolding.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "providers")
@Audited
public class ProvidersEntity extends PersonsEntity {

    @ManyToOne
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JoinColumn(name = "codigo_especialidad")
    private SpecialtyEntity especialidad;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}


