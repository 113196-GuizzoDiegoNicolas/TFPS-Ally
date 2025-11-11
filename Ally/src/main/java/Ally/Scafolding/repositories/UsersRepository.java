package Ally.Scafolding.repositories;


import Ally.Scafolding.entities.UsersEntity;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for User entities.
 * <p>
 *     Provides data access methods for user authentication and management.
 * </p>
 */
@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Long>,
        JpaSpecificationExecutor<UsersEntity> {


    /**
     * Find a user by username.
     * @param usuario the username to search for
     * @return an Optional containing the user if found
     */
    Optional<UsersEntity> findByUsuario(String usuario);

    /**
     * Check if a username exists in the database.
     * @param usuario the username to check
     * @return true if the username exists, false otherwise
     */
    boolean existsByUsuario(String usuario);

    /**
     * Find active users by role.
     * @param rol the role to search for
     * @return list of active users with the specified role
     */
    List<UsersEntity> findByRolAndActivoTrue(String rol);

    boolean existsByEmail(String email);
    // impl.login-10-11
    Optional<UsersEntity> findByEmail(String email);

}