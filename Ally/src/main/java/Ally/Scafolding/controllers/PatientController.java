package Ally.Scafolding.controllers;

import Ally.Scafolding.dtos.common.contact.PatientDTO;
import Ally.Scafolding.models.Patient;
import Ally.Scafolding.services.PatientService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing patients.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PatientService patientService;

    /**
     * Creates a new patient.
     */
    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientDTO patientDTO) {
        System.out.println(" JSON recibido en backend:");
        System.out.println(patientDTO);

        // Mapeo DTO → modelo interno
        Patient patient = modelMapper.map(patientDTO, Patient.class);

        // Lógica de servicio
        Patient createdPatient = patientService.createPatient(patient);

        // Devuelve el DTO nuevamente (para coherencia con front)
        PatientDTO responseDTO = modelMapper.map(createdPatient, PatientDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Updates an existing patient.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @RequestBody PatientDTO patientDTO) {
        Patient patient = modelMapper.map(patientDTO, Patient.class);
        Patient updatedPatient = patientService.updatePatient(id, patient);
        PatientDTO responseDTO = modelMapper.map(updatedPatient, PatientDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Retrieves a patient by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatient(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        PatientDTO responseDTO = modelMapper.map(patient, PatientDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Retrieves all patients.
     */
    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patients = patientService.getAllPatients()
                .stream()
                .map(p -> modelMapper.map(p, PatientDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(patients);
    }

    /**
     * Deletes a patient by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletePatient(@PathVariable Long id) {
        boolean deleted = patientService.deletePatient(id);
        return ResponseEntity.ok(deleted);
    }
}



