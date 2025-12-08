package Ally.Scafolding.services;

import Ally.Scafolding.dtos.common.admin.AdminMetricsDTO;
import Ally.Scafolding.dtos.common.admin.AdminUserDTO;
import Ally.Scafolding.dtos.common.admin.PagosEspecialidadDTO;

import java.util.List;

public interface AdminService {
    AdminMetricsDTO getMetrics();
    List<AdminUserDTO> getUsers();

    AdminUserDTO toggleUser(Long id);
    List<PagosEspecialidadDTO> getPagosPorEspecialidad();
}
