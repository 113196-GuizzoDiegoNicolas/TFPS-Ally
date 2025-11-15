package Ally.Scafolding.controllers;

import Ally.Scafolding.dtos.common.transporter.TransporterDTO;
import Ally.Scafolding.dtos.common.transporter.TransporterCreateDTO;
import Ally.Scafolding.services.TransporterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for handling transporter-related HTTP requests.
 * Provides comprehensive endpoints for CRUD operations, search, filtering,
 * and management of transporters within the system.
 *
 * This controller follows RESTful conventions and returns appropriate
 * HTTP status codes and response entities for all operations.
 *
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/transportistas")
@RequiredArgsConstructor
public class TransporterController {

    /**
     * Service layer dependency for handling transporter business logic.
     * Injected automatically by Spring via constructor injection.
     */
    private final TransporterService transporterService;

    /**
     * Retrieves all transporters from the system.
     * Returns both active and inactive transporters for complete overview.
     *
     * @return ResponseEntity containing list of all transporter DTOs with HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<TransporterDTO>> getAllTransportistas() {
        List<TransporterDTO> transportistas = transporterService.findAll();
        return ResponseEntity.ok(transportistas);
    }

    /**
     * Retrieves a specific transporter by their unique identifier.
     *
     * @param id the unique identifier of the transporter to retrieve
     * @return ResponseEntity containing the transporter DTO with HTTP 200 status if found,
     *         or HTTP 404 if not found (handled by service returning null)
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransporterDTO> getTransportistaById(@PathVariable Long id) {
        TransporterDTO transportista = transporterService.findById(id);
        return ResponseEntity.ok(transportista);
    }

    /**
     * Creates a new transporter in the system.
     * Validates input data and returns the created resource with location header.
     *
     * @param transporterCreateDTO the DTO containing transporter creation data
     * @return ResponseEntity containing the created transporter DTO with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<TransporterDTO> createTransportista(@RequestBody TransporterCreateDTO transporterCreateDTO) {
        TransporterDTO createdTransportista = transporterService.create(transporterCreateDTO);
        return new ResponseEntity<>(createdTransportista, HttpStatus.CREATED);
    }

    /**
     * Updates an existing transporter with the provided data.
     * Performs partial updates, only modifying the fields provided in the request.
     *
     * @param id the ID of the transporter to update
     * @param transporterDTO the DTO containing updated transporter data
     * @return ResponseEntity containing the updated transporter DTO with HTTP 200 status
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransporterDTO> updateTransportista(
            @PathVariable Long id,
            @RequestBody TransporterDTO transporterDTO) {
        TransporterDTO updatedTransportista = transporterService.update(id, transporterDTO);
        return ResponseEntity.ok(updatedTransportista);
    }

    /**
     * Performs logical deletion of a transporter by setting their status to inactive.
     * The transporter record remains in the database but is marked as inactive.
     *
     * @param id the ID of the transporter to deactivate
     * @return ResponseEntity with HTTP 204 No Content status upon successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransportista(@PathVariable Long id) {
        transporterService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Changes the activation status of a transporter.
     * Can be used to both activate and deactivate transporters.
     *
     * @param id the ID of the transporter to modify
     * @param activo the new activation status (true = active, false = inactive)
     * @return ResponseEntity containing the updated transporter DTO with HTTP 200 status
     */
    @PatchMapping("/{id}/activation")
    public ResponseEntity<TransporterDTO> changeActivation(
            @PathVariable Long id,
            @RequestParam Boolean activo) {
        TransporterDTO updatedTransportista = transporterService.changeActivation(id, activo);
        return ResponseEntity.ok(updatedTransportista);
    }

    /**
     * Reactivates a previously deactivated transporter.
     * Specifically designed for reactivation operations with clear semantic meaning.
     *
     * @param id the ID of the transporter to reactivate
     * @return ResponseEntity containing the reactivated transporter DTO with HTTP 200 status
     */
    @PatchMapping("/{id}/reactivar")
    public ResponseEntity<TransporterDTO> reactivarTransportista(@PathVariable Long id) {
        TransporterDTO reactivatedTransportista = transporterService.reactivate(id);
        return ResponseEntity.ok(reactivatedTransportista);
    }

    /**
     * Finds transporters by their coverage zone with partial matching.
     * Returns transporters whose coverage zone contains the specified text.
     *
     * @param zonaCobertura the coverage zone text to search for
     * @return ResponseEntity containing list of matching transporters with HTTP 200 status
     */
    @GetMapping("/zona/{zonaCobertura}")
    public ResponseEntity<List<TransporterDTO>> getTransportistasByZona(@PathVariable String zonaCobertura) {
        List<TransporterDTO> transportistas = transporterService.findByZonaCobertura(zonaCobertura);
        return ResponseEntity.ok(transportistas);
    }

