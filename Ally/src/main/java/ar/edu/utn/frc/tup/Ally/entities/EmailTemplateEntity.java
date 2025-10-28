package ar.edu.utn.frc.tup.lc.iv.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

/**
 * Represents an email template in the database.
 * <p>
 *     This entity stores its ID as well its name and body.
 * </p>
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "email_templates")
@Audited
public class EmailTemplateEntity {

    /**
     * Unique identifier for the email template.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Email Template name.
     */
    @Column
    private String name;

    /**
     * Email Template body.
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    private String body;

    /**
     * Boolean if it is active or not.
     */
    @Column
    private Boolean active;

}
