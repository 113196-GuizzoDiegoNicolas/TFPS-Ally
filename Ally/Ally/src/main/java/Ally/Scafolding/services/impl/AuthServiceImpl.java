package Ally.Scafolding.services.impl;

import Ally.Scafolding.dtos.common.login.LoginDTO;
import Ally.Scafolding.dtos.common.login.LoginResponseDTO;
import Ally.Scafolding.dtos.common.login.UserDTO;
import Ally.Scafolding.entities.UsersEntity;
import Ally.Scafolding.repositories.UsersRepository;
import Ally.Scafolding.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation of authentication service.
 * <p>
 *     Handles user authentication and token management.
 * </p>
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UsersRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Authenticates a user with provided credentials.
     * @param loginDTO the login credentials
     * @return LoginResponseDTO with token and user information
     * @throws RuntimeException if authentication fails
     */
    @Override
    public LoginResponseDTO login(LoginDTO loginDTO) {
        UsersEntity usuario = usuarioRepository.findByUsuario(loginDTO.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // Generar token (en un sistema real, usar JWT)
        String token = generateToken(usuario);

        UserDTO userDTO = new UserDTO(
                usuario.getId(),
              usuario.getUsuario(),
                usuario.getEmail(),
                usuario.getRol(),
                usuario.getPassword(),
                usuario.getActivo(),
                usuario.getBloqueado()
        );

        return new LoginResponseDTO(token, userDTO, usuario.getRol());
    }

    /**
     * Generates a token for the authenticated user.
     * @param usuario the authenticated user
     * @return generated token
     */
    private String generateToken(UsersEntity usuario) {
        // En un sistema real, usar JWT con expiración y firma
        return "token-" + usuario.getId() + "-" + UUID.randomUUID().toString();
    }

    /**
     * Validates a JWT token.
     * @param token the JWT token to validate
     * @return true if the token is valid
     */
    @Override
    public boolean validateToken(String token) {
        // En un sistema real, validar JWT con firma y expiración
        return token != null && token.startsWith("token-");
    }
}