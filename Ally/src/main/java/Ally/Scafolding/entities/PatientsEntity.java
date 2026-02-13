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
@Table(name = "patients")
@Audited
public class PatientsEntity extends PersonsEntity {

    @Column(name = "numero_historia_clinica", nullable = false)
    private String numeroHistoriaClinica;

    @Column(name = "codigo_obra_social", nullable = false)
    private String codigoObraSocial;

    @Column(name = "nro_afiliado_obra_social", nullable = false)
    private String nroAfiliadoObraSocial;

    @Column(name = "tipo_discapacidad")
    private String tipoDiscapacidad;
}
