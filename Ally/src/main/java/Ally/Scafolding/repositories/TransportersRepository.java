package Ally.Scafolding.repositories;

import Ally.Scafolding.entities.TransportersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Transporter entity operations.
 * Provides data access methods for transporter management.
 */
@Repository
public interface TransportersRepository extends JpaRepository<TransportersEntity, Long> {

    /**
     * Finds transporters by active status.
     * @param activo Active status (true/false)
     * @return List of transporters with the specified status
     */
    List<TransportersEntity> findByActivo(Boolean activo);

    /**
     * Finds transporters by coverage zone (case-insensitive contains search).
     * @param zonaCobertura Coverage zone to search for
     * @return List of transporters matching the coverage zone
     */
    List<TransportersEntity> findByZonaCoberturaContainingIgnoreCase(String zonaCobertura);

    /**
     * Finds active transporters by coverage zone.
     * @param zonaCobertura Coverage zone to search for
     * @param activo Active status
     * @return List of active transporters in the specified coverage zone
     */
    List<TransportersEntity> findByZonaCoberturaContainingIgnoreCaseAndActivo(String zonaCobertura, Boolean activo);

    /**
     * Search transporters by first name or last name (case-insensitive).
     * @param nombre Name to search for in first name or last name
     * @return List of transporters matching the name criteria
     */
    @Query("SELECT t FROM TransportersEntity t WHERE LOWER(t.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) OR LOWER(t.apellido) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<TransportersEntity> findByNombreOrApellidoContainingIgnoreCase(@Param("nombre") String nombre);

    /**
     * Finds a transporter by user ID.
     * @param usuarioId User ID to search for
     * @return Transporter entity if found
     */
    Optional<TransportersEntity> findByUsersEntityId(Long usuarioId);

    /**
     * Finds a transporter by email address.
     * @param correoElectronico Email to search for
     * @return Transporter entity if found
     */
    Optional<TransportersEntity> findByCorreoElectronico(String correoElectronico);

    /**
     * Checks if a transporter exists with the specified user ID.
     * @param usuarioId User ID to check
     * @return True if a transporter exists with the user ID, false otherwise
     */
    boolean existsByUsersEntityId(Long usuarioId);

    /**
     * Checks if a transporter exists with the specified email.
     * @param correoElectronico Email to check
     * @return True if a transporter exists with the email, false otherwise
     */
    boolean existsByCorreoElectronico(String correoElectronico);
}