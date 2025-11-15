package Ally.Scafolding.services;

import Ally.Scafolding.dtos.common.transporter.TransporterDTO;
import Ally.Scafolding.dtos.common.transporter.TransporterCreateDTO;

import java.util.List;

/**
 * Service interface for transporter management operations.
 * Defines the business logic contract for handling transporter entities.
 * All methods include proper validation and business rule enforcement.
 *
 * @version 1.0
 * @since 2024
 */
public interface TransporterService {

    /**
     * Retrieves all transporters from the system regardless of activation status.
     *
     * @return a list of all transporter DTOs
     */
    List<TransporterDTO> findAll();

    /**
     * Finds a specific transporter by their unique identifier.
     *
     * @param id the transporter ID to search for
     * @return the transporter DTO if found, null otherwise
     * @throws IllegalArgumentException if the id is null
     */
    TransporterDTO findById(Long id);

    /**
     * Creates a new transporter in the system after validating all business rules.
     *
     * @param transporterCreateDTO the DTO containing transporter creation data
     * @return the created transporter DTO with system-generated ID
     * @throws IllegalArgumentException if validation fails or user already has a transporter
     */
    TransporterDTO create(TransporterCreateDTO transporterCreateDTO);

    /**
     * Updates an existing transporter with the provided data.
     * Only updates provided fields and enforces business validation rules.
     *
     * @param id the transporter ID to update
     * @param transporterDTO the DTO containing updated transporter data
     * @return the updated transporter DTO
     * @throws IllegalArgumentException if transporter not found or validation fails
     */
    TransporterDTO update(Long id, TransporterDTO transporterDTO);

    /**
     * Performs a logical deletion of a transporter by setting their status to inactive.
     * The transporter record remains in the database but is marked as inactive.
     *
     * @param id the transporter ID to deactivate
     * @throws IllegalArgumentException if transporter not found
     */
    void delete(Long id);

    /**
     * Changes the activation status of a transporter.
     * Can be used to both activate and deactivate transporters.
     *
     * @param id the transporter ID to modify
     * @param activo the new activation status (true = active, false = inactive)
     * @return the updated transporter DTO
     * @throws IllegalArgumentException if transporter not found
     */
    TransporterDTO changeActivation(Long id, Boolean activo);

    /**
     * Finds transporters by their coverage zone with partial matching.
     *
     * @param zonaCobertura the coverage zone to search for
     * @return a list of transporters in the specified coverage zone
     * @throws IllegalArgumentException if zonaCobertura is null or empty
     */
    List<TransporterDTO> findByZonaCobertura(String zonaCobertura);

    /**
     * Finds active transporters by their coverage zone.
     * Combines coverage zone filtering with active status check.
     *
     * @param zonaCobertura the coverage zone to search for
     * @return a list of active transporters in the specified coverage zone
     * @throws IllegalArgumentException if zonaCobertura is null or empty
     */
    List<TransporterDTO> findActiveByZonaCobertura(String zonaCobertura);

    /**
     * Finds a transporter by their email address.
     *
     * @param email the email address to search for
     * @return the transporter DTO if found, null otherwise
     * @throws IllegalArgumentException if email is null or empty
     */
    TransporterDTO findByEmail(String email);

    /**
     * Finds a transporter by their associated user ID.
     *
     * @param usuarioId the user ID to search for
     * @return the transporter DTO if found, null otherwise
     * @throws IllegalArgumentException if usuarioId is null
     */
    TransporterDTO findByUsuarioId(Long usuarioId);

    /**
     * Searches transporters by first name or last name with partial matching.
     *
     * @param nombre the name to search for in first name or last name fields
     * @return a list of matching transporters
     * @throws IllegalArgumentException if nombre is null or empty
     */
    List<TransporterDTO> findByNombreOrApellidoContaining(String nombre);

    /**
     * Finds transporters by their activation status.
     *
     * @param activo the activation status to filter by
     * @return a list of transporters with the specified activation status
     * @throws IllegalArgumentException if activo is null
     */
    List<TransporterDTO> findByActivo(Boolean activo);

    /**
     * Checks if a transporter exists with the specified email address.
     *
     * @param email the email address to check
     * @return true if a transporter exists with the email, false otherwise
     * @throws IllegalArgumentException if email is null or empty
     */
    boolean existsByEmail(String email);

    /**
     * Checks if a transporter exists with the specified user ID.
     *
     * @param usuarioId the user ID to check
     * @return true if a transporter exists with the user ID, false otherwise
     * @throws IllegalArgumentException if usuarioId is null
     */
    boolean existsByUsuarioId(Long usuarioId);

    /**
     * Finds transporters whose coverage zones contain the specified location.
     *
     * @param ubicacion the location to search within coverage zones
     * @return a list of transporters that cover the specified location
     * @throws IllegalArgumentException if ubicacion is null or empty
     */
    List<TransporterDTO> findByZonaCoberturaContainingLocation(String ubicacion);

    /**
     * Counts the number of active transporters in the system.
     *
     * @return the number of active transporters
     */
    Long countActiveTransporters();

    /**
     * Counts the number of transporters in a specific coverage zone.
     *
     * @param zonaCobertura the coverage zone to count
     * @return the number of transporters in the specified coverage zone
     * @throws IllegalArgumentException if zonaCobertura is null or empty
     */
    Long countByZonaCobertura(String zonaCobertura);

    /**
     * Retrieves all unique coverage zones from the system.
     *
     * @return a list of distinct coverage zones
     */
    List<String> findAllZonasCobertura();

    /**
     * Reactivates a previously deactivated transporter.
     *
     * @param id the transporter ID to reactivate
     * @return the reactivated transporter DTO
     * @throws IllegalArgumentException if transporter not found
     */
    TransporterDTO reactivate(Long id);
}