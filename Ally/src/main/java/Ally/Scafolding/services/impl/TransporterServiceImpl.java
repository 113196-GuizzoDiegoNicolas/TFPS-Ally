package Ally.Scafolding.services.impl;

import Ally.Scafolding.dtos.common.transporter.TransporterCreateDTO;
import Ally.Scafolding.dtos.common.transporter.TransporterDTO;
import Ally.Scafolding.entities.TransportersEntity;
import Ally.Scafolding.entities.UsersEntity;
import Ally.Scafolding.repositories.TransportersRepository;
import Ally.Scafolding.repositories.UsersRepository;
import Ally.Scafolding.services.TransporterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the TransporterService interface.
 * This service class provides the business logic for managing transporter entities
 * including CRUD operations, validation, and business rule enforcement.
 * It utilizes ModelMapper for object-to-object mapping between entities and DTOs.
 *
 * The service ensures data integrity through comprehensive validation and
 * implements logical deletion by setting the 'activo' field to false instead
 * of physically removing records from the database.
 *
 * @version 1.0
 * @since 2024
 */
@Service
public class TransporterServiceImpl implements TransporterService {

    /**
     * Repository for performing database operations on transporter entities.
     * Provides data access methods for transporter management.
     */
    @Autowired
    private TransportersRepository transportersRepository;

    /**
     * Repository for performing database operations on user entities.
     * Used to validate and manage user relationships with transporters.
     */
    @Autowired
    private UsersRepository usersRepository;

    /**
     * Main ModelMapper instance for object-to-object mapping operations.
     * Used for complete mapping between entities and DTOs in both directions.
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Specialized ModelMapper configured for merge operations.
     * This mapper skips null values during mapping, making it ideal for
     * partial updates where only specific fields need to be modified.
     */
    @Autowired
    @Qualifier("mergerMapper")
    private ModelMapper mergerMapper;

    /**
     * Retrieves all transporters from the system regardless of their activation status.
     * This method returns both active and inactive transporters, providing a complete
     * view of all transporter records in the database.
     *
     * @return List<TransporterDTO> a list of all transporter DTOs
     */
    @Override
    public List<TransporterDTO> findAll() {
        return transportersRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, TransporterDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Finds a specific transporter by their unique identifier.
     * Returns null if no transporter is found with the provided ID.
     *
     * @param id the unique identifier of the transporter to retrieve
     * @return TransporterDTO the transporter DTO if found, null otherwise
     */
    @Override
    public TransporterDTO findById(Long id) {
        return transportersRepository.findById(id)
                .map(entity -> modelMapper.map(entity, TransporterDTO.class))
                .orElse(null);
    }

    /**
     * Creates a new transporter in the system after performing comprehensive validation.
     * Validates all required fields, checks for duplicate emails, and ensures the
     * associated user exists and doesn't already have a transporter assigned.
     *
     * @param transporterCreateDTO the DTO containing all necessary data for transporter creation
     * @return TransporterDTO the created transporter DTO with system-generated ID
     * @throws IllegalArgumentException if validation fails, user doesn't exist,
     *         or user already has a transporter assigned
     */
    @Override
    public TransporterDTO create(TransporterCreateDTO transporterCreateDTO) {
        validateTransporterCreation(transporterCreateDTO);

        UsersEntity usuarioEntity = usersRepository.findById(transporterCreateDTO.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Associated user does not exist"));

        if (transportersRepository.existsByUsersEntityId(transporterCreateDTO.getUsuarioId())) {
            throw new IllegalArgumentException("User already has a transporter assigned");
        }




        TransportersEntity entity = new TransportersEntity();

        entity.setNombre(transporterCreateDTO.getNombre());
        entity.setApellido(transporterCreateDTO.getApellido());
        entity.setDireccion(transporterCreateDTO.getDireccion());
        entity.setTelefono(transporterCreateDTO.getTelefono());
        entity.setTelegram(transporterCreateDTO.getTelegram());
        entity.setCorreoElectronico(transporterCreateDTO.getCorreoElectronico());

     //  parseo seguro
        entity.setFechaNacimiento(transporterCreateDTO.getFechaNacimiento());

        entity.setZonaCobertura(transporterCreateDTO.getZonaCobertura());
        entity.setActivo(true);
        entity.setUsersEntity(usuarioEntity);

        TransportersEntity saved = transportersRepository.saveAndFlush(entity);
        return modelMapper.map(saved, TransporterDTO.class);

    }

    /**
     * Updates an existing transporter with the provided data.
     * Only updates non-null fields from the DTO and prevents updates
     * on inactive transporters. Validates email format and uniqueness
     * if the email is being modified.
     *
     * @param id the ID of the transporter to update
     * @param transporterDTO the DTO containing updated transporter data
     * @return TransporterDTO the updated transporter DTO
     * @throws IllegalArgumentException if transporter not found, is inactive,
     *         or validation fails
     */
    @Override
    public TransporterDTO update(Long id, TransporterDTO transporterDTO) {
        return transportersRepository.findById(id)
                .map(existing -> {
                    // Do not allow updating inactive transporters
                    if (!existing.getActivo()) {
                        throw new IllegalArgumentException("Cannot update an inactive transporter");
                    }

                    validateTransporterUpdate(transporterDTO, existing);

                    // Use mergerMapper to update only non-null fields
                    mergerMapper.map(transporterDTO, existing);

                    // Handle user relationship update if provided
                    if (transporterDTO.getUsuarioId() != null &&
                            !transporterDTO.getUsuarioId().equals(existing.getUsersEntity().getId())) {
                        UsersEntity newUser = usersRepository.findById(transporterDTO.getUsuarioId())
                                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + transporterDTO.getUsuarioId()));
                        existing.setUsersEntity(newUser);
                    }

                    TransportersEntity updated = transportersRepository.save(existing);
                    return modelMapper.map(updated, TransporterDTO.class);
                })
                .orElse(null);
    }

