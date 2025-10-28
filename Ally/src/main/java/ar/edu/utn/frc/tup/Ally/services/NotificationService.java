package ar.edu.utn.frc.tup.lc.iv.services;
import ar.edu.utn.frc.tup.lc.iv.models.EmailNotification;
import ar.edu.utn.frc.tup.lc.iv.models.NotificationFilter;
import ar.edu.utn.frc.tup.lc.iv.models.Notification;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service interface defining the core operations for notification management.
 */
@Service
public interface NotificationService {

    /**
     * @param emailNotification The details of the email used for the notification
     * @return the created Notification object
     */
    Notification createNotification(EmailNotification emailNotification) throws MessagingException;

    /**
     * @param filter        Parameter filters.
     * @return          a list of notifications matching the search criteria.
     */
    List<Notification> getNotifications(NotificationFilter filter);

    /**
     * @param pageable Pageable object with page size and current page
     * @param filters        Parameter filters.
     * @return          a list of notifications matching the search criteria.
     */
    Page<Notification> getNotifications(Pageable pageable, NotificationFilter filters);

    /**
     * @param  id Unique identifier of notification.
     * @return  a Notification with its details.
     */
    Notification getNotification(Long id);

    /**
     * @param id the ID of the viewed notification.
     * @return The notification with its new state.
     */
    Notification viewNotification(Long id);
}
