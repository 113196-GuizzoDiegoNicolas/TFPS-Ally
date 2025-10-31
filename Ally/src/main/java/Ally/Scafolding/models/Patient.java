// src/main/java/Ally/Scafolding/models/Patient.java
package Ally.Scafolding.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo de dominio para un paciente, alineado con PatientsEntity y PersonsEntity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    /**
     * Identificador único del paciente.
     */
    private Long id;

    /**
     * Nombre del paciente.
     */
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    /**
     * Apellido del paciente.
     */
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    /**
     * Fecha de nacimiento del paciente (en formato dd-MM-yyyy).
     */
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private String fechaNacimiento;

    /**
     * Dirección del paciente.
     */
    private String direccion;

    /**
     * Teléfono de contacto del paciente.
     */
    private String telefono;

    /**
     * Usuario de Telegram (opcional).
     */
    private String telegram;

    /**
     * Correo electrónico del paciente.
     */
    private String correoElectronico;

    /**
     * ID del usuario vinculado (sistema de autenticación).
     */
    private Long idUsuario;

    /**
     * Número de historia clínica.
     */
    private String numeroHistoriaClinica;

    /**
     * Código de la obra social.
     */
    private String codigoObraSocial;

    /**
     * Número de afiliado de la obra social.
     */
    private String nroAfiliadoObraSocial;

    /**
     * Tipo de discapacidad (si aplica).
     */
    private String tipoDiscapacidad;
}
