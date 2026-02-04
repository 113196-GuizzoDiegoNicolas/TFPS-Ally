package Ally.Scafolding.controllers;

import Ally.Scafolding.dtos.common.patient.UpdatePatientDTO;
import Ally.Scafolding.models.Patient;
import Ally.Scafolding.services.PatientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PatientService patientService;

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.createPatient(patient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.updatePatient(id, patient));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletePatient(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.deletePatient(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Patient> getByUserId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(patientService.getPatientByUsuarioId(usuarioId));
    }

    // ENDPOINT CORRECTO
    @PatchMapping
    public ResponseEntity<Patient> updatePartial(@RequestBody UpdatePatientDTO dto) {
        Patient actualizado = patientService.updatePatientPartial(dto);
        return ResponseEntity.ok(actualizado);
    }
    /**
     * Endpoint para obtener la cantidad total de pacientes.
     * Ejemplo: GET /api/patients/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPatients() {
        long total = patientService.countPatients();
        return ResponseEntity.ok(total);
    }
}




