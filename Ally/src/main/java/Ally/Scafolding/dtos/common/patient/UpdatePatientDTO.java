package Ally.Scafolding.dtos.common.patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualización parcial del Paciente.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePatientDTO {

    private Long id;

    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    private String tipoDiscapacidad;

    private String codigoObraSocial;
    private String nroAfiliadoObraSocial;
}
