package ar.edu.utn.frc.tup.lc.iv.dtos.common.exceptions;

import java.io.Serial;

/**
 * Custom exception to be thrown when a requested
 * client does not respond
 * Extends {@link RuntimeException} so it is unchecked and doesn't require
 * explicit handling.
 */
public class ClientNotAvailableException extends RuntimeException {

    /**
     * Serial version UID for this class, used during the deserialization process.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ClientNotAvailableException with the specified detail message.
     *
     * @param message the detail message, typically describing the resource failing
     */
    public ClientNotAvailableException(String message) {
        super(message);
    }
}
