package Ally.Scafolding.services.impl;

import Ally.Scafolding.dtos.common.provider.ProviderCreateDTO;
import Ally.Scafolding.dtos.common.provider.ProviderDTO;
import Ally.Scafolding.entities.ProvidersEntity;
import Ally.Scafolding.entities.SpecialtyEntity;
import Ally.Scafolding.entities.UsersEntity;
import Ally.Scafolding.repositories.ProvidersRepository;
import Ally.Scafolding.repositories.SpecialtyRepository;
import Ally.Scafolding.repositories.UsersRepository;
import Ally.Scafolding.services.ProviderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of medical provider management service.
 * <p>
 *     Handles CRUD operations for medical provider entities.
 * </p>
 */
@Service
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    private ProvidersRepository providersRepository;

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    @Qualifier("mergerMapper")
    private ModelMapper mergerMapper;

    @Override
    public List<ProviderDTO> findAll() {
        return providersRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, ProviderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProviderDTO findById(Long id) {
        return providersRepository.findById(id)
                .map(entity -> modelMapper.map(entity, ProviderDTO.class))
                .orElse(null);
    }

    @Override
    public ProviderDTO create(ProviderCreateDTO providerCreateDTO) {
        validateProviderCreation(providerCreateDTO);

        //  Buscar usuario
        UsersEntity usuarioEntity = usersRepository.findById(providerCreateDTO.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("El usuario asociado no existe"));

        // Buscar la especialidad por su código o nombre
        SpecialtyEntity especialidad = specialtyRepository.findByCodigo(providerCreateDTO.getCodigoEspecialidad())
                .orElseGet(() -> specialtyRepository.findByNombre(providerCreateDTO.getCodigoEspecialidad())
                        .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada: " + providerCreateDTO.getCodigoEspecialidad()))
                );

        // Crear manualmente el provider
        ProvidersEntity entity = new ProvidersEntity();
        entity.setNombre(providerCreateDTO.getNombre());
        entity.setApellido(providerCreateDTO.getApellido());
        entity.setCorreoElectronico(providerCreateDTO.getEmail());
        entity.setTelefono(providerCreateDTO.getTelefono());
        entity.setDireccion(providerCreateDTO.getDireccion());
        entity.setEspecialidad(especialidad); // ahora sí es una entidad válida
        entity.setActivo(true);
        entity.setUsersEntity(usuarioEntity);

        ProvidersEntity saved = providersRepository.saveAndFlush(entity);
        return modelMapper.map(saved, ProviderDTO.class);
    }


    @Override
    public ProviderDTO update(Long id, ProviderDTO providerDTO) {
        return providersRepository.findById(id)
                .map(existing -> {
                    validateProviderUpdate(providerDTO, existing);

                    // Usar mergerMapper para actualizar solo campos no nulos
                    mergerMapper.map(providerDTO, existing);

                    ProvidersEntity updated = providersRepository.save(existing);
                    return modelMapper.map(updated, ProviderDTO.class);
                })
                .orElse(null);
    }

    @Override
    public void delete(Long id) {
        providersRepository.deleteById(id);
    }

    @Override
    public ProviderDTO changeActivation(Long id, Boolean activo) {
        return providersRepository.findById(id)
                .map(provider -> {
                    provider.setActivo(activo);
                    ProvidersEntity updated = providersRepository.save(provider);
                    return modelMapper.map(updated, ProviderDTO.class);
                })
                .orElse(null);
    }

    @Override
    public List<ProviderDTO> findByEspecialidad(String nombreEspecialidad) {

        SpecialtyEntity especialidad = specialtyRepository.findByNombre(nombreEspecialidad)
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada: " + nombreEspecialidad));

        return providersRepository.findByEspecialidad(especialidad).stream()
                .map(entity -> modelMapper.map(entity, ProviderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProviderDTO> findActiveByEspecialidad(String nombreEspecialidad) {
        // 1. Buscar la especialidad por nombre
        SpecialtyEntity especialidad = specialtyRepository.findByNombre(nombreEspecialidad)
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada: " + nombreEspecialidad));

        // 2. Buscar solo los providers activos de esa especialidad
        return providersRepository.findByEspecialidadAndActivoTrue(especialidad).stream()
                .map(entity -> modelMapper.map(entity, ProviderDTO.class))
                .collect(Collectors.toList());
    }



    @Override
    public ProviderDTO findByEmail(String email) {
        return providersRepository.findByEmail(email)
                .map(entity -> modelMapper.map(entity, ProviderDTO.class))
                .orElse(null);
    }



    @Override
    public ProviderDTO findByUsuarioId(Long usuarioId) {
        return providersRepository.findByUsuarioId(usuarioId)
                .map(entity -> modelMapper.map(entity, ProviderDTO.class))
                .orElse(null);
    }

    @Override
    public List<ProviderDTO> findByNombreOrApellidoContaining(String nombre) {
        return providersRepository.findByNombreOrApellidoContainingIgnoreCase(nombre).stream()
                .map(entity -> modelMapper.map(entity, ProviderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProviderDTO> findByActivo(Boolean activo) {
        return providersRepository.findByActivo(activo).stream()
                .map(entity -> modelMapper.map(entity, ProviderDTO.class))
                .collect(Collectors.toList());
    }

    // VALIDACIONES EN EL SERVICIO
    private void validateProviderCreation(ProviderCreateDTO providerCreateDTO) {
        if (providerCreateDTO.getNombre() == null || providerCreateDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (providerCreateDTO.getApellido() == null || providerCreateDTO.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        if (providerCreateDTO.getEmail() == null || providerCreateDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        if (providerCreateDTO.getCodigoEspecialidad() == null || providerCreateDTO.getCodigoEspecialidad().trim().isEmpty()) {
            throw new IllegalArgumentException("El código de especialidad no puede estar vacío");
        }
        if (providerCreateDTO.getUsuarioId() == null) {
            throw new IllegalArgumentException("El ID de usuario no puede estar vacío");
        }
        if (!isValidEmail(providerCreateDTO.getEmail())) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }
    }

    private void validateProviderUpdate(ProviderDTO providerDTO, ProvidersEntity existing) {
        if (providerDTO.getEmail() != null &&
                !existing.getCorreoElectronico().equals(providerDTO.getEmail()) &&
                providersRepository.existsByEmail(providerDTO.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso por otro proveedor");
        }
        if (providerDTO.getEmail() != null && !isValidEmail(providerDTO.getEmail())) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}