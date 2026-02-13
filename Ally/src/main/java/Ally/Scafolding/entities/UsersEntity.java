package Ally.Scafolding.entities;

import Ally.Scafolding.models.Rol;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "usuarios")
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario", nullable = false, unique = true)
    private String usuario;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    /**
     * @Enumerated(EnumType.STRING)*/
    @Column(name = "rol", nullable = false)
    private String rol;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "bloqueado", nullable = false)
    private Boolean bloqueado = false;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;

    @Column(name = "intentos_fallidos", nullable = false)
    private Integer intentosFallidos = 0;
}
