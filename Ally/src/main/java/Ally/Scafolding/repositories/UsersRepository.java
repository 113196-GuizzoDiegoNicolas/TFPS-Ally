package Ally.Scafolding.repositories;

import Ally.Scafolding.entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for User entities.
 * Provides data access methods for user authentication and management.
 */
@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Long>,
        JpaSpecificationExecutor<UsersEntity> {

    /**
     * Find a user by username.
     * @param usuario the username to search for
     */
    Optional<UsersEntity> findByUsuario(String usuario);

    /**
     * Check if a username already exists.
     * @param usuario the username to check
     */
    boolean existsByUsuario(String usuario);

    /**
     * Find a user by email (used for login).
     * @param email the email to search for
     */
    Optional<UsersEntity> findByEmail(String email);

    /**
     * Check if an email already exists.
     * @param email the email to check
     */
    boolean existsByEmail(String email);

    /**
     * Find active users by role.
     * @param rol the role to search for
     */
    List<UsersEntity> findByRolAndActivoTrue(String rol);

    long countByRol(String rol); // <-- Agregado
    Optional<UsersEntity> findByEmailIgnoreCase(String email);
}
