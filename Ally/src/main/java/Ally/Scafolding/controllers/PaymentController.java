package Ally.Scafolding.controllers;

import Ally.Scafolding.dtos.common.payment.PaymentRequestDTO;
import Ally.Scafolding.dtos.common.payment.PaymentResponseDTO;
import Ally.Scafolding.models.PagoEstado;
import Ally.Scafolding.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Procesar un nuevo pago
     */
    @PostMapping("/procesar")
    public ResponseEntity<PaymentResponseDTO> procesarPago(@RequestBody PaymentRequestDTO paymentRequest) {
        return ResponseEntity.ok(paymentService.procesarPago(paymentRequest));
    }

    /**
     * Reembolsar un pago completado
     */
    @PostMapping("/{pagoId}/reembolsar")
    public ResponseEntity<PaymentResponseDTO> reembolsarPago(@PathVariable Long pagoId) {
        return ResponseEntity.ok(paymentService.reembolsarPago(pagoId));
    }

    /**
     * Cancelar un pago en procesamiento o pendiente
     */
    @PostMapping("/{pagoId}/cancelar")
    public ResponseEntity<PaymentResponseDTO> cancelarPago(@PathVariable Long pagoId) {
        return ResponseEntity.ok(paymentService.cancelarPago(pagoId));
    }

    /**
     * Obtener un pago por su ID
     */
    @GetMapping("/{pagoId}")
    public ResponseEntity<PaymentResponseDTO> getPagoById(@PathVariable Long pagoId) {
        return ResponseEntity.ok(paymentService.getPagoById(pagoId));
    }

    /**
     * Obtener pagos aceptados/completados de un paciente
     */
    @GetMapping("/paciente/{pacienteId}/aceptados")
    public ResponseEntity<List<PaymentResponseDTO>> getPagosAceptadosPorPaciente(
            @PathVariable Long pacienteId) {
        return ResponseEntity.ok(paymentService.getPagosAceptadosPorPaciente(pacienteId));
    }

    /**
     * Obtener todos los pagos de un servicio específico
     */
    @GetMapping("/servicio/{servicioId}")
    public ResponseEntity<List<PaymentResponseDTO>> getPagosPorServicio(
            @PathVariable Long servicioId) {
        return ResponseEntity.ok(paymentService.getPagosPorServicio(servicioId));
    }

    /**
     * Obtener pagos por estado
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PaymentResponseDTO>> getPagosPorEstado(
            @PathVariable PagoEstado estado) {
        return ResponseEntity.ok(paymentService.getPagosPorEstado(estado));
    }

    /**
     * Obtener pagos de un paciente por estado específico
     */
    @GetMapping("/paciente/{pacienteId}/estado/{estado}")
    public ResponseEntity<List<PaymentResponseDTO>> getPagosPorPacienteYEstado(
            @PathVariable Long pacienteId,
            @PathVariable PagoEstado estado) {
        return ResponseEntity.ok(paymentService.getPagosPorPacienteYEstado(pacienteId, estado));
    }

    /**
     * Validar si un monto coincide con el precio del servicio
     */
    @GetMapping("/validar-monto")
    public ResponseEntity<Boolean> validarMontoPago(
            @RequestParam Long servicioId,
            @RequestParam Double monto) {
        return ResponseEntity.ok(paymentService.validarMontoPago(servicioId, monto));
    }

    /**
     * Verificar si un servicio puede ser pagado
     */
    @GetMapping("/servicio/{servicioId}/puede-pagarse")
    public ResponseEntity<Boolean> servicioPuedeSerPagado(@PathVariable Long servicioId) {
        return ResponseEntity.ok(paymentService.servicioPuedeSerPagado(servicioId));
    }

    /**
     * Obtener el total pagado por un paciente
     */
    @GetMapping("/paciente/{pacienteId}/total")
    public ResponseEntity<Double> getTotalPagadoPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(paymentService.getTotalPagadoPorPaciente(pacienteId));
    }

    /**
     * Obtener cantidad de pagos por estado
     */
    @GetMapping("/estado/{estado}/cantidad")
    public ResponseEntity<Integer> getCantidadPagosPorEstado(@PathVariable PagoEstado estado) {
        return ResponseEntity.ok(paymentService.getCantidadPagosPorEstado(estado));
    }
}