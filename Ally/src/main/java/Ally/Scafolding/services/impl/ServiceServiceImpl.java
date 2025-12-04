package Ally.Scafolding.services.impl;

import Ally.Scafolding.dtos.common.service.ServiceCreateDTO;
import Ally.Scafolding.dtos.common.service.ServiceDTO;
import Ally.Scafolding.entities.ServiceEntity;
import Ally.Scafolding.repositories.ServiceRepository;
import Ally.Scafolding.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import java.util.stream.Collectors;

@Service
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private ServiceRepository repository;

    @Override
    public ServiceDTO crear(ServiceCreateDTO dto) {
        ServiceEntity entity = new ServiceEntity();
        entity.setPacienteId(dto.getPacienteId());
        entity.setPrestadorId(dto.getPrestadorId());
        entity.setTransportistaId(dto.getTransportistaId());
        entity.setEspecialidad(dto.getEspecialidad());
        entity.setDescripcion(dto.getDescripcion());
        entity.setEstado("PENDIENTE");
        entity.setFechaSolicitud(LocalDateTime.now());
        ServiceEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Override
    public List<ServiceDTO> listarPorPaciente(Long pacienteId) {
        return repository.findByPacienteId(pacienteId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<ServiceDTO> listarPorPacienteAceptadas(Long pacienteId) {
        return repository.findServiciosAceptadosPorPaciente(pacienteId, "ACEPTADO")
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<ServiceDTO> listarPorPrestador(Long prestadorId) {
        return repository.findByPrestadorId(prestadorId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public ServiceDTO actualizarEstado(Long id, String nuevoEstado) {
        ServiceEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado"));
        entity.setEstado(nuevoEstado);
        return mapToDTO(repository.save(entity));
    }

    private ServiceDTO mapToDTO(ServiceEntity entity) {
        return new ServiceDTO(
                entity.getId(),
                entity.getPacienteId(),
                entity.getPrestadorId(),
                entity.getEspecialidad(),
                entity.getDescripcion(),
                entity.getEstado(),
                entity.getFechaSolicitud()

        );
    }

    @Override
    public List<ServiceDTO> listarSolicitudesTransportista() {
        return repository.findByEspecialidad("TRANSPORTE_SANITARIO")
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }



}
