// src/main/java/Ally/Scafolding/dtos/common/contact/PatientDTO.java
package Ally.Scafolding.dtos.common.contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object para representar un paciente.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PatientDTO {

    /**
     * Identificador único del paciente.
     */
    private Long id;

    /**
     * Número de historia clínica del paciente.
     */
    private String numeroHistoriaClinica;

    /**
     * Código de obra social.
     */
    private String codigoObraSocial;

    /**
     * Número de afiliado a la obra social.
     */
    private String nroAfiliadoObraSocial;

    /**
     * Tipo de discapacidad.
     */
    private String tipoDiscapacidad;
}
