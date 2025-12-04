// src/main/java/Ally/Scafolding/models/Patient.java
package Ally.Scafolding.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a medical provider in the business domain.
 * <p>
 *     This model class contains the business logic and domain rules for medical providers.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Provider {

    // Datos personales
    private Long id;
    @NotNull(message = "El nombre no puede ser nulo")
    private String nombre;
    @NotNull(message = "El apellido no puede ser nulo")
    private String apellido;
    @Email(message = "El correo electrónico no es válido")
    private String email;
    private String telegram;
    private String telefono;
    private String direccion;
    // Datos médicos
    @NotNull(message = "El código de especialidad no puede ser nulo")
    private String codigoEspecialidad;
    private Boolean activo;
    private String CBUBancaria;
    private LocalDateTime fechaRegistro;
    // Relación con usuario
    private Long idUsuario;
    private String matricula;

}