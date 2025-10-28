package ar.edu.utn.frc.tup.lc.iv.dtos.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Data Transfer Object (DTO) for detailed information about a notification.
 * Extends {@link GetNotificationDTO} and adds the body of the email message.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetNotificationDetailsDTO extends GetNotificationDTO {

    /**
     * The body of the email message.
     */
    private String body;

}
