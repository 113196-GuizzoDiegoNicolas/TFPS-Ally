package Ally.Scafolding.dtos.common.admin;

public class AdminUserDTO {
    private Long id;
    private String nombre;
    private String rol;
    private boolean activo;

    public AdminUserDTO(Long id, String nombre, String rol, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.activo = activo;
    }

    // Getters & Setters
}
