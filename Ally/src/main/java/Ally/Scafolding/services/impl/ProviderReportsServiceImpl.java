package Ally.Scafolding.services.impl;

import Ally.Scafolding.dtos.common.provider.ProviderReportsDTO;
import Ally.Scafolding.repositories.PaymentsRepository;
import Ally.Scafolding.repositories.ServiceRepository;
import Ally.Scafolding.services.payment.ProviderReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderReportsServiceImpl implements ProviderReportsService {

    private final ServiceRepository serviceRepository;
    private final PaymentsRepository paymentsRepository;

    @Override
    public ProviderReportsDTO getReports(Long prestadorId, String periodo) {
        LocalDateTime desde = calcularDesde(periodo);

        long total = serviceRepository.countByPrestadorDesde(prestadorId, desde);

        long aceptadas = serviceRepository.countByPrestadorAndEstadoDesde(prestadorId, "ACEPTADO", desde);
        long rechazadas = serviceRepository.countByPrestadorAndEstadoDesde(prestadorId, "RECHAZADO", desde);

        // Pendientes: ajustá si tus estados pendientes son otros (PENDIENTE / SOLICITADO / etc)
        long pendientes = 0;
        pendientes += serviceRepository.countByPrestadorAndEstadoDesde(prestadorId, "PENDIENTE", desde);
        pendientes += serviceRepository.countByPrestadorAndEstadoDesde(prestadorId, "SOLICITADO", desde);

        BigDecimal ingresosTotales = paymentsRepository.totalIngresosPrestadorDesde(prestadorId, desde);
        if (ingresosTotales == null) ingresosTotales = BigDecimal.ZERO;

        // Servicios por especialidad (aceptados)
        List<Object[]> espRows = serviceRepository.countAceptadosPorEspecialidadPrestador(prestadorId, desde);
        List<ProviderReportsDTO.ReporteEspecialidadItem> especialidades = new ArrayList<>();
        for (Object[] row : espRows) {
            String especialidad = (String) row[0];
            long cantidad = ((Number) row[1]).longValue();
            especialidades.add(new ProviderReportsDTO.ReporteEspecialidadItem(especialidad, cantidad));
        }

        // Ingresos por mes (pagos completados)
        List<Object[]> mesRows = paymentsRepository.ingresosPorMesPrestador(prestadorId, desde);
        List<ProviderReportsDTO.ReporteIngresosMesItem> ingresosPorMes = new ArrayList<>();
        for (Object[] row : mesRows) {
            String mes = (String) row[0]; // "yyyy-MM"
            BigDecimal totalMes = (row[1] instanceof BigDecimal) ? (BigDecimal) row[1] : new BigDecimal(row[1].toString());
            ingresosPorMes.add(new ProviderReportsDTO.ReporteIngresosMesItem(mes, totalMes));
        }

        return new ProviderReportsDTO(
                total,
                aceptadas,
                rechazadas,
                pendientes,
                ingresosTotales,
                especialidades,
                ingresosPorMes
        );
    }

    private LocalDateTime calcularDesde(String periodo) {
        if (periodo == null) return LocalDateTime.now().minusMonths(6);

        return switch (periodo.toUpperCase()) {
            case "12M" -> LocalDateTime.now().minusMonths(12);
            case "ALL" -> null; // sin filtro por fecha
            default -> LocalDateTime.now().minusMonths(6);
        };
    }
}