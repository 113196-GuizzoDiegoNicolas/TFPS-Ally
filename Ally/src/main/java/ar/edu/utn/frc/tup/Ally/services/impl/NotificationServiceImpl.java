package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.exceptions.NotFoundException;
import ar.edu.utn.frc.tup.lc.iv.entities.NotificationEntity;
import ar.edu.utn.frc.tup.lc.iv.models.EmailNotification;
import ar.edu.utn.frc.tup.lc.iv.models.NotificationFilter;
import ar.edu.utn.frc.tup.lc.iv.models.StatusSend;
import ar.edu.utn.frc.tup.lc.iv.models.Notification;
import ar.edu.utn.frc.tup.lc.iv.repositories.NotificationRepository;
import ar.edu.utn.frc.tup.lc.iv.services.NotificationService;
import ar.edu.utn.frc.tup.lc.iv.specifications.NotificationSpecifications;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation class for managing notification operations.
 * Handles notification creation, retrieval, and date-based queries.
 */
@Service
@NoArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    /**
     * Repository for accessing notification data.
     */
    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Mapper for converting between entities and models.
     */
    @Autowired
    private ModelMapper modelMapper;


    /**
     * @param emailNotification The details of the email used for the notification.
     * @return the created Notification object
     */
    @Override
    public Notification createNotification(EmailNotification emailNotification) {
        NotificationEntity entity = new NotificationEntity();
        entity.setRecipient(emailNotification.getRecipient());
        if (emailNotification.getContactId() != null) {
            entity.setContactId(emailNotification.getContactId());
        }
        entity.setSubject(emailNotification.getSubject());
        entity.setTemplateId(emailNotification.getTemplateId());
        entity.setTemplateName(emailNotification.getTemplateName());
        entity.setBody(emailNotification.getBody());
        entity.setDateSend(LocalDateTime.now());
        entity.setStatusSend(StatusSend.SENT);

        NotificationEntity saved = notificationRepository.save(entity);
        Notification notification = modelMapper.map(saved, Notification.class);

        notification.setEmailNotification(emailNotification);

        return notification;
    }

    /**
     * Retrieves notifications based on provided filters.
     * @param filters the desired filters.
     * @return a list of notifications matching the filters
     */
    @Override
    public List<Notification> getNotifications(NotificationFilter filters) {
        List<NotificationEntity> notifications;

        if (filters != null) {
            Specification<NotificationEntity> spec = Specification
                    .where(NotificationSpecifications.filterById(filters.getId()))
                    .and(NotificationSpecifications.filterByViewed(filters.getViewed()))
                    .and(NotificationSpecifications.filterByContactId(filters.getContactId()))
                    .and(NotificationSpecifications.filterBySubject(filters.getSubject()))
                    .and(NotificationSpecifications.filterByRecipient(filters.getRecipient()))
                    .and(NotificationSpecifications.filterByDateRange(filters.getFrom(), filters.getUntil()));

            notifications = notificationRepository.findAll(spec);
        } else {
            notifications = notificationRepository.findAll();
        }

        return notifications.stream()
                .map(this::mapToNotification)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves notifications based on provided filters.
     * @param pageable The page of the notifications.
     * @param filters the desired filters.
     * @return a list of notifications matching the filters
     */
    @Override
    public Page<Notification> getNotifications(Pageable pageable, NotificationFilter filters) {
        Page<NotificationEntity> notificationsPage;

        Specification<NotificationEntity> spec = Specification.where(null);

        if (filters != null) {
            spec = spec
                    .and(NotificationSpecifications.filterById(filters.getId()))
                    .and(NotificationSpecifications.filterByViewed(filters.getViewed()))
                    .and(NotificationSpecifications.filterByContactId(filters.getContactId()))
                    .and(NotificationSpecifications.filterBySubject(filters.getSubject()))
                    .and(NotificationSpecifications.filterByRecipient(filters.getRecipient()))
                    .and(NotificationSpecifications.filterByDateRange(filters.getFrom(), filters.getUntil()));

            if (filters.getSearchTerm() != null && !filters.getSearchTerm().isEmpty()) {
                spec = spec.and(NotificationSpecifications.filterBySearchTerm(filters.getSearchTerm()));
            }
        }

        notificationsPage = notificationRepository.findAll(spec, pageable);

        List<Notification> notifications = notificationsPage.getContent().stream()
                .map(this::mapToNotification)
                .collect(Collectors.toList());

        return new PageImpl<>(notifications, pageable, notificationsPage.getTotalElements());
    }



    /**
     * Marks a notification as visualized by updating its status.
     *
     * @param id The ID of the notification to be visualized.
     * @return The updated notification as a {@link Notification} object.
     * @throws NotFoundException if no notification with the specified ID exists.
     */
    @Override
    public Notification viewNotification(Long id) {
        Optional<NotificationEntity> entityOpt =
                notificationRepository.findById(id);

        if (entityOpt.isEmpty()) {
            throw new NotFoundException("Notification not found");
        }

        entityOpt.get().setStatusSend(StatusSend.VISUALIZED);

        NotificationEntity savedEntity =
                notificationRepository.save(entityOpt.get());

        return modelMapper.map(savedEntity, Notification.class);

    }

    /**
     * Maps a NotificationEntity to a Notification object.
     * @param entity the NotificationEntity to be mapped.
     * @return a Notification object containing the mapped values.
     */
    private Notification mapToNotification(NotificationEntity entity) {
        Notification notification = modelMapper.map(entity, Notification.class);

        EmailNotification emailNotification = new EmailNotification();
        emailNotification.setSubject(entity.getSubject());
        emailNotification.setTemplateId(entity.getTemplateId());
        emailNotification.setTemplateName(entity.getTemplateName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDate = entity.getDateSend().format(formatter);

        notification.setDateSend(formattedDate);
        notification.setEmailNotification(emailNotification);

        return notification;
    }

    /**
     * Retrieves a {@link Notification} by its unique identifier.
     *
     * @param id the unique identifier of the notification to be retrieved.
     * @return the {@link Notification} object corresponding to the specified ID.
     * @throws NotFoundException if no notification is found with the given ID.
     */
    @Override
    public Notification getNotification(Long id) {
        Optional<NotificationEntity> optionalNotification =
                notificationRepository.findById(id);

        if (optionalNotification.isEmpty()) {
            throw new NotFoundException("Notification not found");
        }

        return mapToNotification(optionalNotification.get());
    }

}
