package Ally.Scafolding.controllers;

import Ally.Scafolding.models.Patient;
import Ally.Scafolding.services.PatientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing patients.
 * <p>
 * Handles HTTP requests related to patient operations,
 * such as creating, updating, retrieving, and deleting patients.
 * </p>
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/patients")
public class PatientController {

    /**
     * The ModelMapper used for mapping between DTOs and entities.
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Service for managing patients.
     */
    @Autowired
    private PatientService patientService;

    /**
     * Creates a new patient.
     * @param patient the patient data to create
     * @return a ResponseEntity containing the created patient
     */
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient createdPatient = patientService.createPatient(patient);
        return ResponseEntity.ok(createdPatient);
    }

    /**
     * Updates an existing patient.
     * @param id the ID of the patient to update
     * @param patient the updated patient data
     * @return a ResponseEntity containing the updated patient
     */
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        Patient updatedPatient = patientService.updatePatient(id, patient);
        return ResponseEntity.ok(updatedPatient);
    }

    /**
     * Retrieves a patient by ID.
     * @param id the ID of the patient to retrieve
     * @return a ResponseEntity containing the patient
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }

    /**
     * Retrieves all patients.
     * @return a ResponseEntity containing a list of patients
     */
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    /**
     * Deletes a patient by ID.
     * @param id the ID of the patient to delete
     * @return a ResponseEntity indicating the result of the operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletePatient(@PathVariable Long id) {
        boolean deleted = patientService.deletePatient(id);
        return ResponseEntity.ok(deleted);
    }
}



