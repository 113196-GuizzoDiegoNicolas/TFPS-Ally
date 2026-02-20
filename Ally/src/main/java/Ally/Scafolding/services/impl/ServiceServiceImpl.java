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
    @Autowired
    private ServiceRepository serviceRepository;


    @Override
    public ServiceDTO crear(ServiceCreateDTO dto) {

        // 1) Paciente: vos mandás el ID del usuario logueado (userId)
        PatientsEntity patientEntity = repositoryPatient.findByUsersEntityId(dto.getPacienteId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Paciente no encontrado para usuarioId: " + dto.getPacienteId()
                ));

        boolean esPrestador = dto.getPrestadorId() != null;
        boolean esTransportista = dto.getTransportistaId() != null;

        // 2) Validación mínima
        if (!esPrestador && !esTransportista) {
            throw new IllegalArgumentException("Debe enviar prestadorId o transportistaId");
        }

        ServiceEntity entity = new ServiceEntity();
        entity.setPacienteId(patientEntity.getId());
        entity.setEspecialidad(dto.getEspecialidad());
        entity.setDescripcion(dto.getDescripcion());
        entity.setEstado("PENDIENTE");
        entity.setFechaSolicitud(LocalDateTime.now());

        // 3) PRESTADOR
        if (esPrestador) {
            ProvidersEntity providerEntity = repositoryProvider.findById(dto.getPrestadorId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Prestador no encontrado con id: " + dto.getPrestadorId()
                    ));

            SpecialtyEntity specialty = providerEntity.getEspecialidad();
            if (specialty == null) {
                throw new IllegalArgumentException("El prestador no tiene especialidad asignada");
            }

            entity.setPrestadorId(dto.getPrestadorId());
            entity.setTransportistaId(null);
            entity.setMonto(specialty.getImporteConsulta()); // ✅ monto desde especialidad
        }

        // 4) TRANSPORTISTA
        if (esTransportista) {
            entity.setPrestadorId(null);
            entity.setTransportistaId(dto.getTransportistaId());

            // Definí el monto como quieras:
            // - si todavía no manejás monto transporte -> 0
            // - o si viene desde el front -> dto.getMontoApagar()
            entity.setMonto(dto.getMontoApagar() != null ? dto.getMontoApagar() : BigDecimal.ZERO);
        }

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
                entity.getTransportistaId(),   // ✅ NUEVO
                entity.getEspecialidad(),
                entity.getDescripcion(),
                entity.getEstado(),
                entity.getMonto(),
                entity.getFechaSolicitud().toString()
        );
    }

    @Override
    public List<ServiceDTO> listarSolicitudesTransportista() {
        return repository.findByEspecialidad("TRANSPORTE_SANITARIO")
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<ServiceEntity> findByEstado(String estado) {
        return serviceRepository.findByEstado(estado);
    }


}
