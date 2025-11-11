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
@Service
public interface UserService {

    /**
     * Retrieves all users from the system.
     * @return list of all users
     */
    List<UserDTO> findAll();

    /**
     * Finds a user by ID.
     * @param id the user ID
     * @return UserDTO if found, null otherwise
     */
    UserDTO findById(Long id);

    /**
     * Creates a new user from UserDTO.
     * @param userDTO the user data
     * @return the created user
     */
    UserDTO create(UserDTO userDTO);

    /**
     * Creates a new user from UserCreateDTO.
     * @param userCreateDTO the user creation data
     * @return the created user
     */
    UserDTO createUser(UserCreateDTO userCreateDTO);

    /**
     * Updates an existing user.
     * @param id the user ID
     * @param userDTO the updated user data
     * @return the updated user
     */
    UserDTO update(Long id, UserDTO userDTO);

    /**
     * Deletes a user by ID.
     * @param id the user ID to delete
     */
    void delete(Long id);

    /**
     * Changes user activation status.
     * @param id the user ID
     * @param activo the new activation status
     * @return the updated user
     */
    UserDTO changeActivation(Long id, Boolean activo);
}