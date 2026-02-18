package Ally.Scafolding.services;

import Ally.Scafolding.dtos.common.location.PatientLocationDTO;

public interface PatientLocationService {
    PatientLocationDTO getLastLocation(Long pacienteId);
    PatientLocationDTO saveLocation(Long pacienteId, PatientLocationDTO dto);
}
