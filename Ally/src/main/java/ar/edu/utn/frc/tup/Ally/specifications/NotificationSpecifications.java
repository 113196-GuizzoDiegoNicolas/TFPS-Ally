package ar.edu.utn.frc.tup.lc.iv.specifications;

import ar.edu.utn.frc.tup.lc.iv.entities.NotificationEntity;
import ar.edu.utn.frc.tup.lc.iv.models.StatusSend;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Specification class to build filtering criteria for the  entity.
 * Uses Spring Data JPA specifications to apply dynamic filters based on
 * the attributes of {@link NotificationEntity}.
 */
public class NotificationSpecifications {

    /**
     * Filter by the ID of the notification.
     *
     * @param id the unique identifier of the notification to filter by
     * @return a {@link Specification} for the ID filter,
     * or null if {@code id} is null
     */
    public static Specification<NotificationEntity> filterById(Long id) {
        return (root, query, criteriaBuilder) ->
                id != null ? criteriaBuilder.equal(root.get("id"), id) : null;
    }

    /**
     * Filter by the ID of contact.
     *
     * @param contactId the unique identifier of the contact to filter by
     * @return a {@link Specification} for the ID filter,
     * or null if {@code contactId} is null
     */
    public static Specification<NotificationEntity> filterByContactId(Long contactId) {
        return (root, query, criteriaBuilder) ->
                contactId != null ? criteriaBuilder.equal(root.get("contactId"), contactId) : null;
    }

    /**
     * Filter by the viewed status of the notification.
     * @param viewed boolean indicating whether the notification has been viewed.
     * If true, it filters for notifications with {@link StatusSend#VISUALIZED}.
     * If false, it filters for notifications with {@link StatusSend#SENT}.
     * @return a {@link Specification} for the viewed status filter,
     * or null if {@code viewed} is null
     */
    public static Specification<NotificationEntity> filterByViewed(Boolean viewed) {
        return (root, query, criteriaBuilder) ->
                viewed != null ? criteriaBuilder.equal(root.get("statusSend"), viewed ? StatusSend.VISUALIZED : StatusSend.SENT) : null;
    }

    /**
     * Filter by the recipient of the notification.
     * @param recipient the recipient's identifier to filter by
     * @return a {@link Specification} for the recipient filter,
     * or null if {@code recipient} is null
     */
    public static Specification<NotificationEntity> filterByRecipient(String recipient) {
        return (root, query, criteriaBuilder) ->
                recipient != null ? criteriaBuilder.like(root.get("recipient"), "%" + recipient + "%") : null;
    }

    /**
     * Filter by the date range for when the notification was sent.
     * @param from  the starting date and time for the filter range
     * @param until the ending date and time for the filter range
     * @return a {@link Specification} that filters notifications
     * sent between {@code from} and {@code until},
     *         or null if either {@code from} or {@code until} is null
     */
    public static Specification<NotificationEntity> filterByDateRange(LocalDateTime from, LocalDateTime until) {
        return (root, query, criteriaBuilder) -> {
            if (from != null && until != null) {
                return criteriaBuilder.between(root.get("dateSend"), from, until);
            } else {
                return null;
            }
        };
    }

    /**
     * Filter by the subject of the notifications.
     * @param subject the subject name to filter by
     * @return a {@link Specification} for the recipient filter,
     * or null if {@code subject} is null
     */
    public static Specification<NotificationEntity> filterBySubject(String subject) {
        return (root, query, criteriaBuilder) ->
                subject != null ? criteriaBuilder.like(root.get("subject"), "%" + subject + "%") : null;
    }

    /**
     * Global filter for notifications.
     * @param searchTerm text to filter by
     * @return a {@link Specification} for the recipient filter,
     * or null if {@code subject} is null
     */
    public static Specification<NotificationEntity> filterBySearchTerm(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String searchPattern = "%" + searchTerm.toLowerCase(Locale.ROOT) + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("recipient")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("subject")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("templateName")), searchPattern)
            );
        };
    }


}