    /**
     * Performs a logical deletion of a transporter by setting their status to inactive.
     * The transporter record remains in the database but is marked as inactive,
     * preserving historical data and maintaining referential integrity.
     *
     * @param id the ID of the transporter to deactivate
     * @throws IllegalArgumentException if no transporter is found with the provided ID
     */
    @Override
    public void delete(Long id) {
        TransportersEntity transporter = transportersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transporter not found with ID: " + id));

        transporter.setActivo(false);
        transportersRepository.save(transporter);
    }

    /**
     * Changes the activation status of a transporter.
     * Can be used to both activate and deactivate transporters.
     * This method provides a generic way to modify the active status.
     *
     * @param id the ID of the transporter to modify
     * @param activo the new activation status (true for active, false for inactive)
     * @return TransporterDTO the updated transporter DTO with new activation status
     */
    @Override
    public TransporterDTO changeActivation(Long id, Boolean activo) {
        return transportersRepository.findById(id)
                .map(transporter -> {
                    transporter.setActivo(activo);
                    TransportersEntity updated = transportersRepository.save(transporter);
                    return modelMapper.map(updated, TransporterDTO.class);
                })
                .orElse(null);
    }

    /**
     * Reactivates a previously deactivated transporter.
     * This method specifically sets the activation status to true,
     * providing a clear semantic meaning for reactivation operations.
     *
     * @param id the ID of the transporter to reactivate
     * @return TransporterDTO the reactivated transporter DTO
     * @throws IllegalArgumentException if no transporter is found with the provided ID
     */
    @Override
    public TransporterDTO reactivate(Long id) {
        return transportersRepository.findById(id)
                .map(transporter -> {
                    transporter.setActivo(true);
                    TransportersEntity updated = transportersRepository.save(transporter);
                    return modelMapper.map(updated, TransporterDTO.class);
                })
                .orElseThrow(() -> new IllegalArgumentException("Transporter not found with ID: " + id));
    }

