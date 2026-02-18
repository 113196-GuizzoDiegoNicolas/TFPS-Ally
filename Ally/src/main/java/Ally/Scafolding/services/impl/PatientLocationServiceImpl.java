package Ally.Scafolding.services.impl;

import Ally.Scafolding.dtos.common.location.PatientLocationDTO;
import Ally.Scafolding.entities.PatientLocationEntity;
import Ally.Scafolding.entities.PatientsEntity;
import Ally.Scafolding.repositories.PatientLocationRepository;
import Ally.Scafolding.repositories.PatientsRepository;
import Ally.Scafolding.services.PatientLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PatientLocationServiceImpl implements PatientLocationService {

    private final PatientLocationRepository repo;
    private final PatientsRepository patientsRepo;

    @Override
    public PatientLocationDTO getLastLocation(Long pacienteId) {
        return repo.findTopByPacienteIdOrderByUpdatedAtDesc(pacienteId)
                .map(e -> {
                    PatientLocationDTO dto = new PatientLocationDTO();
                    dto.setPacienteId(pacienteId);
                    dto.setLat(e.getLat());
                    dto.setLng(e.getLng());
                    dto.setAddressText(e.getAddressText());
                    dto.setUpdatedAt(e.getUpdatedAt());
                    return dto;
                })
                .orElse(null);
    }

    @Override
    public PatientLocationDTO saveLocation(Long pacienteId, PatientLocationDTO dto) {
        PatientsEntity paciente = patientsRepo.findById(pacienteId)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no existe"));

        PatientLocationEntity e = new PatientLocationEntity();
        e.setPaciente(paciente);
        e.setLat(dto.getLat());
        e.setLng(dto.getLng());
        e.setAddressText(dto.getAddressText());
        e.setUpdatedAt(LocalDateTime.now());

        PatientLocationEntity saved = repo.save(e);

        PatientLocationDTO out = new PatientLocationDTO();
        out.setPacienteId(pacienteId);
        out.setLat(saved.getLat());
        out.setLng(saved.getLng());
        out.setAddressText(saved.getAddressText());
        out.setUpdatedAt(saved.getUpdatedAt());
        return out;
    }
}