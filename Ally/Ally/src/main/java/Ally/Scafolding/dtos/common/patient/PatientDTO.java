// src/main/java/Ally/Scafolding/dtos/common/contact/PatientDTO.java
package Ally.Scafolding.dtos.common.patient;

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


    private String numeroHistoriaClinica;
    private String codigoObraSocial;
    private String nroAfiliadoObraSocial;
    private String tipoDiscapacidad;
}
