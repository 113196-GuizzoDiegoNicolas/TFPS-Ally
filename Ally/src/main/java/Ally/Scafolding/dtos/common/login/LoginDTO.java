package Ally.Scafolding.dtos.common.login;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Represents user login credentials.
 * <p>
 *     This DTO is used for user authentication requests.
 * </p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginDTO {
    /**
     * Username for system access.
     */


    /**
     * Password for system access.
     */
    private String password;
    private String email;   // cambio para coincidir con el front

}
