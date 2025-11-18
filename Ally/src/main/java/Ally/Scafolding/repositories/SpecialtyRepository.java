package Ally.Scafolding.repositories;

import Ally.Scafolding.entities.ProvidersEntity;
import Ally.Scafolding.entities.SpecialtyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SpecialtyRepository extends JpaRepository<SpecialtyEntity, Long>,
        JpaSpecificationExecutor<SpecialtyEntity> {


    /**
     * Finds a specialty by its exact name.
     */
    Optional<SpecialtyEntity> findByNombre(String nombre);

    /**
     * Finds specialties by name (case-insensitive containing).
     */
    List<SpecialtyEntity> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Finds a specialty by exact name match (case-insensitive).
     */
    Optional<SpecialtyEntity> findByNombreIgnoreCase(String nombre);

    /**
     * Checks if a specialty exists with the given name.
     */
    boolean existsByNombre(String nombre);

    /**
     * Checks if a specialty exists with the given name (case-insensitive).
     */
    boolean existsByNombreIgnoreCase(String nombre);

    /**
     * Custom query to find specialties with a specific pattern in the name.
     */
    @Query("SELECT s FROM SpecialtyEntity s WHERE s.nombre LIKE %:nombrePattern%")
    List<SpecialtyEntity> findByNombreContaining(@Param("nombrePattern") String nombrePattern);

    /**
     * Counts the number of providers associated with a specialty.
     */
    @Query("SELECT COUNT(p) FROM ProvidersEntity p WHERE p.especialidad.id = :especialidadId")
    Long countProvidersByEspecialidad(@Param("especialidadId") Long especialidadId);
}