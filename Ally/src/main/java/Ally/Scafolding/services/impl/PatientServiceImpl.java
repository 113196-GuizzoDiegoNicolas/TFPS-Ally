package Ally.Scafolding.services.impl;

import Ally.Scafolding.dtos.common.patient.UpdatePatientDTO;
import Ally.Scafolding.models.Patient;
import Ally.Scafolding.entities.PatientsEntity;
import Ally.Scafolding.repositories.PatientsRepository;
import Ally.Scafolding.repositories.UsersRepository;
import Ally.Scafolding.services.PatientService;
import Ally.Scafolding.dtos.common.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of PatientService for managing patients.
 */
@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientsRepository patientsRepository;

    @Autowired
    private ModelMapper modelMapper;
    // cambio indi -con front
    @Autowired
    private UsersRepository usersRepository; // Asegurate de tenerlo
    /**
     * Creates a new patient.
     */
    @Override
    public Patient createPatient(Patient patient) {
        PatientsEntity entity = modelMapper.map(patient, PatientsEntity.class);

        // 🔹 Asociar el usuario (ya que el front envía idUsuario)
        if (patient.getIdUsuario() != null) {
            entity.setUsersEntity(usersRepository.findById(patient.getIdUsuario())
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado con id: " + patient.getIdUsuario())));
        } else {
            throw new IllegalArgumentException("El campo idUsuario es obligatorio para crear un paciente.");
        }

        PatientsEntity saved = patientsRepository.save(entity);
        return modelMapper.map(saved, Patient.class);
    }

    /**
     * Retrieves a patient by its unique identifier.
     */
    @Override
    public Patient getPatientById(Long id) {
        Optional<PatientsEntity> entity = patientsRepository.findById(id);
        if (entity.isEmpty()) {
            throw new NotFoundException("Patient not found with id: " + id);
        }
        return modelMapper.map(entity.get(), Patient.class);
    }

    /**
     * Retrieves all patients.
     */
    @Override
    public List<Patient> getAllPatients() {
        List<PatientsEntity> entities = patientsRepository.findAll();
        return entities.stream()
                .map(e -> modelMapper.map(e, Patient.class))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a paginated list of patients.
     */
    @Override
    public Page<Patient> getPatients(Pageable pageable) {
        Page<PatientsEntity> page = patientsRepository.findAll(pageable);
        List<Patient> patients = page.getContent().stream()
                .map(e -> modelMapper.map(e, Patient.class))
                .collect(Collectors.toList());
        return new PageImpl<>(patients, pageable, page.getTotalElements());
    }

    /**
     * Updates an existing patient.
     */
    @Override
    public Patient updatePatient(Long id, Patient patient) {
        Optional<PatientsEntity> entityOpt = patientsRepository.findById(id);
        if (entityOpt.isEmpty()) {
            throw new NotFoundException("Patient not found with id: " + id);
        }
        PatientsEntity entity = entityOpt.get();
        modelMapper.map(patient, entity);
        PatientsEntity updated = patientsRepository.save(entity);
        return modelMapper.map(updated, Patient.class);
    }

    /**
     * Deletes a patient by its unique identifier.
     */
    @Override
    public boolean deletePatient(Long id) {
        if (!patientsRepository.existsById(id)) {
            throw new NotFoundException("Patient not found with id: " + id);
        }
        patientsRepository.deleteById(id);
        return true;
    }
    @Override
    public Patient getPatientByUsuarioId(Long usuarioId) {
        PatientsEntity entity = patientsRepository.findByUsersEntityId(usuarioId)
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado con usuarioId: " + usuarioId));

        return modelMapper.map(entity, Patient.class);
    }
    @Override
    public Patient updatePatientPartial(UpdatePatientDTO dto) {
        PatientsEntity entity = patientsRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado con id: " + dto.getId()));

        // Actualizamos solo si el valor no es null
        if (dto.getNombre() != null) entity.setNombre(dto.getNombre());
        if (dto.getApellido() != null) entity.setApellido(dto.getApellido());
        if (dto.getTelefono() != null) entity.setTelefono(dto.getTelefono());
        if (dto.getDireccion() != null) entity.setDireccion(dto.getDireccion());
        if (dto.getTipoDiscapacidad() != null) entity.setTipoDiscapacidad(dto.getTipoDiscapacidad());
        if (dto.getNroAfiliadoObraSocial() != null) entity.setNroAfiliadoObraSocial(dto.getNroAfiliadoObraSocial());
        if (dto.getCodigoObraSocial() != null) entity.setCodigoObraSocial(dto.getCodigoObraSocial());

        PatientsEntity actualizado = patientsRepository.save(entity);

        return modelMapper.map(actualizado, Patient.class);
    }
}