package Ally.Scafolding.services;

import Ally.Scafolding.models.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service interface for managing patients.
 * <p>
 * Provides methods for creating, retrieving, updating, and deleting patients.
 * </p>
 */

public interface PatientService {

    /**
     * Creates a new patient.
     * @param patient the patient data to create
     * @return the created patient
     */
    Patient createPatient(Patient patient);

    /**
     * Retrieves a patient by its unique identifier.
     * @param id the unique identifier of the patient
     * @return the patient with the specified id
     */
    Patient getPatientById(Long id);

    /**
     * Retrieves all patients.
     * @return a list of all patients
     */
    List<Patient> getAllPatients();

    /**
     * Retrieves a paginated list of patients.
     * @param pageable pagination information
     * @return a page of patients
     */
    Page<Patient> getPatients(Pageable pageable);

    /**
     * Updates an existing patient.
     * @param id the id of the patient to update
     * @param patient the updated patient data
     * @return the updated patient
     */
    Patient updatePatient(Long id, Patient patient);

    /**
     * Deletes a patient by its unique identifier.
     * @param id the unique identifier of the patient to be deleted
     * @return true if the patient was successfully deleted, false otherwise
     */
    boolean deletePatient(Long id);
}