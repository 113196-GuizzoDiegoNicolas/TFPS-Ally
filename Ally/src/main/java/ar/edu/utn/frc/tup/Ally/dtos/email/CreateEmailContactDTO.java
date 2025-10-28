package ar.edu.utn.frc.tup.lc.iv.dtos.email;

import ar.edu.utn.frc.tup.lc.iv.models.KeyValueCustomPair;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for creating an email contact with a specific template.
 * Includes template ID, contact IDs, email subject, and template variables.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmailContactDTO {
    /**
     * ID of which email template it will use.
     */
    @JsonProperty("template_id")
    private Long templateId;
    /**
     * The contact ids to recieve.
     */
    @JsonProperty("contact_ids")
    private List<Long> contactIds;
    /**
     * The email subject.
     */
    private String subject;

    /**
     * Key-value pair representing variables.
     */
    private List<KeyValueCustomPair> variables;

    /**
     * Name of Notification type.
     */
    @JsonProperty("notification_type")
    @Nullable
    private String notificationType;
}
