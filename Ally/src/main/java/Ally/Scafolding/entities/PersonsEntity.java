package Ally.Scafolding.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.time.LocalDate;

/**
 * Abstract base class for people entities.
 * Contains common attributes for patients and providers.
 */
@MappedSuperclass
@Data
public abstract class PersonsEntity {

    /**
     * First name of the person.
     */
    @Column(name = "nombre", nullable = false)
    private String nombre;

    /**
     * Last name of the person.
     */
    @Column(name = "apellido", nullable = false)
    private String apellido;

    /**
     * Date of birth.
     * Accepts and serializes in format dd-MM-yyyy (e.g., 22-02-2022)
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    /**
     * Address of the person.
     */
    @Column(name = "direccion", nullable = true)
    private String direccion;

    /**
     * Phone number of the person.
     */
    @Column(name = "telefono", nullable = true)
    private String telefono;

    /**
     * Telegram contact of the person.
     */
    @Column(name = "telegram", nullable = true)
    private String telegram;

    /**
     * Email address of the person.
     */
    @Column(name = "correo_electronico", nullable = true)
    private String correoElectronico;

    /**
     * User ID for system access.
     */
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;
}
