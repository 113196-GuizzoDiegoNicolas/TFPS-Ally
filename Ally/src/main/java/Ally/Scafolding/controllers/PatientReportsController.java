package Ally.Scafolding.controllers;

import Ally.Scafolding.dtos.common.patient.ReportResumenDTO;
import Ally.Scafolding.dtos.common.patient.SerieDTO;
import Ally.Scafolding.services.payment.PatientReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PatientReportsController {

    private final PatientReportsService reportsService;

    @GetMapping("/{usuarioId}/reportes/resumen")
    public ResponseEntity<ReportResumenDTO> resumen(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "6") int months
    ) {
        return ResponseEntity.ok(reportsService.getResumen(usuarioId, months));
    }

    @GetMapping("/{usuarioId}/reportes/servicios-por-especialidad")
    public ResponseEntity<List<SerieDTO>> serviciosPorEspecialidad(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "6") int months
    ) {
        return ResponseEntity.ok(reportsService.getServiciosPorEspecialidad(usuarioId, months));
    }

    @GetMapping("/{usuarioId}/reportes/servicios-por-estado")
    public ResponseEntity<List<SerieDTO>> serviciosPorEstado(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "6") int months
    ) {
        return ResponseEntity.ok(reportsService.getServiciosPorEstado(usuarioId, months));
    }

    // (Opcional) pagos por mes, si ya tenés PaymentRepository
    @GetMapping("/{usuarioId}/reportes/pagos-por-mes")
    public ResponseEntity<List<SerieDTO>> pagosPorMes(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "6") int months
    ) {
        return ResponseEntity.ok(reportsService.getPagosPorMes(usuarioId, months));
    }
}