
package Ally.Scafolding.dtos.common.provider;

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
public class ProviderDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String direccion;
    private String codigoEspecialidad;
    private String CBUBancaria;
    private Boolean activo;
    private Long usuarioId;
    private String nombreUsuario;
}
