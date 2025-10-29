package Ally.Scafolding.repositories;

import Ally.Scafolding.entities.ProvidersEntity;
import Ally.Scafolding.entities.SpecialtyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link ProvidersEntity} entities.
 * <p>
 * Extends {@link JpaRepository} to provide CRUD operations for providers.
 * </p>
 */
@Repository
public interface ProvidersRepository extends JpaRepository<ProvidersEntity, Long>,
        JpaSpecificationExecutor<ProvidersEntity> {

    /**
     * Finds providers whose last name contains the given value (case insensitive).
     * @param apellido the last name to search for
     * @return a list of providers matching the condition
     */
    List<ProvidersEntity> findByApellidoContainingIgnoreCase(String apellido);

    /**
     * Finds providers whose first name contains the given value (case insensitive).
     * @param nombre the first name to search for
     * @return a list of providers matching the condition
     */
    List<ProvidersEntity> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Finds providers by specialty.
     * @param specialty the specialty to search for
     * @return a list of providers matching the condition
     */
    List<ProvidersEntity> findBySpecialty(SpecialtyEntity specialty);

    /**
     * Finds providers by status.
     * @param estado the status to search for
     * @return a list of providers matching the condition
     */
    List<ProvidersEntity> findByEstado(String estado);
}