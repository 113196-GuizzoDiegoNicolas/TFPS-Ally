package Ally.Scafolding.services.impl;

import Ally.Scafolding.dtos.common.login.UserLoginDTO;
import Ally.Scafolding.dtos.common.login.LoginResponseDTO;
import Ally.Scafolding.dtos.common.login.UserDTO;
import Ally.Scafolding.entities.UsersEntity;
import Ally.Scafolding.repositories.UsersRepository;
import Ally.Scafolding.services.AuthService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDTO login(UserLoginDTO userLoginDTO) {
        UsersEntity usuario = usuarioRepository.findByEmail(userLoginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        if (!passwordEncoder.matches(userLoginDTO.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = generateToken(usuario);

        UserDTO userDTO = UserDTO.builder()
                .id(usuario.getId())
                .username(usuario.getUsuario())
                .email(usuario.getEmail())
                .role(usuario.getRol())
                .active(usuario.getActivo())
                .locked(usuario.getBloqueado())
                .build();

        return new LoginResponseDTO(token, userDTO, usuario.getRol());
    }


    private String generateToken(UsersEntity usuario) {
        return "token-" + usuario.getId() + "-" + UUID.randomUUID();
    }

    @Override
    public boolean validateToken(String token) {
        return token != null && token.startsWith("token-");
    }
}
