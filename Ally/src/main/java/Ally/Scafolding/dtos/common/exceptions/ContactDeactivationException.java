package Ally.Scafolding.dtos.common.exceptions;

import java.io.Serial;

/**
 * Exception thrown to indicate that an error
 * occurred when trying to deactivate a contact.
 * This class extends RuntimeException, allowing it to be used
 * in cases where such errors are encountered.
 */
public class ContactDeactivationException extends RuntimeException {

    /**
     * Serial version UID for this class, used during the deserialization process.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ContactDeactivationException
     * with the specified detail message.
     * @param message the detail message, typically
     *               describing why the contact is invalid
     */
    public ContactDeactivationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ContactDeactivationException
     * with the specified detail message and cause.
     * @param message the detail message,
     * @param cause the cause of the exception, usually
     *              another exception that triggered this one
     */
    public ContactDeactivationException(String message, Throwable cause) {
        super(message, cause);
    }


}
