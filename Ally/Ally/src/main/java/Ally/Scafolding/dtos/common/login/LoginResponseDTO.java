package Ally.Scafolding.dtos.common.login;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the response after successful login.
 * <p>
 *     This DTO contains authentication token and user information.
 * </p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponseDTO {
    /**
     * JWT token for authenticated requests.
     */
    private String token;

    /**
     * User information.
     */
    private UserDTO usuario;

    /**
     * Role of the authenticated user.
     */
    private String rol;

}
