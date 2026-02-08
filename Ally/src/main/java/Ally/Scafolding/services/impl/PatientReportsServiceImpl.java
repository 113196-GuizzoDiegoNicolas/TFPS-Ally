package Ally.Scafolding.services.impl;

import Ally.Scafolding.dtos.common.exceptions.NotFoundException;
import Ally.Scafolding.dtos.common.patient.ReportResumenDTO;
import Ally.Scafolding.dtos.common.patient.SerieDTO;
import Ally.Scafolding.entities.PatientsEntity;
import Ally.Scafolding.repositories.PatientsRepository;
import Ally.Scafolding.repositories.PaymentsRepository;
import Ally.Scafolding.repositories.ServiceRepository;
import Ally.Scafolding.services.payment.PatientReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientReportsServiceImpl implements PatientReportsService {

    private final PatientsRepository patientsRepository;
    private final ServiceRepository serviceRepository;
    private final PaymentsRepository paymentsRepository;

    private Long resolvePatientId(Long usuarioId) {
        PatientsEntity patient = patientsRepository.findByUsersEntityId(usuarioId)
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado con usuarioId: " + usuarioId));
        return patient.getId();
    }

    private LocalDateTime desde(int months) {
        return LocalDateTime.now().minusMonths(months);
    }

    @Override
    public ReportResumenDTO getResumen(Long usuarioId, int months) {
        Long patientId = resolvePatientId(usuarioId);

        List<Object[]> estados = serviceRepository.countByEstadoPacienteDesde(patientId, desde(months));

        long aceptados = 0, pendientes = 0, rechazados = 0, totalServicios = 0;

        for (Object[] row : estados) {
            String estado = (String) row[0];
            long count = ((Number) row[1]).longValue();
            totalServicios += count;

            if ("ACEPTADO".equalsIgnoreCase(estado)) aceptados = count;
            else if ("PENDIENTE".equalsIgnoreCase(estado) || "PAGO_PENDIENTE".equalsIgnoreCase(estado)) pendientes += count;
            else if ("RECHAZADO".equalsIgnoreCase(estado)) rechazados = count;
        }

        // Recomendado: implementar totalPagadoDesde(...) en PaymentsRepository
        BigDecimal totalPagado = paymentsRepository.totalPagadoDesde(patientId, desde(months));

        return new ReportResumenDTO(totalServicios, aceptados, pendientes, rechazados, totalPagado);
    }

    @Override
    public List<SerieDTO> getServiciosPorEspecialidad(Long usuarioId, int months) {
        Long patientId = resolvePatientId(usuarioId);

        return serviceRepository.countByEspecialidadPacienteDesde(patientId, desde(months))
                .stream()
                .map(r -> new SerieDTO((String) r[0], BigDecimal.valueOf(((Number) r[1]).longValue())))
                .collect(Collectors.toList());
    }

    @Override
    public List<SerieDTO> getServiciosPorEstado(Long usuarioId, int months) {
        Long patientId = resolvePatientId(usuarioId);

        return serviceRepository.countByEstadoPacienteDesde(patientId, desde(months))
                .stream()
                .map(r -> new SerieDTO((String) r[0], BigDecimal.valueOf(((Number) r[1]).longValue())))
                .collect(Collectors.toList());
    }

    @Override
    public List<SerieDTO> getPagosPorMes(Long usuarioId, int months) {
        Long patientId = resolvePatientId(usuarioId);

        return paymentsRepository.pagosPorMesPaciente(patientId, desde(months))
                .stream()
                .map(r -> {
                    String mes = (String) r[0];
                    BigDecimal total = (r[1] instanceof BigDecimal bd)
                            ? bd
                            : BigDecimal.valueOf(((Number) r[1]).doubleValue());
                    return new SerieDTO(mes, total);
                })
                .collect(Collectors.toList());
    }
}