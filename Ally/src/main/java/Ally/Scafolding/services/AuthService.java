package Ally.Scafolding.services;

import Ally.Scafolding.dtos.common.login.UserLoginDTO;
import Ally.Scafolding.dtos.common.login.LoginResponseDTO;


/**
 * Service interface for authentication operations.
 * <p>
 *     Handles user login and token generation.
 * </p>
 */

public interface AuthService {

    /**
     * Authenticates a user with provided credentials.
     * @param userLoginDTO the login credentials
     * @return LoginResponseDTO with token and user information if authentication is successful
     * @throws RuntimeException if authentication fails
     */
    LoginResponseDTO login(UserLoginDTO userLoginDTO);

    /**
     * Validates a JWT token.
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    boolean validateToken(String token);
}