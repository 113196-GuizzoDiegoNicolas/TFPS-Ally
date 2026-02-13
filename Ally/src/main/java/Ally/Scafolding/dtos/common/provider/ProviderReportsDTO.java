package Ally.Scafolding.dtos.common.provider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderReportsDTO {

    private long totalSolicitudes;
    private long aceptadas;
    private long rechazadas;
    private long pendientes;
    private BigDecimal ingresosTotales;

    private List<ReporteEspecialidadItem> serviciosPorEspecialidad;
    private List<ReporteIngresosMesItem> ingresosPorMes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReporteEspecialidadItem {
        private String especialidad;
        private long cantidad;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReporteIngresosMesItem {
        private String mes;     // ej "2026-02" o "feb"
        private BigDecimal total;
    }
}