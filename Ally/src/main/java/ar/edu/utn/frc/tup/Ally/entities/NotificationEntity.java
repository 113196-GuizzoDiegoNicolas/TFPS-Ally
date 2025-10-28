package ar.edu.utn.frc.tup.lc.iv.entities;
import jakarta.persistence.GenerationType;
import ar.edu.utn.frc.tup.lc.iv.models.StatusSend;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.persistence.EnumType;
/**
 * Represents a notification in the database.
 * <p>
 *     This entity stores its ID as well its name and body.
 * </p>
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
@Audited
public class NotificationEntity {

    /**
     * Unique identifier for the notification.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Recipient of the notification.
     */
    @Column(nullable = false)
    private String recipient;
    /**
     * ContactId recipient of the notification.
     */
    @Column(nullable = true)
    private Long contactId;
    /**
     * Subject of the notification.
     */
    @Column(nullable = false)
    private String subject;
    /**
     * ID of the template used for the notification.
     */
    @Column(name = "template_id")
    private Long templateId;
    /**
     * Name of the template used.
     */
    @Column(name = "template_name", nullable = true)
    private String templateName;
    /**
     * Date and time when the notification was sent.
     */
    @Column(name = "date_send", nullable = false)
    private LocalDateTime dateSend;
    /**
     * Sending status of the notification.
     */
    @Column(name = "status_send", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusSend statusSend;

    /**
     * Body of the notification.
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    private String body;

}
