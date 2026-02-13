package Ally.Scafolding.services;

import Ally.Scafolding.dtos.common.login.UserCreateDTO;
import Ally.Scafolding.dtos.common.login.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service interface for user management operations.
 * <p>
 *     Handles CRUD operations for user entities.
 * </p>
 */

public interface UserService {
    List<UserDTO> findAll();
    UserDTO findById(Long id);
    UserDTO createUser(UserCreateDTO userCreateDTO);
    UserDTO update(Long id, UserDTO userDTO);
    void delete(Long id);
    UserDTO changeActivation(Long id, Boolean activo);
}
