package Ally.Scafolding.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * Represents a notification in the database.
 * <p>
 *     This entity stores its ID as well its name and body.
 * </p>
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patients")
@Audited
public class PatientsEntity extends PersonsEntity {

    /**
     * Unique identifier for the patient.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Clinical history number of the patient.
     */
    @Column(name = "numero_historia_clinica", nullable = false)
    private String numeroHistoriaClinica;

    /**
     * Social security code.
     */
    @Column(name = "codigo_obra_social", nullable = false)
    private String codigoObraSocial;

    /**
     * Social security affiliate number.
     */
    @Column(name = "nro_afiliado_obra_social", nullable = false)
    private String nroAfiliadoObraSocial;



    /**
     * Type of disability.
     */
    @Column(name = "tipo_discapacidad", nullable = true)
    private String tipoDiscapacidad;

}