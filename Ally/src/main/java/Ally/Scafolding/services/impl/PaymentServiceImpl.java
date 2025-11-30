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

    @Override
    @Transactional
    public PaymentResponseDTO procesarPago(PaymentRequestDTO paymentRequest) {
        // 1. VALIDAR SERVICIO Y MONTO
        ServiceEntity servicio = validarServicioYMonto(paymentRequest);

        // 2. VALIDAR MÉTODO DE PAGO
        MetodosPagosEntity metodoPago = validarMetodoPago(paymentRequest);

        // 3. CREAR ENTIDAD DE PAGO
        PaymentsEntity pago = crearEntidadPago(paymentRequest, servicio, metodoPago);

        try {
            // 4. PROCESAR PAGO EXTERNO
            procesarPagoExterno(paymentRequest, pago);

            // 5. ACTUALIZAR ESTADO DEL SERVICIO
            actualizarEstadoServicio(servicio, PagoEstado.COMPLETADO);

            // 6. GUARDAR PAGO
            paymentsRepository.save(pago);

            return mapToDTO(pago, "Pago procesado exitosamente");

        } catch (Exception e) {
            // 7. MANEJAR ERROR - Actualizar ambos
            manejarErrorPago(pago, servicio, e.getMessage());
            throw new RuntimeException("Error al procesar el pago: " + e.getMessage());
        }
    }

    /**
     * Valida que el servicio exista y que el monto coincida
     */
    private ServiceEntity validarServicioYMonto(PaymentRequestDTO request) {
        ServiceEntity servicio = serviceRepository.findById(request.getServicioId())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        // ✅ VERIFICAR QUE EL MONTO COINCIDE CON EL PRECIO DEL SERVICIO
        if (!servicio.getPrecio().equals(request.getMonto())) {
            throw new RuntimeException(
                    String.format("El monto enviado ($%.2f) no coincide con el precio del servicio ($%.2f)",
                            request.getMonto(), servicio.getPrecio())
            );
        }

        // ✅ VERIFICAR QUE EL SERVICIO NO ESTÉ PAGADO
        if (servicio.getEstadoPago() == PagoEstado.COMPLETADO) {
            throw new RuntimeException("El servicio ya ha sido pagado");
        }

        // ✅ VERIFICAR QUE EL SERVICIO ESTÉ EN ESTADO VÁLIDO PARA PAGO
        if (!esEstadoValidoParaPago(servicio.getEstado())) {
            throw new RuntimeException(
                    String.format("El servicio está en estado '%s'. No se puede pagar.", servicio.getEstado())
            );
        }

        return servicio;
    }

    /**
     * Valida que el método de pago exista y esté activo
     */
    private MetodosPagosEntity validarMetodoPago(PaymentRequestDTO request) {
        return metodosPagosRepository.findById(request.getMetodoPagoId())
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));
    }

    /**
     * Crea la entidad de pago con estado inicial
     */
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

    /**
     * Actualiza el estado del servicio después del pago exitoso
     */
    private void actualizarEstadoServicio(ServiceEntity servicio, PagoEstado estado) {
        servicio.setEstadoPago(estado);
        serviceRepository.save(servicio);
    }

    /**
     * Maneja el error actualizando tanto el pago como el servicio
     */
    private void manejarErrorPago(PaymentsEntity pago, ServiceEntity servicio, String mensajeError) {
        pago.setEstado(PagoEstado.FALLIDO);
        pago.setMensajeError(mensajeError);
        paymentsRepository.save(pago);

        // También actualizar el servicio como fallido
        servicio.setEstadoPago(PagoEstado.FALLIDO);
        serviceRepository.save(servicio);
    }

    /**
     * Define qué estados de servicio permiten pago
     */
    private boolean esEstadoValidoParaPago(String estadoServicio) {
        return List.of("CONFIRMADO", "COMPLETADO", "EN_CURSO").contains(estadoServicio);
    }

    @Override
    public List<PaymentResponseDTO> getPagosAceptadosPorPaciente(Long pacienteId) {
        List<PaymentsEntity> pagos = paymentsRepository.findPagosAceptadosByPacienteId(pacienteId);
        return pagos.stream()
                .map(pago -> mapToDTO(pago, "Pago aceptado"))
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponseDTO getPagoById(Long pagoId) {
        PaymentsEntity pago = paymentsRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        return mapToDTO(pago, "Pago encontrado");
    }

    /**
     * Simula el procesamiento con gateway de pago externo
     */
    @Override
    public void procesarPagoExterno(PaymentRequestDTO request, PaymentsEntity pago) {
        // Simular lógica de gateway de pago
        // En producción, aquí integrarías con MercadoPago, Stripe, etc.

        if (request.getMonto() <= 0) {
            throw new RuntimeException("Monto inválido");
        }

        // Simular validación de tarjeta (si aplica)
        if (request.getNumeroTarjeta() != null && !validarTarjeta(request.getNumeroTarjeta())) {
            throw new RuntimeException("Tarjeta inválida o rechazada");
        }

        // Simular procesamiento exitoso (90% éxito, 10% fallo para testing)
        if (Math.random() > 0.1) {
            pago.setEstado(PagoEstado.COMPLETADO);
            pago.setFechaProcesamiento(LocalDateTime.now());
        } else {
            throw new RuntimeException("Pago rechazado por el procesador");
        }
    }

    /**
     * Valida formato básico de tarjeta (simulado)
     */
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

    // Métodos adicionales útiles
    @Override
    public List<PaymentResponseDTO> getPagosPorServicio(Long servicioId) {
        List<PaymentsEntity> pagos = paymentsRepository.findByServicioId(servicioId);
        return pagos.stream()
                .map(pago -> mapToDTO(pago, "Pago del servicio"))
                .collect(Collectors.toList());
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
}