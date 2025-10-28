package ar.edu.utn.frc.tup.lc.iv.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * EmailNotificationDetails class.
 * <p>
 * This class represents the detailed information
 * of the email used in a notification,
 * including the subject, template ID, and template name.
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EmailNotification {

    /**
     * The receiver of the notification.
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
     * The contact identifier.
     */
    private Long contactId;

    /**
     * The body of the notification.
     */
    private String body;

}
