package Ally.Scafolding.services.impl;

import Ally.Scafolding.dtos.common.service.ServiceCreateDTO;
import Ally.Scafolding.dtos.common.service.ServiceDTO;
import Ally.Scafolding.entities.PatientsEntity;
import Ally.Scafolding.entities.ProvidersEntity;
import Ally.Scafolding.entities.ServiceEntity;
import Ally.Scafolding.entities.SpecialtyEntity;
import Ally.Scafolding.repositories.PatientsRepository;
import Ally.Scafolding.repositories.ProvidersRepository;
import Ally.Scafolding.repositories.ServiceRepository;
import Ally.Scafolding.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private ServiceRepository repository;

    @Autowired
    private ProvidersRepository repositoryProvider;

    @Autowired
    private PatientsRepository repositoryPatient;


    @Override
    public ServiceDTO crear(ServiceCreateDTO dto) {
        ProvidersEntity providerEntity = repositoryProvider.getById(dto.getPrestadorId());
        SpecialtyEntity espcialityEntity = providerEntity.getEspecialidad();
        Optional<PatientsEntity> patientEntity = repositoryPatient.findByUsersEntityId(dto.getPacienteId());

        ServiceEntity entity = new ServiceEntity();
        entity.setPacienteId(patientEntity.get().getId());
        entity.setPrestadorId(dto.getPrestadorId());
        entity.setTransportistaId(dto.getTransportistaId());
        entity.setEspecialidad(dto.getEspecialidad());
        entity.setDescripcion(dto.getDescripcion());
        entity.setEstado("PENDIENTE");
        entity.setMonto(espcialityEntity.getImporteConsulta());
        entity.setFechaSolicitud(LocalDateTime.now());
        ServiceEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Override
    public List<ServiceDTO> listarPorPaciente(Long pacienteId) {
        Optional<PatientsEntity> patientEntity = repositoryPatient.findByUsersEntityId(pacienteId);
        return repository.findByPacienteId(patientEntity.get().getId())
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<ServiceDTO> listarPorPacienteAceptadas(Long pacienteId) {
        Optional<PatientsEntity> patientEntity = repositoryPatient.findByUsersEntityId(pacienteId);
        return repository.findServiciosAceptadosPorPaciente(patientEntity.get().getId(), "ACEPTADO")
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

       /* // Si el prestador acepta → asignar monto
        if ("ACEPTADO".equals(nuevoEstado)) {
            BigDecimal monto = BigDecimal.valueOf(4000); // 👈 Default temporal
            entity.setMonto(monto);
        }*/

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
                entity.getMonto(),
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
