package Ally.Scafolding.dtos.common.admin;

public class AdminMetricsDTO {
    private long pacientes;
    private long prestadores;
    private long transportistas;
    private long admins;

    public AdminMetricsDTO(long pacientes, long prestadores, long transportistas, long admins) {
        this.pacientes = pacientes;
        this.prestadores = prestadores;
        this.transportistas = transportistas;
        this.admins = admins;
    }

    // Getters & Setters

}
