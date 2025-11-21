package Ally.Scafolding.services;
import Ally.Scafolding.dtos.common.service.ServiceCreateDTO;
import Ally.Scafolding.dtos.common.service.ServiceDTO;
import Ally.Scafolding.entities.ServiceEntity;

import java.util.List;

public interface ServiceService {
    ServiceDTO crear(ServiceCreateDTO dto);
    List<ServiceDTO> listarPorPaciente(Long pacienteId);
    List<ServiceDTO> listarPorPrestador(Long prestadorId);
    ServiceDTO actualizarEstado(Long id, String nuevoEstado);
    List<ServiceDTO> listarSolicitudesTransportista();


}
