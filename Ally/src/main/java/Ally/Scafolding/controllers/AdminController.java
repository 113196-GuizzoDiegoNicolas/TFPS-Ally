package Ally.Scafolding.controllers;

import Ally.Scafolding.dtos.common.admin.AdminMetricsDTO;
import Ally.Scafolding.dtos.common.admin.AdminUserDTO;
import Ally.Scafolding.services.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") //  Angular corre en http://localhost:4200
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/metrics")
    public ResponseEntity<AdminMetricsDTO> getMetrics() {
        log.info("Solicitando métricas del sistema");
        return ResponseEntity.ok(adminService.getMetrics());
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserDTO>> getUsers() {
        log.info("Solicitando listado de usuarios");
        return ResponseEntity.ok(adminService.getUsers());
    }

    @PatchMapping("/users/{id}/toggle")
    public ResponseEntity<AdminUserDTO> toggleUser(@PathVariable Long id) {
        log.warn("Cambiando estado del usuario con id {}", id);
        AdminUserDTO updatedUser = adminService.toggleUser(id);
        return ResponseEntity.ok(updatedUser);
    }
}

