package Ally.Scafolding.configs;

import Ally.Scafolding.entities.ProvidersEntity;
import Ally.Scafolding.entities.UsersEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ModelMapper and ObjectMapper configuration class.
 */
@Configuration
public class MappersConfig {

    /**
     * The ModelMapper bean by default.
     * @return the ModelMapper by default.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Configuración para mapeo entre UsersEntity y User (model)
        modelMapper.addMappings(new PropertyMap<UsersEntity, Ally.Scafolding.models.User>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setUsername(source.getUsuario());
                map().setPassword(source.getPassword());
                map().setEmail(source.getEmail());
                map().setRole(source.getRol());
                map().setActive(source.getActivo());
                map().setLocked(source.getBloqueado());
                map().setCreatedAt(source.getFechaCreacion());
                map().setLastLogin(source.getUltimoLogin());
                map().setFailedAttempts(source.getIntentosFallidos());
            }
        });

        modelMapper.addMappings(new PropertyMap<Ally.Scafolding.models.User, UsersEntity>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setUsuario(source.getUsername());
                map().setPassword(source.getPassword());
                map().setEmail(source.getEmail());
                map().setRol(source.getRole());
                map().setActivo(source.isActive());
                map().setBloqueado(source.isLocked());
                map().setFechaCreacion(source.getCreatedAt());
                map().setUltimoLogin(source.getLastLogin());
                map().setIntentosFallidos(source.getFailedAttempts());
            }
        });

        // Configuración para mapeo entre UsersEntity y UserDTO
        modelMapper.addMappings(new PropertyMap<UsersEntity, Ally.Scafolding.dtos.common.login.UserDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setUsername(source.getUsuario());
                map().setEmail(source.getEmail());
                map().setRole(source.getRol());
                map().setActive(source.getActivo());
                map().setLocked(source.getBloqueado());
            }
        });

        modelMapper.addMappings(new PropertyMap<Ally.Scafolding.dtos.common.login.UserDTO, UsersEntity>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setUsuario(source.getUsername());
                map().setEmail(source.getEmail());
                map().setRol(source.getRole());
                map().setActivo(source.isActive());
                map().setBloqueado(source.isLocked());
            }
        });

        // ✅ NUEVO: Configuración para mapeo entre UserCreateDTO y UsersEntity
        modelMapper.addMappings(new PropertyMap<Ally.Scafolding.dtos.common.login.UserCreateDTO, UsersEntity>() {
            @Override
            protected void configure() {
                map().setUsuario(source.getUsername());
                map().setEmail(source.getEmail());
                map().setPassword(source.getPassword());
                map().setRol(source.getRole()); // 👈 clave: role → rol
            }
        });

        // Configuración para mapeo entre User (model) y UserDTO
        modelMapper.addMappings(new PropertyMap<Ally.Scafolding.models.User, Ally.Scafolding.dtos.common.login.UserDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setUsername(source.getUsername());
                map().setEmail(source.getEmail());
                map().setRole(source.getRole());
                map().setActive(source.isActive());
                map().setLocked(source.isLocked());
            }
        });

        modelMapper.addMappings(new PropertyMap<Ally.Scafolding.dtos.common.login.UserDTO, Ally.Scafolding.models.User>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setUsername(source.getUsername());
                map().setEmail(source.getEmail());
                map().setRole(source.getRole());
                map().setActive(source.isActive());
                map().setLocked(source.isLocked());
            }
        });

        // CONFIGURACIONES PARA PROVIDERS - CORREGIDAS
        modelMapper.addMappings(new PropertyMap<ProvidersEntity, Ally.Scafolding.dtos.common.provider.ProviderDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setNombre(source.getNombre());
                map().setApellido(source.getApellido());
                map().setEmail(source.getCorreoElectronico());
                map().setTelefono(source.getTelefono());
                map().setDireccion(source.getDireccion());

                if (source.getEspecialidad() != null && source.getEspecialidad().getId() != null) {
                    map().setCodigoEspecialidad(source.getEspecialidad().getId().toString());
                }

                map().setActivo(source.getActivo());

                if (source.getUsersEntity() != null) {
                    map().setUsuarioId(source.getUsersEntity().getId());
                    map().setNombreUsuario(source.getUsersEntity().getUsuario());
                }
            }
        });

        modelMapper.addMappings(new PropertyMap<Ally.Scafolding.dtos.common.provider.ProviderCreateDTO, ProvidersEntity>() {
            @Override
            protected void configure() {
                map().setNombre(source.getNombre());
                map().setApellido(source.getApellido());
                map().setCorreoElectronico(source.getEmail());
                map().setTelefono(source.getTelefono());
                map().setDireccion(source.getDireccion());
            }
        });

        modelMapper.addMappings(new PropertyMap<Ally.Scafolding.dtos.common.provider.ProviderDTO, ProvidersEntity>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setNombre(source.getNombre());
                map().setApellido(source.getApellido());
                map().setCorreoElectronico(source.getEmail());
                map().setTelefono(source.getTelefono());
                map().setDireccion(source.getDireccion());
                map().setActivo(source.getActivo());
            }
        });

        return modelMapper;
    }

    /**
     * The ModelMapper bean to merge objects.
     * @return the ModelMapper to use in updates.
     */
    @Bean("mergerMapper")
    public ModelMapper mergerMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        mapper.addMappings(new PropertyMap<Ally.Scafolding.dtos.common.login.UserDTO, UsersEntity>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setUsuario(source.getUsername());
                map().setEmail(source.getEmail());
                map().setRol(source.getRole());
                map().setActivo(source.isActive());
                map().setBloqueado(source.isLocked());
            }
        });

        mapper.addMappings(new PropertyMap<Ally.Scafolding.dtos.common.provider.ProviderDTO, ProvidersEntity>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setNombre(source.getNombre());
                map().setApellido(source.getApellido());
                map().setCorreoElectronico(source.getEmail());
                map().setTelefono(source.getTelefono());
                map().setDireccion(source.getDireccion());
                map().setActivo(source.getActivo());
            }
        });

        return mapper;
    }

    /**
     * The ObjectMapper bean.
     * @return the ObjectMapper with JavaTimeModule included.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
