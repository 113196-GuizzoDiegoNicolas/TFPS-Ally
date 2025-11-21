package Ally.Scafolding.configs;

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

        // =====================================================
        // USERS ENTITY -> DOMAIN MODEL
        // =====================================================
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

        // DOMAIN MODEL -> USERS ENTITY
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

        // =====================================================
        // USERS ENTITY ↔ USER DTO
        // =====================================================
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

        // =====================================================
        // USER CREATE DTO → USERS ENTITY
        // =====================================================
        modelMapper.addMappings(new PropertyMap<Ally.Scafolding.dtos.common.login.UserCreateDTO, UsersEntity>() {
            @Override
            protected void configure() {
                map().setUsuario(source.getUsername());
                map().setEmail(source.getEmail());
                map().setPassword(source.getPassword());
                map().setRol(source.getRole());
            }
        });

        // =====================================================
        // USER DOMAIN ↔ USER DTO
        // =====================================================
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

        // IMPORTANTE:
        // YA NO MAPEAMOS PROVIDERS AQUÍ
        // Ahora el mapeo es 100% manual en ProviderServiceImpl

        return modelMapper;
    }

    // =====================================================
    // MERGER MAPPER (solo copia campos NO nulos)
    // =====================================================
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

        return mapper;
    }

    // =====================================================
    // JACKSON (JavaTimeModule)
    // =====================================================
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
