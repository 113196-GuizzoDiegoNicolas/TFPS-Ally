package Ally.Scafolding.services.impl;

import Ally.Scafolding.dtos.common.service.ServiceCreateDTO;
import Ally.Scafolding.dtos.common.service.ServiceDTO;
import Ally.Scafolding.entities.ServiceRequestEntity;
import Ally.Scafolding.repositories.ServiceRequestRepository;
import Ally.Scafolding.services.ServiceService;
import Ally.Scafolding.dtos.common.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRequestRepository repository;
    @Qualifier("modelMapper")
    private final ModelMapper mapper;

    @Override
    public ServiceDTO crear(ServiceCreateDTO dto) {
        ServiceRequestEntity entity = mapper.map(dto, ServiceRequestEntity.class);
        entity.setEstado("PENDIENTE");
        entity.setActivo(true);
        return mapper.map(repository.save(entity), ServiceDTO.class);
    }

    @Override
    public List<ServiceDTO> listarPorPaciente(Long pacienteId) {
        return repository.findByPaciente_IdAndActivoTrue(pacienteId)
                .stream().map(e -> mapper.map(e, ServiceDTO.class)).toList();
    }

    @Override
    public List<ServiceDTO> listarPorPrestador(Long prestadorId) {
        return repository.findByPrestador_Id(prestadorId)
                .stream().map(e -> mapper.map(e, ServiceDTO.class)).toList();
    }

    @Override
    public ServiceDTO actualizarEstado(Long id, String estado) {
        return repository.findById(id)
                .map(s -> {
                    s.setEstado(estado);
                    return mapper.map(repository.save(s), ServiceDTO.class);
                })
                .orElseThrow(() -> new NotFoundException("Servicio no encontrado"));
    }
}

