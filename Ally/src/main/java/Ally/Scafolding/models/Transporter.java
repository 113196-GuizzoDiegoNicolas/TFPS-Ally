package Ally.Scafolding.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object for Transporter entities.
 * Used to transfer transporter data between layers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transporter {
    private Long id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private String telegram;
    private String correoElectronico;
    private Long usuarioId;
    private String zonaCobertura;
    private Boolean activo;
}