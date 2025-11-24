package Ally.Scafolding.services;

import Ally.Scafolding.dtos.common.provider.ProviderDTO;
import Ally.Scafolding.dtos.common.provider.ProviderCreateDTO;
import Ally.Scafolding.models.Provider;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Service interface for medical provider management operations.
 * <p>
 *     Handles CRUD operations for medical provider entities.
 * </p>
 */

public interface ProviderService {

    /**
     * Retrieves all medical providers from the system.
     */
    List<ProviderDTO> findAll();

    /**
     * Finds a medical provider by ID.
     */
    ProviderDTO findById(Long id);

    /**
     * Creates a new medical provider.
     */
    Provider create(Provider providerCreate);

    /**
     * Updates an existing medical provider.
     */
    ProviderDTO update(Long id, ProviderDTO providerDTO);

    /**
     * Deletes a medical provider by ID.
     */
    void delete(Long id);

    /**
     * Changes medical provider activation status.
     */
    ProviderDTO changeActivation(Long id, Boolean activo);

    /**
     * Finds medical providers by specialty code.
     */
    List<ProviderDTO> findByEspecialidad(String codigoEspecialidad);

    /**
     * Finds active medical providers by specialty code.
     */
    List<ProviderDTO> findActiveByEspecialidad(String nombreEspecialidad);

    /**
     * Finds medical providers by activation status.
     */
    List<ProviderDTO> findByActivo(Boolean activo);

    /**
     * Finds medical provider by email.
     */
    ProviderDTO findByEmail(String email);

    /**
     * Finds medical provider by associated user ID.
     */
    ProviderDTO findByUsuarioId(Long usuarioId);

    /**
     * Finds medical providers by name or last name containing.
     */
    List<ProviderDTO> findByNombreOrApellidoContaining(String nombre);
}