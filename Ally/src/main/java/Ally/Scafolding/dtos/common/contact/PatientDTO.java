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

    private Long id;
    private String nombre;
    private String apellido;
    private String fechaNacimiento;
    private String direccion;
    private String telefono;
    private String telegram;
    private String correoElectronico;
    private Long idUsuario;
    private String numeroHistoriaClinica;
    private String codigoObraSocial;
    private String nroAfiliadoObraSocial;
    private String tipoDiscapacidad;
}
