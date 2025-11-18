package Ally.Scafolding.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "transportistas")
@Audited
public class TransportersEntity extends PersonsEntity {

    @Column(name = "zona_cobertura", nullable = false)
    private String zonaCobertura;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}
