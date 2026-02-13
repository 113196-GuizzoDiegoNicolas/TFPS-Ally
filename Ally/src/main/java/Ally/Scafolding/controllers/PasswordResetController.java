package Ally.Scafolding.controllers;

import Ally.Scafolding.dtos.common.login.ForgotPasswordRequest;
import Ally.Scafolding.dtos.common.login.ResetPasswordRequest;
import Ally.Scafolding.services.impl.PasswordResetService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {
    private final PasswordResetService service;

    public PasswordResetController(PasswordResetService service) {
        this.service = service;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody ForgotPasswordRequest req) {
        if (req.getEmail() == null || req.getEmail().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        service.forgotPassword(req.getEmail().trim());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest req) {
        service.resetPassword(req.token(), req.newPassword());
        return ResponseEntity.ok().build();
    }
}