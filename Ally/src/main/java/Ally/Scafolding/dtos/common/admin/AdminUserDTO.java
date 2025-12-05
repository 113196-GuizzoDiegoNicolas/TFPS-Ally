package Ally.Scafolding.dtos.common.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDTO {
    private Long id;
    private String nombre; // O cambiar luego por usuario si hace falta
    private String rol;
    private boolean activo;
}
