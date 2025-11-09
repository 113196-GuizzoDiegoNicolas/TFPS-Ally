package Ally.Scafolding.services.impl;

import Ally.Scafolding.dtos.common.login.UserCreateDTO;
import Ally.Scafolding.dtos.common.login.UserDTO;
import Ally.Scafolding.entities.UsersEntity;
import Ally.Scafolding.repositories.UsersRepository;
import Ally.Scafolding.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of user management service.
 * <p>
 *     Handles CRUD operations for user entities.
 * </p>
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    @Qualifier("mergerMapper")
    private ModelMapper mergerMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> findAll() {
        return usersRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(Long id) {
        return usersRepository.findById(id)
                .map(entity -> modelMapper.map(entity, UserDTO.class))
                .orElse(null);
    }

    @Override
    public UserDTO create(UserDTO userDTO) {  // Cambiado de createUser a create
        if (usersRepository.existsByUsuario(userDTO.getUsername())) {
            throw new RuntimeException("El usuario ya existe");
        }

        if (usersRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        UsersEntity entity = modelMapper.map(userDTO, UsersEntity.class);
        // Encriptar la contraseña
        entity.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        UsersEntity saved = usersRepository.save(entity);
        return modelMapper.map(saved, UserDTO.class);
    }


    @Override
    public UserDTO createUser(UserCreateDTO userCreateDTO) {  // Cambiado de createUser a create
        if (usersRepository.existsByUsuario(userCreateDTO.getUsername())) {
            throw new RuntimeException("El usuario ya existe");
        }

        if (usersRepository.existsByEmail(userCreateDTO.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        UsersEntity entity = modelMapper.map(userCreateDTO, UsersEntity.class);
        // Encriptar la contraseña
        entity.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));

        UsersEntity saved = usersRepository.save(entity);
        return modelMapper.map(saved, UserDTO.class);
    }

    @Override
    public UserDTO update(Long id, UserDTO userDTO) {
        return usersRepository.findById(id)
                .map(existing -> {
                    // Usar mergerMapper para actualizar solo campos no nulos
                    mergerMapper.map(userDTO, existing);

                    // Validaciones de unicidad
                    if (userDTO.getUsername() != null &&
                            !existing.getUsuario().equals(userDTO.getUsername()) &&
                            usersRepository.existsByUsuario(userDTO.getUsername())) {
                        throw new RuntimeException("El nombre de usuario ya está en uso");
                    }

                    if (userDTO.getEmail() != null &&
                            !existing.getEmail().equals(userDTO.getEmail()) &&
                            usersRepository.existsByEmail(userDTO.getEmail())) {
                        throw new RuntimeException("El email ya está en uso");
                    }

                    // La contraseña se maneja por separado
                    if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                        existing.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                    }

                    UsersEntity updated = usersRepository.save(existing);
                    return modelMapper.map(updated, UserDTO.class);
                })
                .orElse(null);
    }

    @Override
    public void delete(Long id) {
        usersRepository.deleteById(id);
    }

    @Override
    public UserDTO changeActivation(Long id, Boolean activo) {
        return usersRepository.findById(id)
                .map(usuario -> {
                    usuario.setActivo(activo);
                    UsersEntity updated = usersRepository.save(usuario);
                    return modelMapper.map(updated, UserDTO.class);
                })
                .orElse(null);
    }


}