package ar.edu.utn.frc.tup.lc.iv.dtos.emailTemplate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Data Transfer Object (DTO) representing the update of an email template.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateEmailTemplateDTO {
    /**
     * Field of its ID.
     */
    private Long id;
    /**
     * Field of its new name.
     */
    private String name;
    /**
     * This field represents the new body stored in base64.
     * <p>
     *     This is because ...
     * </p>
     */
    private String base64body;
}