    /**
     * Finds transporters by their coverage zone with case-insensitive partial matching.
     * Returns all transporters whose coverage zone contains the specified text,
     * regardless of their activation status.
     *
     * @param zonaCobertura the coverage zone text to search for
     * @return List<TransporterDTO> list of transporters matching the coverage zone criteria
     * @throws IllegalArgumentException if zonaCobertura is null or empty
     */
    @Override
    public List<TransporterDTO> findByZonaCobertura(String zonaCobertura) {
        if (zonaCobertura == null || zonaCobertura.trim().isEmpty()) {
            throw new IllegalArgumentException("Coverage zone cannot be null or empty");
        }
        return transportersRepository.findByZonaCoberturaContainingIgnoreCase(zonaCobertura).stream()
                .map(entity -> modelMapper.map(entity, TransporterDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Finds active transporters by their coverage zone.
     * Combines coverage zone filtering with active status check to return
     * only active transporters serving the specified area.
     *
     * @param zonaCobertura the coverage zone text to search for
     * @return List<TransporterDTO> list of active transporters in the specified coverage zone
     * @throws IllegalArgumentException if zonaCobertura is null or empty
     */
    @Override
    public List<TransporterDTO> findActiveByZonaCobertura(String zonaCobertura) {
        if (zonaCobertura == null || zonaCobertura.trim().isEmpty()) {
            throw new IllegalArgumentException("Coverage zone cannot be null or empty");
        }
        return transportersRepository.findByZonaCoberturaContainingIgnoreCaseAndActivo(zonaCobertura, true).stream()
                .map(entity -> modelMapper.map(entity, TransporterDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Finds a transporter by their email address with exact matching.
     * Email search is case-sensitive and requires exact match.
     *
     * @param email the email address to search for
     * @return TransporterDTO the transporter DTO if found, null otherwise
     * @throws IllegalArgumentException if email is null or empty
     */
    @Override
    public TransporterDTO findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        return transportersRepository.findByCorreoElectronico(email)
                .map(entity -> modelMapper.map(entity, TransporterDTO.class))
                .orElse(null);
    }

    /**
     * Finds a transporter by their associated user ID.
     * Each user can be associated with only one transporter, ensuring
     * a one-to-one relationship between users and transporters.
     *
     * @param usuarioId the user ID to search for
     * @return TransporterDTO the transporter DTO if found, null otherwise
     * @throws IllegalArgumentException if usuarioId is null
     */
    @Override
    public TransporterDTO findByUsuarioId(Long usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return transportersRepository.findByUsersEntityId(usuarioId)
                .map(entity -> modelMapper.map(entity, TransporterDTO.class))
                .orElse(null);
    }

    /**
     * Searches transporters by first name or last name with case-insensitive partial matching.
     * Returns transporters whose first name or last name contains the specified text.
     * Useful for implementing search functionality in user interfaces.
     *
     * @param nombre the name text to search for in first name or last name fields
     * @return List<TransporterDTO> list of transporters matching the name criteria
     * @throws IllegalArgumentException if nombre is null or empty
     */
    @Override
    public List<TransporterDTO> findByNombreOrApellidoContaining(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        return transportersRepository.findByNombreOrApellidoContainingIgnoreCase(nombre).stream()
                .map(entity -> modelMapper.map(entity, TransporterDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Finds transporters by their activation status.
     * Allows filtering transporters based on whether they are active or inactive.
     *
     * @param activo the activation status to filter by (true for active, false for inactive)
     * @return List<TransporterDTO> list of transporters with the specified activation status
     * @throws IllegalArgumentException if activo is null
     */
    @Override
    public List<TransporterDTO> findByActivo(Boolean activo) {
        if (activo == null) {
            throw new IllegalArgumentException("Activation status cannot be null");
        }
        return transportersRepository.findByActivo(activo).stream()
                .map(entity -> modelMapper.map(entity, TransporterDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Checks if a transporter exists with the specified email address.
     * Used for validation during creation and update operations to prevent duplicates.
     *
     * @param email the email address to check
     * @return boolean true if a transporter exists with the email, false otherwise
     * @throws IllegalArgumentException if email is null or empty
     */
    @Override
    public boolean existsByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        return transportersRepository.existsByCorreoElectronico(email);
    }

    /**
     * Checks if a transporter exists with the specified user ID.
     * Ensures the one-to-one relationship between users and transporters is maintained.
     *
     * @param usuarioId the user ID to check
     * @return boolean true if a transporter exists with the user ID, false otherwise
     * @throws IllegalArgumentException if usuarioId is null
     */
    @Override
    public boolean existsByUsuarioId(Long usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return transportersRepository.existsByUsersEntityId(usuarioId);
    }

    /**
     * Finds transporters whose coverage zones contain the specified location.
     * Provides a more flexible search capability for location-based queries.
     * This implementation uses simple text matching; more complex geolocation
     * logic could be implemented as needed.
     *
     * @param ubicacion the location text to search within coverage zones
     * @return List<TransporterDTO> list of transporters that cover the specified location
     * @throws IllegalArgumentException if ubicacion is null or empty
     */
    @Override
    public List<TransporterDTO> findByZonaCoberturaContainingLocation(String ubicacion) {
        if (ubicacion == null || ubicacion.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be null or empty");
        }
        return transportersRepository.findByZonaCoberturaContainingIgnoreCase(ubicacion).stream()
                .map(entity -> modelMapper.map(entity, TransporterDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Counts the number of active transporters in the system.
     * Useful for dashboards, reports, and system monitoring.
     *
     * @return Long the number of active transporters
     */
    @Override
    public Long countActiveTransporters() {
        return transportersRepository.findByActivo(true).stream().count();
    }

    /**
     * Counts the number of transporters in a specific coverage zone.
     * Provides statistical information about transporter distribution.
     *
     * @param zonaCobertura the coverage zone to count transporters for
     * @return Long the number of transporters in the specified coverage zone
     * @throws IllegalArgumentException if zonaCobertura is null or empty
     */
    @Override
    public Long countByZonaCobertura(String zonaCobertura) {
        if (zonaCobertura == null || zonaCobertura.trim().isEmpty()) {
            throw new IllegalArgumentException("Coverage zone cannot be null or empty");
        }
        return transportersRepository.findByZonaCoberturaContainingIgnoreCase(zonaCobertura).stream().count();
    }

    /**
     * Retrieves all unique coverage zones from the system.
     * Useful for populating dropdowns, filters, or providing users with
     * available service area options.
     *
     * @return List<String> list of distinct coverage zones
     */
    @Override
    public List<String> findAllZonasCobertura() {
        return transportersRepository.findAll().stream()
                .map(TransportersEntity::getZonaCobertura)
                .distinct()
                .collect(Collectors.toList());
    }

    // ============ VALIDATION METHODS ============

    /**
     * Validates transporter creation data before persisting to database.
     * Performs comprehensive validation including required field checks,
     * email format validation, and duplicate email detection.
     *
     * @param transporterCreateDTO the DTO containing transporter creation data
     * @throws IllegalArgumentException if any validation rule is violated:
     *         - First name is null or empty
     *         - Last name is null or empty
     *         - Email is null or empty
     *         - Coverage zone is null or empty
     *         - User ID is null
     *         - Email format is invalid
     *         - Email is already in use by another transporter
     */
    private void validateTransporterCreation(TransporterCreateDTO transporterCreateDTO) {
        if (transporterCreateDTO.getNombre() == null || transporterCreateDTO.getNombre().trim().isEmpty())
            throw new IllegalArgumentException("First name cannot be empty");

        if (transporterCreateDTO.getApellido() == null || transporterCreateDTO.getApellido().trim().isEmpty())
            throw new IllegalArgumentException("Last name cannot be empty");

        if (transporterCreateDTO.getCorreoElectronico() == null || transporterCreateDTO.getCorreoElectronico().trim().isEmpty())
            throw new IllegalArgumentException("Email cannot be empty");

        if (transporterCreateDTO.getZonaCobertura() == null || transporterCreateDTO.getZonaCobertura().trim().isEmpty())
            throw new IllegalArgumentException("Coverage zone cannot be empty");

        if (transporterCreateDTO.getUsuarioId() == null)
            throw new IllegalArgumentException("User ID cannot be empty");

        if (!isValidEmail(transporterCreateDTO.getCorreoElectronico()))
            throw new IllegalArgumentException("Email format is not valid");

        if (transportersRepository.existsByCorreoElectronico(transporterCreateDTO.getCorreoElectronico())) {
            throw new IllegalArgumentException("Email is already in use by another transporter");
        }
    }

    /**
     * Validates transporter update data before applying changes.
     * Ensures data integrity during update operations by checking for
     * email format validity and uniqueness when email is being modified.
     *
     * @param transporterDTO the DTO containing updated transporter data
     * @param existing the existing transporter entity being updated
     * @throws IllegalArgumentException if validation fails:
     *         - Email is being changed to one already in use by another transporter
     *         - Email format is invalid
     */
    private void validateTransporterUpdate(TransporterDTO transporterDTO, TransportersEntity existing) {
        if (transporterDTO.getCorreoElectronico() != null &&
                !existing.getCorreoElectronico().equals(transporterDTO.getCorreoElectronico()) &&
                transportersRepository.existsByCorreoElectronico(transporterDTO.getCorreoElectronico())) {
            throw new IllegalArgumentException("Email is already in use by another transporter");
        }

        if (transporterDTO.getCorreoElectronico() != null && !isValidEmail(transporterDTO.getCorreoElectronico()))
            throw new IllegalArgumentException("Email format is not valid");
    }

    /**
     * Validates email format using regular expression.
     * Checks if the provided string matches standard email format patterns.
     *
     * @param email the email string to validate
     * @return boolean true if the email format is valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}