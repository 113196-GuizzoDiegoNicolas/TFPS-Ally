package Ally.Scafolding.services.payment;



import Ally.Scafolding.dtos.common.patient.ReportResumenDTO;
import Ally.Scafolding.dtos.common.patient.SerieDTO;

import java.util.List;

public interface PatientReportsService {
    ReportResumenDTO getResumen(Long usuarioId, int months);
    List<SerieDTO> getServiciosPorEspecialidad(Long usuarioId, int months);
    List<SerieDTO> getServiciosPorEstado(Long usuarioId, int months);
    List<SerieDTO> getPagosPorMes(Long usuarioId, int months); // opcional
}