package ar.edu.utn.frc.tup.lc.iv.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * EmailTemplate class.
 * <p>
 * This class represents an email with its name and body.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplate {

    /**
     * Identification number.
     */
    private Long id;

    /**
     * The email's name.
     */
    private String name;

    /**
     * What the email's body will be.
     */
    private String body;

    /**
     * Boolean representing if the
     * template is active or not.
     */
    private Boolean active;

}
