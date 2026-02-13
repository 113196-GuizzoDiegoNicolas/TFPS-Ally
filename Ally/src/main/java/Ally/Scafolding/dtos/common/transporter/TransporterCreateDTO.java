package Ally.Scafolding.dtos.common.transporter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object for creating new transporters.
 * Used specifically for creation operations to separate concerns
 * from update operations. Does not include ID or activation status
 * as these are set by the system.
 *
 * @version 1.0
 * @since 2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransporterCreateDTO {

    /**
     * First name of the transporter. Cannot be null or empty.
     */
    private String nombre;

    /**
     * Last name of the transporter. Cannot be null or empty.
     */
    private String apellido;

    /**
     * Date of birth of the transporter.
     */
    private LocalDate fechaNacimiento;

    /**
     * Physical address of the transporter.
     */
    private String direccion;

    /**
     * Contact phone number.
     */
    private String telefono;

    /**
     * Telegram username for communication.
     */
    private String telegram;

    /**
     * Email address for official communications. Cannot be null or empty.
     */
    private String correoElectronico;

    /**
     * Reference to the associated user account ID. Cannot be null.
     */
    private Long usuarioId;

    /**
     * Geographical area where the transporter operates. Cannot be null or empty.
     */
    private String zonaCobertura;
}