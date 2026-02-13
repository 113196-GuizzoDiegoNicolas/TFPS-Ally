package Ally.Scafolding.services.impl;

import Ally.Scafolding.entities.PasswordResetTokenEntity;
import Ally.Scafolding.repositories.PasswordResetTokenRepository;
import Ally.Scafolding.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final UsersRepository userRepo;
    private final PasswordResetTokenRepository tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final String frontUrl;

    public PasswordResetService(
            UsersRepository userRepo,
            PasswordResetTokenRepository tokenRepo,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            @Value("${app.front-url}") String frontUrl
    ) {
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.frontUrl = frontUrl;
    }

    public void forgotPassword(String email) {
        var userOpt = userRepo.findByEmailIgnoreCase(email);

        // Seguridad: responder OK siempre
        if (userOpt.isEmpty()) return;

        var user = userOpt.get();

        String token = UUID.randomUUID().toString();

        PasswordResetTokenEntity prt = new PasswordResetTokenEntity();
        prt.setToken(token);
        prt.setUser(user);
        prt.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        prt.setUsed(false);

        tokenRepo.save(prt);

        String link = frontUrl + "/reset-password?token=" + token;

        emailService.send(
                user.getEmail(),
                "Recuperación de contraseña - Ally",
                "Hola " + user.getUsuario() + ",\n\n" +
                        "Para restablecer tu contraseña ingresá al siguiente enlace:\n\n" +
                        link + "\n\n" +
                        "Este enlace vence en 15 minutos.\n\n" +
                        "Si vos no solicitaste esto, ignorá este correo."
        );
    }

    public void resetPassword(String token, String newPassword) {
        var prt = tokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (Boolean.TRUE.equals(prt.getUsed()) || prt.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token vencido o ya usado");
        }

        var user = prt.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        prt.setUsed(true);
        tokenRepo.save(prt);
    }
}