    /**
     * Finds active transporters by their coverage zone.
     * Combines coverage zone filtering with active status check.
     *
     * @param zonaCobertura the coverage zone text to search for
     * @return ResponseEntity containing list of active transporters in the specified zone with HTTP 200 status
     */
    @GetMapping("/zona/{zonaCobertura}/activos")
    public ResponseEntity<List<TransporterDTO>> getTransportistasActivosByZona(@PathVariable String zonaCobertura) {
        List<TransporterDTO> transportistas = transporterService.findActiveByZonaCobertura(zonaCobertura);
        return ResponseEntity.ok(transportistas);
    }

    /**
     * Searches transporters by first name or last name with partial matching.
     * Useful for implementing search functionality in user interfaces.
     *
     * @param nombre the name text to search for in first name or last name fields
     * @return ResponseEntity containing list of matching transporters with HTTP 200 status
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<TransporterDTO>> searchTransportistas(@RequestParam String nombre) {
        List<TransporterDTO> transportistas = transporterService.findByNombreOrApellidoContaining(nombre);
        return ResponseEntity.ok(transportistas);
    }

    /**
     * Finds transporters by activation status.
     * Allows filtering transporters based on whether they are active or inactive.
     *
     * @param activo the activation status to filter by (true for active, false for inactive)
     * @return ResponseEntity containing list of transporters with the specified status with HTTP 200 status
     */
    @GetMapping("/estado/{activo}")
    public ResponseEntity<List<TransporterDTO>> getTransportistasByEstado(@PathVariable Boolean activo) {
        List<TransporterDTO> transportistas = transporterService.findByActivo(activo);
        return ResponseEntity.ok(transportistas);
    }

    /**
     * CORREGIDO: Endpoint para obtener transportistas activos.
     * Utiliza el método findByActivo(true) en lugar del método inexistente findActivos().
     *
     * @return ResponseEntity containing list of active transporters with HTTP 200 status
     */
    @GetMapping("/activos")
    public ResponseEntity<List<TransporterDTO>> getTransportistasActivos() {
        List<TransporterDTO> transportistas = transporterService.findByActivo(true);
        return ResponseEntity.ok(transportistas);
    }

    /**
     * Finds a transporter by email address with exact matching.
     *
     * @param email the email address to search for
     * @return ResponseEntity containing the transporter DTO with HTTP 200 status if found
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<TransporterDTO> getTransportistaByEmail(@PathVariable String email) {
        TransporterDTO transportista = transporterService.findByEmail(email);
        return ResponseEntity.ok(transportista);
    }

    /**
     * Finds a transporter by their associated user ID.
     *
     * @param usuarioId the user ID to search for
     * @return ResponseEntity containing the transporter DTO with HTTP 200 status if found
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<TransporterDTO> getTransportistaByUsuarioId(@PathVariable Long usuarioId) {
        TransporterDTO transportista = transporterService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(transportista);
    }

    /**
     * Counts the number of active transporters in the system.
     * Useful for dashboards and reporting.
     *
     * @return ResponseEntity containing the count of active transporters with HTTP 200 status
     */
    @GetMapping("/contador/activos")
    public ResponseEntity<Long> countActiveTransporters() {
        Long count = transporterService.countActiveTransporters();
        return ResponseEntity.ok(count);
    }

    /**
     * Counts the number of transporters in a specific coverage zone.
     *
     * @param zonaCobertura the coverage zone to count transporters for
     * @return ResponseEntity containing the count with HTTP 200 status
     */
    @GetMapping("/contador/zona/{zonaCobertura}")
    public ResponseEntity<Long> countByZonaCobertura(@PathVariable String zonaCobertura) {
        Long count = transporterService.countByZonaCobertura(zonaCobertura);
        return ResponseEntity.ok(count);
    }

    /**
     * Retrieves all unique coverage zones from the system.
     * Useful for populating dropdowns and filters.
     *
     * @return ResponseEntity containing list of distinct coverage zones with HTTP 200 status
     */
    @GetMapping("/zonas-cobertura")
    public ResponseEntity<List<String>> getAllZonasCobertura() {
        List<String> zonas = transporterService.findAllZonasCobertura();
        return ResponseEntity.ok(zonas);
    }
}