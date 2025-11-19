package Ally.Scafolding.controllers;

import Ally.Scafolding.dtos.common.login.UserLoginDTO;
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
    public ResponseEntity<?> login(@RequestBody UserLoginDTO request) {

        UsersEntity user = usersRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }

        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .username(user.getUsuario())
                .email(user.getEmail())
                .role(user.getRol())
                .active(user.getActivo())
                .locked(user.getBloqueado())
                .build();

        LoginResponseDTO response = new LoginResponseDTO(
                null, // token cuando se implemente JWT
                userDTO,
                user.getRol()
        );

        return ResponseEntity.ok(response);
    }

}
