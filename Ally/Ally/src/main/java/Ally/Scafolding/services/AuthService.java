package Ally.Scafolding.services;

import Ally.Scafolding.dtos.common.login.LoginDTO;
import Ally.Scafolding.dtos.common.login.LoginResponseDTO;

import org.springframework.stereotype.Service;



/**
 * Service interface for authentication operations.
 * <p>
 *     Handles user login and token generation.
 * </p>
 */
@Service
public interface AuthService {

    /**
     * Authenticates a user with provided credentials.
     * @param loginDTO the login credentials
     * @return LoginResponseDTO with token and user information if authentication is successful
     * @throws RuntimeException if authentication fails
     */
    LoginResponseDTO login(LoginDTO loginDTO);

    /**
     * Validates a JWT token.
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    boolean validateToken(String token);
}