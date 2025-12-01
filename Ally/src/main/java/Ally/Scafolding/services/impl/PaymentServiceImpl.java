package Ally.Scafolding.services.impl;

import Ally.Scafolding.dtos.common.payment.PaymentRequestDTO;
import Ally.Scafolding.dtos.common.payment.PaymentResponseDTO;
import Ally.Scafolding.entities.PaymentsEntity;
import Ally.Scafolding.entities.ServiceEntity;
import Ally.Scafolding.entities.MetodosPagosEntity;
import Ally.Scafolding.models.PagoEstado;
import Ally.Scafolding.repositories.PaymentsRepository;
import Ally.Scafolding.repositories.ServiceRepository;
import Ally.Scafolding.repositories.MetodosPagosRepository;
import Ally.Scafolding.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentsRepository paymentsRepository;
    private final ServiceRepository serviceRepository;
    private final MetodosPagosRepository metodosPagosRepository;

    // MÉTODOS PRINCIPALES
    @Override
    @Transactional
    public PaymentResponseDTO procesarPago(PaymentRequestDTO paymentRequest) {
        ServiceEntity servicio = validarServicioYMonto(paymentRequest);
        MetodosPagosEntity metodoPago = validarMetodoPago(paymentRequest);
        PaymentsEntity pago = crearEntidadPago(paymentRequest, servicio, metodoPago);

        try {
            procesarPagoExterno(paymentRequest, pago);
            actualizarEstadoServicio(servicio, PagoEstado.COMPLETADO);
            paymentsRepository.save(pago);
            return mapToDTO(pago, "Pago procesado exitosamente");
        } catch (Exception e) {
            manejarErrorPago(pago, servicio, e.getMessage());
            throw new RuntimeException("Error al procesar el pago: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public PaymentResponseDTO reembolsarPago(Long pagoId) {
        PaymentsEntity pago = paymentsRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        if (pago.getEstado() != PagoEstado.COMPLETADO) {
            throw new RuntimeException("Solo se pueden reembolsar pagos completados");
        }

        pago.setEstado(PagoEstado.REEMBOLSADO);
        pago.getServicio().setEstadoPago(PagoEstado.REEMBOLSADO);

        paymentsRepository.save(pago);
        serviceRepository.save(pago.getServicio());

        return mapToDTO(pago, "Pago reembolsado exitosamente");
    }

    @Override
    @Transactional
    public PaymentResponseDTO cancelarPago(Long pagoId) {
        PaymentsEntity pago = paymentsRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        if (pago.getEstado() != PagoEstado.PROCESANDO && pago.getEstado() != PagoEstado.PENDIENTE) {
            throw new RuntimeException(
                    String.format("No se puede cancelar un pago en estado '%s'", pago.getEstado())
            );
        }

        pago.setEstado(PagoEstado.CANCELADO);
        pago.setMensajeError("Pago cancelado por el usuario");

        if (pago.getServicio().getEstadoPago() == PagoEstado.PROCESANDO) {
            pago.getServicio().setEstadoPago(PagoEstado.PENDIENTE);
            serviceRepository.save(pago.getServicio());
        }

        paymentsRepository.save(pago);
        return mapToDTO(pago, "Pago cancelado exitosamente");
    }

    // MÉTODOS DE CONSULTA
    @Override
    public PaymentResponseDTO getPagoById(Long pagoId) {
        PaymentsEntity pago = paymentsRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        return mapToDTO(pago, "Pago encontrado");
    }

    @Override
    public List<PaymentResponseDTO> getPagosAceptadosPorPaciente(Long pacienteId) {
        List<PaymentsEntity> pagos = paymentsRepository.findPagosAceptadosByPacienteId(pacienteId);
        return pagos.stream()
                .map(pago -> mapToDTO(pago, "Pago aceptado"))
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponseDTO> getPagosPorServicio(Long servicioId) {
        List<PaymentsEntity> pagos = paymentsRepository.findByServicioId(servicioId);
        return pagos.stream()
                .map(pago -> mapToDTO(pago, "Pago del servicio"))
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponseDTO> getPagosPorEstado(PagoEstado estado) {
        List<PaymentsEntity> pagos = paymentsRepository.findByEstado(estado);
        return pagos.stream()
                .map(pago -> mapToDTO(pago, "Pagos por estado: " + estado))
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponseDTO> getPagosPorPacienteYEstado(Long pacienteId, PagoEstado estado) {
        // Implementación usando @Query en el repository o filtrado manual
        List<PaymentsEntity> pagos = paymentsRepository.findByEstado(estado);
        return pagos.stream()
                .filter(pago -> pago.getServicio().getPacienteId().equals(pacienteId))
                .map(pago -> mapToDTO(pago, "Pago del paciente en estado: " + estado))
                .collect(Collectors.toList());
    }

    // MÉTODOS DE VALIDACIÓN
    @Override
    public Boolean validarMontoPago(Long servicioId, Double monto) {
        ServiceEntity servicio = serviceRepository.findById(servicioId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        return servicio.getPrecio().equals(monto);
    }

    @Override
    public Boolean servicioPuedeSerPagado(Long servicioId) {
        ServiceEntity servicio = serviceRepository.findById(servicioId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        return servicio.getEstadoPago() == PagoEstado.PENDIENTE &&
                esEstadoValidoParaPago(servicio.getEstado());
    }

    // MÉTODOS DE REPORTE
    @Override
    public Double getTotalPagadoPorPaciente(Long pacienteId) {
        List<PaymentsEntity> pagosAceptados = paymentsRepository.findPagosAceptadosByPacienteId(pacienteId);
        return pagosAceptados.stream()
                .mapToDouble(PaymentsEntity::getMonto)
                .sum();
    }

    @Override
    public Integer getCantidadPagosPorEstado(PagoEstado estado) {
        List<PaymentsEntity> pagos = paymentsRepository.findByEstado(estado);
        return pagos.size();
    }

    // MÉTODOS PRIVADOS (sin cambios)
    private ServiceEntity validarServicioYMonto(PaymentRequestDTO request) {
        ServiceEntity servicio = serviceRepository.findById(request.getServicioId())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        if (!servicio.getPrecio().equals(request.getMonto())) {
            throw new RuntimeException(
                    String.format("El monto enviado ($%.2f) no coincide con el precio del servicio ($%.2f)",
                            request.getMonto(), servicio.getPrecio())
            );
        }

        if (servicio.getEstadoPago() == PagoEstado.COMPLETADO) {
            throw new RuntimeException("El servicio ya ha sido pagado");
        }

        if (!esEstadoValidoParaPago(servicio.getEstado())) {
            throw new RuntimeException(
                    String.format("El servicio está en estado '%s'. No se puede pagar.", servicio.getEstado())
            );
        }

        return servicio;
    }

    private MetodosPagosEntity validarMetodoPago(PaymentRequestDTO request) {
        return metodosPagosRepository.findById(request.getMetodoPagoId())
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));
    }

    private PaymentsEntity crearEntidadPago(PaymentRequestDTO request, ServiceEntity servicio, MetodosPagosEntity metodoPago) {
        PaymentsEntity pago = new PaymentsEntity();
        pago.setServicio(servicio);
        pago.setMonto(request.getMonto());
        pago.setMetodoPago(metodoPago);
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado(PagoEstado.PROCESANDO);
        pago.setIdTransaccion(generarIdTransaccion());
        return pago;
    }

    private void actualizarEstadoServicio(ServiceEntity servicio, PagoEstado estado) {
        servicio.setEstadoPago(estado);
        serviceRepository.save(servicio);
    }

    private void manejarErrorPago(PaymentsEntity pago, ServiceEntity servicio, String mensajeError) {
        pago.setEstado(PagoEstado.FALLIDO);
        pago.setMensajeError(mensajeError);
        paymentsRepository.save(pago);

        servicio.setEstadoPago(PagoEstado.FALLIDO);
        serviceRepository.save(servicio);
    }

    private boolean esEstadoValidoParaPago(String estadoServicio) {
        return List.of("CONFIRMADO", "COMPLETADO", "EN_CURSO").contains(estadoServicio);
    }

    private void procesarPagoExterno(PaymentRequestDTO request, PaymentsEntity pago) {
        if (request.getMonto() <= 0) {
            throw new RuntimeException("Monto inválido");
        }

        if (request.getNumeroTarjeta() != null && !validarTarjeta(request.getNumeroTarjeta())) {
            throw new RuntimeException("Tarjeta inválida o rechazada");
        }

        // Simular procesamiento exitoso
        if (Math.random() > 0.1) {
            pago.setEstado(PagoEstado.COMPLETADO);
            pago.setFechaProcesamiento(LocalDateTime.now());
        } else {
            throw new RuntimeException("Pago rechazado por el procesador");
        }
    }

    private boolean validarTarjeta(String numeroTarjeta) {
        return numeroTarjeta != null &&
                numeroTarjeta.matches("\\d{16}") &&
                !numeroTarjeta.startsWith("0000");
    }

    private String generarIdTransaccion() {
        return "TX_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private PaymentResponseDTO mapToDTO(PaymentsEntity pago, String mensaje) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(pago.getId());
        dto.setServicioId(pago.getServicio().getId());
        dto.setMonto(pago.getMonto());
        dto.setEstado(pago.getEstado());
        dto.setFechaPago(pago.getFechaPago());
        dto.setIdTransaccion(pago.getIdTransaccion());
        dto.setMensaje(mensaje);
        dto.setMetodoPago(pago.getMetodoPago().getMetodoPago().name());
        return dto;
    }
}