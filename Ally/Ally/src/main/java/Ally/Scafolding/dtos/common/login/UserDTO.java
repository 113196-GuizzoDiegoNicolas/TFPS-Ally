package Ally.Scafolding.dtos.common.login;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents user information in the system.
 * <p>
 *     This DTO contains user details without sensitive password information.
 * </p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String password;
    private boolean active;
    private boolean locked;


}
