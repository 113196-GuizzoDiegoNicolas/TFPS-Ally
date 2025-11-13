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

@Configuration
public class MappersConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // USERS ↔ MODELS
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

        // USERS ↔ DTOs
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

        // USERCREATE DTO → ENTITY
        modelMapper.addMappings(new PropertyMap<Ally.Scafolding.dtos.common.login.UserCreateDTO, UsersEntity>() {
            @Override
            protected void configure() {
                map().setUsuario(source.getUsername());
                map().setEmail(source.getEmail());
                map().setPassword(source.getPassword());
                map().setRol(source.getRole());
            }
        });

        // USER ↔ DTO
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

             // FIX: PROVIDERSENTITY → PROVIDERDTO (usando lambdas, sin ifs)
        modelMapper.typeMap(ProvidersEntity.class, Ally.Scafolding.dtos.common.provider.ProviderDTO.class)
                .addMappings(mapper -> {
                    mapper.map(ProvidersEntity::getId, Ally.Scafolding.dtos.common.provider.ProviderDTO::setId);
                    mapper.map(ProvidersEntity::getNombre, Ally.Scafolding.dtos.common.provider.ProviderDTO::setNombre);
                    mapper.map(ProvidersEntity::getApellido, Ally.Scafolding.dtos.common.provider.ProviderDTO::setApellido);
                    mapper.map(ProvidersEntity::getCorreoElectronico, Ally.Scafolding.dtos.common.provider.ProviderDTO::setEmail);
                    mapper.map(ProvidersEntity::getTelefono, Ally.Scafolding.dtos.common.provider.ProviderDTO::setTelefono);
                    mapper.map(ProvidersEntity::getDireccion, Ally.Scafolding.dtos.common.provider.ProviderDTO::setDireccion);
                    mapper.map(ProvidersEntity::getActivo, Ally.Scafolding.dtos.common.provider.ProviderDTO::setActivo);

                    mapper.map(src -> src.getUsersEntity() != null ? src.getUsersEntity().getId() : null,
                            Ally.Scafolding.dtos.common.provider.ProviderDTO::setUsuarioId);

                    mapper.map(src -> src.getUsersEntity() != null ? src.getUsersEntity().getUsuario() : null,
                            Ally.Scafolding.dtos.common.provider.ProviderDTO::setNombreUsuario);

                    mapper.map(src -> src.getEspecialidad() != null ? src.getEspecialidad().getCodigo() : null,
                            Ally.Scafolding.dtos.common.provider.ProviderDTO::setCodigoEspecialidad);
                });

        // PROVIDER DTOs → ENTITY
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

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
