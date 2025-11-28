package Ally.Scafolding.controllers;

import Ally.Scafolding.dtos.common.service.ServiceCreateDTO;
import Ally.Scafolding.dtos.common.service.ServiceDTO;
import Ally.Scafolding.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestaciones")
@CrossOrigin(origins = "*")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @PostMapping
    public ResponseEntity<ServiceDTO> crear(@RequestBody ServiceCreateDTO dto) {
        return ResponseEntity.ok(serviceService.crear(dto));
    }

    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<ServiceDTO>> listarPorPaciente(@PathVariable Long idPaciente) {
        return ResponseEntity.ok(serviceService.listarPorPaciente(idPaciente));
    }

    @GetMapping("/paciente-aceptadas/{idPaciente}")
    public ResponseEntity<List<ServiceDTO>> listarPorPacienteAceptadas(@PathVariable Long idPaciente) {
        return ResponseEntity.ok(serviceService.listarPorPacienteAceptadas(idPaciente));
    }
    @GetMapping("/prestador/{idPrestador}")
    public ResponseEntity<List<ServiceDTO>> listarPorPrestador(@PathVariable Long idPrestador) {
        return ResponseEntity.ok(serviceService.listarPorPrestador(idPrestador));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ServiceDTO> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return ResponseEntity.ok(serviceService.actualizarEstado(id, estado));
    }

    @GetMapping("/transportista")
    public ResponseEntity<List<ServiceDTO>> listarParaTransportista() {
        return ResponseEntity.ok(serviceService.listarSolicitudesTransportista());
    }
    @PutMapping("/{id}/aceptar")
    public ResponseEntity<ServiceDTO> aceptar(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.actualizarEstado(id, "ACEPTADO"));
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<ServiceDTO> rechazar(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.actualizarEstado(id, "RECHAZADO"));
    }






}
