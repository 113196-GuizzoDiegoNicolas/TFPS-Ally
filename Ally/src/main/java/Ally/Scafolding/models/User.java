
package Ally.Scafolding.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * Represents a user in the business domain.
 * <p>
 *     This model class contains the business logic and domain rules for users.
 *     It serves as the core domain entity before persistence.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String role; // PACIENTE, PRESTADOR, TRANSPORTISTA, ADMIN
    private boolean active;
    private boolean locked;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private int failedAttempts;


    // Business Logic Methods
    /**
     * Checks if the user can login.
     * @return true if the user is active and not locked
     */
    public boolean canLogin() {
        return this.active && !this.locked;
    }

    /**
     * Updates the last login timestamp.
     * Resets failed login attempts.
     */
    public void registerSuccessfulLogin() {
        this.lastLogin = LocalDateTime.now();
        this.failedAttempts = 0;
    }

    /**
     * Increments failed login attempts.
     * Locks account after maximum attempts.
     */
    public void registerFailedAttempt() {
        this.failedAttempts++;
        if (this.failedAttempts >= 5) { // Maximum 5 attempts
            this.locked = true;
        }
    }

    /**
     * Unlocks the user account and resets failed attempts.
     */
    public void unlockAccount() {
        this.locked = false;
        this.failedAttempts = 0;
    }

    /**
     * Validates if the user data is correct.
     * @throws IllegalArgumentException if validation fails
     */
    public void validate() {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    /**
     * Basic email validation.
     * @param email the email to validate
     * @return true if the email is valid
     */
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", active=" + active +
                ", locked=" + locked +
                '}';
    }
}