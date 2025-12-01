package Ally.Scafolding.services;

import Ally.Scafolding.dtos.common.payment.PaymentRequestDTO;
import Ally.Scafolding.dtos.common.payment.PaymentResponseDTO;
import Ally.Scafolding.dtos.common.service.ServiceCreateDTO;
import Ally.Scafolding.dtos.common.service.ServiceDTO;
import Ally.Scafolding.entities.PaymentsEntity;
import Ally.Scafolding.models.PagoEstado;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    // Métodos principales de procesamiento
    PaymentResponseDTO procesarPago(PaymentRequestDTO paymentRequest);
    PaymentResponseDTO reembolsarPago(Long pagoId);
    PaymentResponseDTO cancelarPago(Long pagoId);

    // Métodos de consulta
    PaymentResponseDTO getPagoById(Long pagoId);
    List<PaymentResponseDTO> getPagosAceptadosPorPaciente(Long pacienteId);
    List<PaymentResponseDTO> getPagosPorServicio(Long servicioId);
    List<PaymentResponseDTO> getPagosPorEstado(PagoEstado estado);
    List<PaymentResponseDTO> getPagosPorPacienteYEstado(Long pacienteId, PagoEstado estado);

    // Métodos de validación
    Boolean validarMontoPago(Long servicioId, Double monto);
    Boolean servicioPuedeSerPagado(Long servicioId);

    // Métodos de reporte
    Double getTotalPagadoPorPaciente(Long pacienteId);
    Integer getCantidadPagosPorEstado(PagoEstado estado);
}
