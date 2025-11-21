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

    // =======================================================
    // MAPPER MANUAL PARA ELIMINAR EL MappingException
    // =======================================================
    private ProviderDTO toDTO(ProvidersEntity entity) {
        if (entity == null) return null;

        ProviderDTO dto = new ProviderDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setEmail(entity.getCorreoElectronico());
        dto.setTelefono(entity.getTelefono());
        dto.setDireccion(entity.getDireccion());
        dto.setActivo(entity.getActivo());

        if (entity.getEspecialidad() != null) {
            dto.setCodigoEspecialidad(entity.getEspecialidad().getCodigo());
        }

        if (entity.getUsersEntity() != null) {
            dto.setUsuarioId(entity.getUsersEntity().getId());
            dto.setNombreUsuario(entity.getUsersEntity().getUsuario());
        }

        return dto;
    }

    // =======================================================
    // MÉTODOS DEL SERVICIO
    // =======================================================

    @Override
    public List<ProviderDTO> findAll() {
        return providersRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProviderDTO findById(Long id) {
        return providersRepository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    @Override
    public ProviderDTO create(ProviderCreateDTO providerCreateDTO) {
        validateProviderCreation(providerCreateDTO);

        UsersEntity usuarioEntity = usersRepository.findById(providerCreateDTO.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("El usuario asociado no existe"));

        SpecialtyEntity especialidad = specialtyRepository.findByCodigo(providerCreateDTO.getCodigoEspecialidad())
                .orElseGet(() -> specialtyRepository.findByNombre(providerCreateDTO.getCodigoEspecialidad())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Especialidad no encontrada: " + providerCreateDTO.getCodigoEspecialidad()))
                );

        ProvidersEntity entity = new ProvidersEntity();
        entity.setNombre(providerCreateDTO.getNombre());
        entity.setApellido(providerCreateDTO.getApellido());
        entity.setCorreoElectronico(providerCreateDTO.getEmail());
        entity.setTelefono(providerCreateDTO.getTelefono());
        entity.setDireccion(providerCreateDTO.getDireccion());
        entity.setEspecialidad(especialidad);
        entity.setActivo(true);
        entity.setUsersEntity(usuarioEntity);

        ProvidersEntity saved = providersRepository.saveAndFlush(entity);
        return toDTO(saved);
    }

    @Override
    public ProviderDTO update(Long id, ProviderDTO providerDTO) {
        return providersRepository.findById(id)
                .map(existing -> {
                    validateProviderUpdate(providerDTO, existing);
                    mergerMapper.map(providerDTO, existing);
                    ProvidersEntity updated = providersRepository.save(existing);
                    return toDTO(updated);
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
                    return toDTO(updated);
                })
                .orElse(null);
    }

    @Override
    public List<ProviderDTO> findByEspecialidad(String nombreEspecialidad) {
        SpecialtyEntity especialidad = specialtyRepository.findByNombre(nombreEspecialidad)
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada: " + nombreEspecialidad));

        return providersRepository.findByEspecialidad(especialidad).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProviderDTO> findActiveByEspecialidad(String nombreEspecialidad) {
        SpecialtyEntity especialidad = specialtyRepository.findByNombre(nombreEspecialidad)
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada: " + nombreEspecialidad));

        return providersRepository.findByEspecialidadAndActivoTrue(especialidad).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProviderDTO findByEmail(String email) {
        return providersRepository.findByEmail(email)
                .map(this::toDTO)
                .orElse(null);
    }

    @Override
    public ProviderDTO findByUsuarioId(Long usuarioId) {
        return providersRepository.findByUsuarioId(usuarioId)
                .map(this::toDTO)
                .orElse(null);
    }

    @Override
    public List<ProviderDTO> findByNombreOrApellidoContaining(String nombre) {
        return providersRepository.findByNombreOrApellidoContainingIgnoreCase(nombre).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProviderDTO> findByActivo(Boolean activo) {
        return providersRepository.findByActivo(activo).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // =======================================================
    // VALIDACIONES
    // =======================================================

    private void validateProviderCreation(ProviderCreateDTO providerCreateDTO) {
        if (providerCreateDTO.getNombre() == null || providerCreateDTO.getNombre().trim().isEmpty())
            throw new IllegalArgumentException("El nombre no puede estar vacío");

        if (providerCreateDTO.getApellido() == null || providerCreateDTO.getApellido().trim().isEmpty())
            throw new IllegalArgumentException("El apellido no puede estar vacío");

        if (providerCreateDTO.getEmail() == null || providerCreateDTO.getEmail().trim().isEmpty())
            throw new IllegalArgumentException("El email no puede estar vacío");

        if (providerCreateDTO.getCodigoEspecialidad() == null || providerCreateDTO.getCodigoEspecialidad().trim().isEmpty())
            throw new IllegalArgumentException("El código de especialidad no puede estar vacío");

        if (providerCreateDTO.getUsuarioId() == null)
            throw new IllegalArgumentException("El ID de usuario no puede estar vacío");

        if (!isValidEmail(providerCreateDTO.getEmail()))
            throw new IllegalArgumentException("El formato del email no es válido");
    }

    private void validateProviderUpdate(ProviderDTO providerDTO, ProvidersEntity existing) {
        if (providerDTO.getEmail() != null &&
                !existing.getCorreoElectronico().equals(providerDTO.getEmail()) &&
                providersRepository.existsByEmail(providerDTO.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso por otro proveedor");
        }

        if (providerDTO.getEmail() != null && !isValidEmail(providerDTO.getEmail()))
            throw new IllegalArgumentException("El formato del email no es válido");
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}