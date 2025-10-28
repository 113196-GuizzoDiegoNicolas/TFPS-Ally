package Ally.Scafolding.dtos.common.exceptions;

import java.io.Serial;

/**
 * Exception thrown to indicate that a contact is invalid.
 * This class extends RuntimeException, allowing it to be used
 * in cases where invalid contacts are encountered.
 */
public class InvalidPatientException extends RuntimeException {

    /**
     * Serial version UID for this class, used during the deserialization process.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new InvalidContactException with the specified detail message.
     *
     * @param message the detail message, typically
     *               describing why the contact is invalid
     */
    public InvalidPatientException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidContactException
     * with the specified detail message and cause.
     * @param message the detail message,
     *                typically describing why the contact is invalid
     * @param cause the cause of the exception, usually
     *              another exception that triggered this one
     */
    public InvalidPatientException(String message, Throwable cause) {
        super(message, cause);
    }
}
