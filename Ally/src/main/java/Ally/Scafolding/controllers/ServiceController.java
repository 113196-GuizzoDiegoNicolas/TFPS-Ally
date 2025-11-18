package Ally.Scafolding.controllers;

import Ally.Scafolding.dtos.common.service.ServiceCreateDTO;
import Ally.Scafolding.dtos.common.service.ServiceDTO;
import Ally.Scafolding.services.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Unificado: Controlador para solicitudes de prestación de servicios.
 * Aquí se gestionan:
 *  - Crear solicitudes
 *  - Listar solicitudes por paciente o prestador
 *  - Actualizar estado (ACEPTADO / RECHAZADO / CANCELADO / etc.)
 */
@RestController
@RequestMapping("/api/prestaciones")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    /**
     * Crear una solicitud de servicio (siempre comienza con estado PENDIENTE)
     */
    @PostMapping
    public ResponseEntity<ServiceDTO> crear(@RequestBody ServiceCreateDTO dto) {
        return ResponseEntity.ok(serviceService.crear(dto));
    }

    /**
     * Obtener solicitudes vinculadas a un paciente
     */
    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<ServiceDTO>> listarPorPaciente(@PathVariable Long idPaciente) {
        return ResponseEntity.ok(serviceService.listarPorPaciente(idPaciente));
    }

    /**
     * Obtener solicitudes destinadas a un prestador
     */
    @GetMapping("/prestador/{idPrestador}")
    public ResponseEntity<List<ServiceDTO>> listarPorPrestador(@PathVariable Long idPrestador) {
        return ResponseEntity.ok(serviceService.listarPorPrestador(idPrestador));
    }

    /**
     * Actualizar estado de la solicitud:
     * ACEPATADO | RECHAZADO | CANCELADO | FINALIZADO | etc.
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ServiceDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado
    ) {
        return ResponseEntity.ok(serviceService.actualizarEstado(id, estado));
    }

    /**
     * Acciones específicas (alternativa más amigable para front):
     */

    @PutMapping("/{id}/aceptar")
    public ResponseEntity<ServiceDTO> aceptar(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.actualizarEstado(id, "ACEPTADO"));
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<ServiceDTO> rechazar(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.actualizarEstado(id, "RECHAZADO"));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ServiceDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.actualizarEstado(id, "CANCELADO"));
    }
}
