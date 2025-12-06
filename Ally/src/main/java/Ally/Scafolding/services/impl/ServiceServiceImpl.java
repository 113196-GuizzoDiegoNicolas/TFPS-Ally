package Ally.Scafolding.services.impl;

import Ally.Scafolding.dtos.common.service.ServiceCreateDTO;
import Ally.Scafolding.dtos.common.service.ServiceDTO;
import Ally.Scafolding.entities.PatientsEntity;
import Ally.Scafolding.entities.ServiceEntity;
import Ally.Scafolding.repositories.PatientsRepository;
import Ally.Scafolding.repositories.ServiceRepository;
import Ally.Scafolding.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private ServiceRepository repository;
    @Autowired
    private PatientsRepository patientsRepository;

    @Override
    public ServiceDTO crear(ServiceCreateDTO dto) {
        // Busca el patient usando el usuarioId
        long usuarioId = dto.getPacienteId();
        PatientsEntity patientEntity = patientsRepository.findByUsersEntityId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró paciente para el usuario ID: " + usuarioId));
        ServiceEntity entity = new ServiceEntity();
        entity.setPacienteId(patientEntity.getId());
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
        // Busca el patient usando el usuarioId
        long usuarioId = pacienteId;
        PatientsEntity patientEntity = patientsRepository.findByUsersEntityId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró paciente para el usuario ID: " + usuarioId));
        long patientId = patientEntity.getId();
        return repository.findByPacienteId(patientId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<ServiceDTO> listarPorPacienteAceptadas(Long pacienteId) {
        // Busca el patient usando el usuarioId
        long usuarioId = pacienteId;
        PatientsEntity patientEntity = patientsRepository.findByUsersEntityId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró paciente para el usuario ID: " + usuarioId));
                long patientId = patientEntity.getId();
        return repository.findServiciosAceptadosPorPaciente(patientId, "ACEPTADO")
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
                entity.getFechaSolicitud().toString() // 👈 Esto genera ISO-8601 válido

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
