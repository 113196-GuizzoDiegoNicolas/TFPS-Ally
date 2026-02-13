
package Ally.Scafolding.dtos.common.provider;

import Ally.Scafolding.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * Data Transfer Object para representar un paciente.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProviderCreateDTO {


    // Datos personales
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String direccion;
    // Datos médicos
    private String codigoEspecialidad;
    private Boolean activo;
    // Relación con usuario
    private Long idUsuario;
}
