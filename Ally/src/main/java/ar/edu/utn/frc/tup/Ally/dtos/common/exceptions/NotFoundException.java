package ar.edu.utn.frc.tup.lc.iv.dtos.common.exceptions;

import java.io.Serial;

/**
 * Custom exception to be thrown when a requested resource is not found.
 * Extends {@link RuntimeException} so it is unchecked and doesn't require
 * explicit handling.
 */
public class NotFoundException extends RuntimeException {

    /**
     * Serial version UID for this class, used during the deserialization process.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new NotFoundException with the specified detail message.
     *
     * @param message the detail message, typically describing the resource not found
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new NotFoundException with the specified detail message and cause.
     *
     * @param message the detail message, typically describing the resource not found
     * @param cause the cause of the exception
     */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
