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
     * Finds providers by specialty code (String) - CORREGIDO
     */
    List<ProvidersEntity> findByEspecialidad(SpecialtyEntity especialidad);

    /**
     * Finds active providers by specialty code
     */
    List<ProvidersEntity> findByEspecialidadAndActivoTrue(SpecialtyEntity especialidad);

    /**
     * Finds providers by activation status
     */
    List<ProvidersEntity> findByActivo(Boolean activo);

    /**
     * Finds active providers
     */
    List<ProvidersEntity> findByActivoTrue();

    /**
     * Finds providers by email
     */
    @Query("SELECT p FROM ProvidersEntity p WHERE p.correoElectronico = :email")
    Optional<ProvidersEntity> findByEmail(@Param("email") String email);

    /**
     * Checks if email exists
     */
    @Query("SELECT COUNT(p) > 0 FROM ProvidersEntity p WHERE p.correoElectronico = :email")
    boolean existsByEmail(@Param("email") String email);

    /**
     * Finds provider by associated user ID
     */
    @Query("SELECT p FROM ProvidersEntity p WHERE p.usersEntity.id = :usuarioId")
    Optional<ProvidersEntity> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Finds providers by name or last name containing (case insensitive)
     */
    @Query("SELECT p FROM ProvidersEntity p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) OR LOWER(p.apellido) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<ProvidersEntity> findByNombreOrApellidoContainingIgnoreCase(@Param("nombre") String nombre);
}