package Ally.Scafolding.repositories;

import Ally.Scafolding.entities.SpecialtyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialtyRepository extends JpaRepository<SpecialtyEntity, Long> {

    /**
     * Busca una especialidad por su código único.
     */
    Optional<SpecialtyEntity> findByCodigo(String codigo);

    /**
     * Busca una especialidad por su nombre exacto.
     */
    Optional<SpecialtyEntity> findByNombre(String nombre);

    /**
     * Busca especialidades que contengan un texto en su nombre (ignorando mayúsculas).
     */
    List<SpecialtyEntity> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Verifica si existe una especialidad con ese nombre.
     */
    boolean existsByNombreIgnoreCase(String nombre);

    /**
     * Cuenta cuántos proveedores están asociados a una especialidad.
     */
    @Query("SELECT COUNT(p) FROM ProvidersEntity p WHERE p.especialidad.id = :especialidadId")
    Long countProvidersByEspecialidad(@Param("especialidadId") Long especialidadId);
}
