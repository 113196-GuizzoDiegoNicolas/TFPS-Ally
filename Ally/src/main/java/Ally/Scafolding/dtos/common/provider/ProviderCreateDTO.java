
package Ally.Scafolding.dtos.common.provider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * Data Transfer Object para representar un paciente.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProviderCreateDTO {

    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String direccion;
    private String codigoEspecialidad;
    private Long usuarioId;
}
