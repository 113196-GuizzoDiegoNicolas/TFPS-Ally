package ar.edu.utn.frc.tup.lc.iv.models;

/**
 * Enum representing the possible status of a notification send operation.
 * Used to track the delivery and visualization state of notifications.
 */
public enum StatusSend {

    /**
     * Indicates that the notification has been successfully sent.
     */
    SENT,
    /**
     *
     * Indicates that the notification has been viewed by the recipient.
     */
    VISUALIZED
}
