package Ally.Scafolding.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents user login credentials in the database.
 * <p>
 *     This entity stores user authentication information separately from personal data.
 * </p>
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuarios")
public class UsersEntity {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username for system access.
     */
    @Column(name = "usuario", nullable = false, unique = true)
    private String usuario;

    /**
     * Password for system access.
     */
    @Column(name = "password", nullable = false)
    private String password;
    /**
     * Email address of the user.
     * Used for communication and password recovery.
     */
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;


    /**
     * Role of the user in the system.
     */
    @Column(name = "rol", nullable = false)
    private String rol; // PACIENTE, PRESTADOR, TRANSPORTISTA
    /**
     * Indicates if the user account is active.
     */
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    /**
     * Indicates if the user account is locked.
     * Locked accounts cannot be used for login.
     */
    @Column(name = "bloqueado", nullable = false)
    private Boolean bloqueado = false;

    /**
     * Date and time when the user account was created.
     */
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    /**
     * Date and time of the last user login.
     */
    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;

    /**
     * Number of failed login attempts.
     * Resets after successful login.
     */
    @Column(name = "intentos_fallidos", nullable = false)
    private Integer intentosFallidos = 0;
}