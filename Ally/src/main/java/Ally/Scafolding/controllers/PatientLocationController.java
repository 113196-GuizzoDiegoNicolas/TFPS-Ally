package Ally.Scafolding.controllers;

import Ally.Scafolding.dtos.common.location.PatientLocationDTO;
import Ally.Scafolding.services.PatientLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PatientLocationController {

    private final PatientLocationService service;

    // GET /api/pacientes/{id}/ubicacion
    @GetMapping("/{pacienteId}/ubicacion")
    public ResponseEntity<PatientLocationDTO> getLast(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(service.getLastLocation(pacienteId));
    }

    // POST /api/pacientes/{id}/ubicacion
    @PostMapping("/{pacienteId}/ubicacion")
    public ResponseEntity<PatientLocationDTO> save(@PathVariable Long pacienteId,
                                                   @RequestBody PatientLocationDTO dto) {
        return ResponseEntity.ok(service.saveLocation(pacienteId, dto));
    }

}
