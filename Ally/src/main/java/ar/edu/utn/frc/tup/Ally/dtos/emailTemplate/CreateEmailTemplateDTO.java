package ar.edu.utn.frc.tup.lc.iv.dtos.emailTemplate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing the creation of an email template.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateEmailTemplateDTO {
    /**
     * Field of its name.
     */
    private String name;
    /**
     * This field represents the body stored in base64.
     * <p>
     *     This is because ...
     * </p>
     */
    private String base64body;

}
