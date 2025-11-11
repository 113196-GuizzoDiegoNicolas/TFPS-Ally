package Ally.Scafolding.models;

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
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private String telegram;
    private String correoElectronico;

    // Relación con usuario
    private Long idUsuario;

    // Datos médicos
    private String numeroHistoriaClinica;
    private String codigoObraSocial;
    private String nroAfiliadoObraSocial;
    private String tipoDiscapacidad;
}

