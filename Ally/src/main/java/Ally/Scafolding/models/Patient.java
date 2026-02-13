package Ally.Scafolding.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    private Long id;

    // Datos personales
    @NotNull(message = "El nombre no puede ser nulo")
    private String nombre;
    @NotNull(message = "El apellido no puede ser nulo")
    private String apellido;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;

    private String telegram;
    @Email(message = "El correo electrónico no es válido")
    private String correoElectronico;

    // Relación con usuario
    private Long idUsuario;

    // Datos médicos
    private String numeroHistoriaClinica;
    private String codigoObraSocial;
    private String nroAfiliadoObraSocial;
    @NotNull(message = "El tipo de discapacidad no puede ser nulo")
    private String tipoDiscapacidad;
}

