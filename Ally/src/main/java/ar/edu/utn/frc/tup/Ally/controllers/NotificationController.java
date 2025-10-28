package ar.edu.utn.frc.tup.lc.iv.controllers;
import ar.edu.utn.frc.tup.lc.iv.dtos.notification.GetNotificationDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.notification.GetNotificationDetailsDTO;
import ar.edu.utn.frc.tup.lc.iv.models.Notification;
import ar.edu.utn.frc.tup.lc.iv.models.NotificationFilter;
import ar.edu.utn.frc.tup.lc.iv.services.NotificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing notifications.
 * Provides endpoints for retrieving notifications with various filters.
 */
@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {


    /**
     * Service for managing notifications.
     */
    @Autowired
    private NotificationService notificationService;
    /**
     * A Mapper to map between DTOs and entities.
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     * @param filters The desired filters for get.
     * @return  A single notification or a list of notifications
     */
    @GetMapping
    public ResponseEntity<?> getNotifications(@ModelAttribute() NotificationFilter filters) {

        List<Notification> notifications =
                notificationService.getNotifications(filters);

        // If searching by specific ID, return a single object instead of a list
        if (filters != null && filters.getId() != null && !notifications.isEmpty()) {
            Notification notification = notifications.get(0);
            GetNotificationDTO dto = convertToDto(notification);
            return ResponseEntity.ok(dto);
        }

        List<GetNotificationDTO> dtos = notifications.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * @param filters The desired filters for get.
     * @param pageable Pageable object.
     * @return  A single notification or a list of notifications
     */
    @GetMapping("pageable")
    public ResponseEntity<Page<GetNotificationDTO>> getNotificationsPageable(
            @ModelAttribute NotificationFilter filters,
            Pageable pageable) {

        Page<Notification> notificationsPage = notificationService
                .getNotifications(pageable, filters);

        List<GetNotificationDTO> dtos = notificationsPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        Page<GetNotificationDTO> dtoPage =
                new PageImpl<>(dtos, pageable, notificationsPage.getTotalElements());

        return ResponseEntity.ok(dtoPage);
    }

    /**
     * Updates the status of a notification to indicate that it has been visualized.
     * @param id The ID of the notification to be visualized.
     * @return A ResponseEntity containing the updated notification
     * If the notification with the specified ID is not found,
     * a NotFoundException will be thrown.
     */
    @PutMapping("{id}")
    public ResponseEntity<GetNotificationDTO> visualizeNotification(@PathVariable Long id) {
        Notification updatedNotification = notificationService.viewNotification(id);

        return ResponseEntity.ok(convertToDto(updatedNotification));
    }

    /**
     * Retrieves the details of a notification by its unique identifier.
     *
     * @param id the unique identifier of the notification to be retrieved.
     * @return a {@link ResponseEntity} containing the
     * {@link GetNotificationDetailsDTO} object
     *         with the details of the specified notification.
     */
    @GetMapping("{id}")
    public ResponseEntity<GetNotificationDetailsDTO> getNotificationDetail(@PathVariable Long id) {

        Notification notification = notificationService.getNotification(id);
        GetNotificationDetailsDTO dto =
                modelMapper.map(notification, GetNotificationDetailsDTO.class);

        if (notification.getEmailNotification() != null) {
            dto.setSubject(notification.getEmailNotification().getSubject());
            dto.setTemplateId(notification.getEmailNotification().getTemplateId());
            dto.setTemplateName(notification.getEmailNotification().getTemplateName());
        }
        return ResponseEntity.ok(dto);
    }

    /**
     * Converts a Notification object to a GetNotificationDTO.
     * @param notification the Notification object to be converted.
     * @return a GetNotificationDTO containing the mapped values.
     */
    private GetNotificationDTO convertToDto(Notification notification) {
        GetNotificationDTO dto = modelMapper.map(notification, GetNotificationDTO.class);

        if (notification.getEmailNotification() != null) {
            dto.setSubject(notification.getEmailNotification().getSubject());
            dto.setTemplateId(notification.getEmailNotification().getTemplateId());
            dto.setTemplateName(notification.getEmailNotification().getTemplateName());
        }

        return dto;
    }


}
