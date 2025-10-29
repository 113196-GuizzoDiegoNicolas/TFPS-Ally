package Ally.Scafolding.dtos.common.contact;


import Ally.Scafolding.models.ContactType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Data Transfer Object (DTO) representing a Contact.
 * <p>
 * This class is used to transfer data related to contact between
 * layers, and it includes validation constraints for its fields.
 * </p>
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePatientDTO {

    /**
     * The value of the contact (e.g., email, phone number).
     */
    @JsonProperty("contact_value")
    private String value;

    /**
     *  Contact type, represented as an enum.
     *  Expected to be one of the values from the ContactType enum
     *  (e.g., "EMAIL", "PHONE").
     */

    @JsonProperty("contact_type")
    private ContactType contactType;
}
