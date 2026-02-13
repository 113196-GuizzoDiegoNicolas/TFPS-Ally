package Ally.Scafolding.controllers;

import Ally.Scafolding.dtos.common.admin.AdminMetricsDTO;
import Ally.Scafolding.dtos.common.admin.AdminUserDTO;
import Ally.Scafolding.dtos.common.admin.PagosEspecialidadDTO;
import Ally.Scafolding.entities.PaymentsEntity;
import Ally.Scafolding.entities.ServiceEntity;
import Ally.Scafolding.repositories.PaymentsRepository;
import Ally.Scafolding.repositories.ServiceRepository;
import Ally.Scafolding.services.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private PaymentsRepository paymentsRepository;
    @GetMapping("/metrics")
    public ResponseEntity<AdminMetricsDTO> getMetrics(@RequestParam(required = false) String fechaDesde,
                                                      @RequestParam(required = false) String fechaHasta) {
        log.info("Solicitando métricas del sistema");
        return ResponseEntity.ok(adminService.getMetrics(fechaDesde, fechaHasta));
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
    @GetMapping("/pagos-por-especialidad")
    public ResponseEntity<List<PagosEspecialidadDTO>> pagosPorEspecialidad() {
        return ResponseEntity.ok(adminService.getPagosPorEspecialidad());
    }
    @GetMapping("/services")
    public ResponseEntity<List<ServiceEntity>> getServicesByEstado(@RequestParam String estado) {
        return ResponseEntity.ok(serviceRepository.findByEstado(estado));
    }


    @GetMapping("/pagos")
    public ResponseEntity<List<PaymentsEntity>> getPagos() {
        return ResponseEntity.ok(paymentsRepository.findAll());
    }


    //  Solicitudes Detalladas
    @GetMapping("/services/detalle/pendientes")
    public ResponseEntity<List<Object[]>> getPendientesDetalle() {
        return ResponseEntity.ok(adminService.getSolicitudesPendientesDetalle());
    }

    @GetMapping("/services/detalle/aceptados")
    public ResponseEntity<List<Object[]>> getAceptadosDetalle() {
        return ResponseEntity.ok(adminService.getServiciosAceptadosDetalle());
    }

    //  Cambiar estado del servicio
    @PatchMapping("/services/{id}/estado")
    public ResponseEntity<Void> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {

        serviceRepository.updateEstadoServicio(id, estado);
        return ResponseEntity.ok().build();
    }


}

