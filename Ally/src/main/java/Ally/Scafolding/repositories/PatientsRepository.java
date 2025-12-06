package Ally.Scafolding.repositories;

import Ally.Scafolding.entities.PatientsEntity;
import Ally.Scafolding.entities.ProvidersEntity;
import Ally.Scafolding.entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link PatientsEntity} entities.
 * <p>
 * Extends {@link JpaRepository} to provide CRUD operations for patients.
 * </p>
 */
@Repository
public interface PatientsRepository extends JpaRepository<PatientsEntity, Long>,
        JpaSpecificationExecutor<PatientsEntity> {

    /**
     * Finds a patient by their medical record number.
     * @param numeroHistoriaClinica the medical record number to search for
     * @return an {@link Optional} containing the found {@link PatientsEntity}, or empty if not found
     */
    Optional<PatientsEntity> findByNumeroHistoriaClinica(String numeroHistoriaClinica);

    /**
     * Finds patients whose last name contains the given value (case insensitive).
     * @param apellido the last name to search for
     * @return a list of patients matching the condition
     */
    List<PatientsEntity> findByApellidoContainingIgnoreCase(String apellido);

    /**
     * Finds patients whose first name contains the given value (case insensitive).
     * @param nombre the first name to search for
     * @return a list of patients matching the condition
     */
    List<PatientsEntity> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Finds a patient by their social security affiliate number.
     * @param nroAfiliadoObraSocial the affiliate number to search for
     * @return an {@link Optional} containing the found {@link PatientsEntity}, or empty if not found
     */
    Optional<PatientsEntity> findByNroAfiliadoObraSocial(String nroAfiliadoObraSocial);

    /**
     * Finds patients by their social security code.
     * @param codigoObraSocial the social security code to search for
     * @return a list of patients matching the condition
     */
    List<PatientsEntity> findByCodigoObraSocial(String codigoObraSocial);
    Optional<PatientsEntity> findByUsersEntityId(Long usuarioId);


}