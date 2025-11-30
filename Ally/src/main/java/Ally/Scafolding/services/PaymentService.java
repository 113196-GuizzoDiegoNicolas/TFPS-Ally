package Ally.Scafolding.services;

import Ally.Scafolding.dtos.common.payment.PaymentRequestDTO;
import Ally.Scafolding.dtos.common.payment.PaymentResponseDTO;
import Ally.Scafolding.dtos.common.service.ServiceCreateDTO;
import Ally.Scafolding.dtos.common.service.ServiceDTO;
import Ally.Scafolding.entities.PaymentsEntity;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
Optional <PaymentResponseDTO> procesarPago(PaymentRequestDTO paymentRequest);
    List<PaymentResponseDTO> getPagosAceptadosPorPaciente(Long pacienteId);
    Optional <PaymentResponseDTO> getPagoById(Long pagoId);
    List<ServiceDTO> listarPorPacienteAceptadas(Long pacienteId);
    void procesarPagoExterno(PaymentRequestDTO request, PaymentsEntity pago);
}
