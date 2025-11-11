package Ally.Scafolding.dtos.common.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents user creation request data.
 * <p>
 *     This DTO is used specifically for creating new user accounts.
 *     It includes password field for initial setup.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {

    /**
     * Username for system access.
     * Must be unique across the system.
     */
    private String username;

    /**
     * Password for system access.
     * Will be encrypted before storage.
     */
    private String password;

    /**
     * Email address of the user.
     * Used for communication and must be unique.
     */
    private String email;

    /**
     * Role of the user in the system.
     * Determines access permissions and capabilities.
     */
    private String role; // PACIENTE, PRESTADOR, TRANSPORTISTA, ADMIN
}


