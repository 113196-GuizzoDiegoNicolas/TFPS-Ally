package Ally.Scafolding.repositories;

import Ally.Scafolding.entities.PatientLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientLocationRepository extends JpaRepository<PatientLocationEntity, Long> {
    Optional<PatientLocationEntity> findTopByPacienteIdOrderByUpdatedAtDesc(Long pacienteId);
}
