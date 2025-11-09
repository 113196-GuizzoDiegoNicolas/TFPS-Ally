package Ally.Scafolding.dtos.common.patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating a contact's value.
 * <p>
 * Encapsulates the contact's ID and the new value.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePatientDTO {

    /**
     * The unique identifier of the contact.
     */
    private Long id;

    /**
     * The new value for the contact.
     */
    private String value;
}
