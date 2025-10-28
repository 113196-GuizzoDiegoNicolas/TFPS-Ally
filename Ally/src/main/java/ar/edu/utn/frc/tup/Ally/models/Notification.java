package ar.edu.utn.frc.tup.lc.iv.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Notification class.
 * <p>
 * This class represents a notification with its attributes.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    /**
     * The unique identifier of the notification.
     */
    private Long id;

    /**
     * The identifier of the recipient who will receive the notification.
     */
    private String recipient;
    /**
     * The identifier of the contact who will receive the notification.
     */
    private Long contactId;

    /**
     * The details of the email used for the notification.
     */
    private EmailNotification emailNotification;
    /**
     * The date and time when the notification was sent.
     */
    private String dateSend;

    /**
     * The current status of the notification (e.g., SENT, PENDING, etc.).
     */
    private StatusSend statusSend;

    /**
     * The date and time when the notification was created.
     */
    private LocalDateTime createdDate;

    /**
     * The body of the email.
     */
    private String body;
}
