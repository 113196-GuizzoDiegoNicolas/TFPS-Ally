// src/main/java/Ally/Scafolding/dtos/common/contact/PatientDTO.java
package Ally.Scafolding.dtos.common.person;

import Ally.Scafolding.dtos.common.login.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Base DTO class for person entities.
 * <p>
 *     Contains common attributes for patients, providers and transporters.
 * </p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PersonDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private String telegram;
    private String correoElectronico;
    private UserDTO usuario;
}
