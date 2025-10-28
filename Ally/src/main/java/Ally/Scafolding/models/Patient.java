// src/main/java/Ally/Scafolding/models/Patient.java
package Ally.Scafolding.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Modelo de dominio para un paciente, alineado con PatientsEntity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    /**
     * Unique identifier for the patient.
     */
    private Long id;

    /**
     * Clinical history number of the patient.
     */
    private String numeroHistoriaClinica;

    /**
     * Social security code.
     */
    private String codigoObraSocial;

    /**
     * Social security affiliate number.
     */
    private String nroAfiliadoObraSocial;

    /**
     * Type of disability.
     */
    private String tipoDiscapacidad;
}