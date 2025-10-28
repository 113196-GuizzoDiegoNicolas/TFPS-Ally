package ar.edu.utn.frc.tup.lc.iv.models;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;

/**
 * Model representing the filter criteria for retrieving notifications.
 * <p>
 * This class encapsulates the various criteria that can be used to filter
 * notifications when retrieving them from the system.
 * </p>
 */
@Data
public class NotificationFilter {

    /** The ID of the notification to filter by. */
    private Long id;

    /** The recipient of the notifications to filter by. */
    private String recipient;

    /** The contact ID associated with the notifications. */
    @JsonProperty("contact_id")
    private Long contactId;

    /** Indicates whether to filter notifications that have been viewed. */
    private Boolean viewed;

    /** The subject of the notifications to filter by. */
    private String subject;

    /**
     * The start date and time for filtering notifications based on the send date.
     * The date should be in ISO-8601 format.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime from;

    /**
     * The end date and time for filtering notifications based on the send date.
     * The date should be in ISO-8601 format.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime until;

    /** The page number for pagination. */
    private Integer page;

    /** The size of the page for pagination. */
    private Integer size;

    /** A search term to filter notifications based on various criteria. */
    @JsonProperty("search_term")
    private String searchTerm;
}
