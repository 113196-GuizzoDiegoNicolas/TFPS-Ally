package Ally.Scafolding.controllers;

import Ally.Scafolding.dtos.common.login.LoginDTO;
import Ally.Scafolding.dtos.common.login.LoginResponseDTO;
import Ally.Scafolding.dtos.common.login.UserDTO;
import Ally.Scafolding.entities.UsersEntity;
import Ally.Scafolding.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request) {

        // Buscar por email
        UsersEntity user = usersRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }

        // Armar DTO de respuesta
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getUsuario(),
                user.getEmail(),
                user.getRol(),
                null, // no mandes password
                user.getActivo(),
                user.getBloqueado()
        );

        LoginResponseDTO response = new LoginResponseDTO(
                null, // token, si lo implementás más adelante
                userDTO,
                user.getRol()
        );

        return ResponseEntity.ok(response);
    }
}
