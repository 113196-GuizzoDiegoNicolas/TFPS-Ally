// src/main/java/Ally/Scafolding/models/Patient.java
package Ally.Scafolding.models;

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


    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String direccion;
    private String codigoEspecialidad;
    private Boolean activo;
    private LocalDateTime fechaRegistro;
    private User usuario;

}