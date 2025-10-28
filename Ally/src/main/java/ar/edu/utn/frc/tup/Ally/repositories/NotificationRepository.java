package ar.edu.utn.frc.tup.lc.iv.repositories;

import ar.edu.utn.frc.tup.lc.iv.entities.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for managing notification entities.
 * <p>
 * This repository provides custom queries for NotificationEntity.
 */
@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long>,
        JpaSpecificationExecutor<NotificationEntity> {

    /**
     * Finds all notifications sent between two specified dates.
     *
     * @param from  the start date and time (inclusive)
     * @param until the end date and time (inclusive)
     * @return a list of notifications within the specified date range
     */
    List<NotificationEntity> findByDateSendBetween(LocalDateTime from, LocalDateTime until);

    /**
     * Finds all notifications for a specific recipient.
     *
     * @param recipient the ID of the recipient
     * @return a list of notifications sent to the specified recipient
     */
    List<NotificationEntity> findByRecipient(String recipient);
}
