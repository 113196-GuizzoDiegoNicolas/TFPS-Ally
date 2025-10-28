package ar.edu.utn.frc.tup.lc.iv.dtos.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing the details of a notification.
 * <p>
 * This class is used to transfer notification data between different layers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetNotificationDTO {

    /**
     * The unique identifier of the notification.
     */
    private Long id;

    /**
     * The identifier of the recipient who will receive the notification.
     */
    private String recipient;

    /**
     * The subject or title of the notification.
     */
    private String subject;

    /**
     * The identifier of the template used for the notification.
     */
    private Long templateId;

    /**
     * The name of the template used for the notification.
     */
    private String templateName;

    /**
     * The date and time when the notification was sent.
     */
    private String dateSend;

    /**
     * The current status of the notification (e.g., "SENT", "PENDING").
     */
    private String statusSend;

}
