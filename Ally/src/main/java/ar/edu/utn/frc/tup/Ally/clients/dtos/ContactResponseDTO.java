package ar.edu.utn.frc.tup.lc.iv.clients.dtos;

import ar.edu.utn.frc.tup.lc.iv.models.ContactType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ContactResponseDTO is a Data Transfer Object (DTO)
 * used to encapsulate the response data
 * of the contact service. It contains information about the
 * contact's ID, value, and type.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactResponseDTO {

    /**
     * The unique identifier of the contact.
     */
    private Long id;

    /**
     * The value of the contact, for example, an email address or a phone number.
     */
    @JsonProperty("contact_value")
    private String contactValue;

    /**
     * The type of contact, such as 'email', 'phone', etc.
     */
    @JsonProperty("contact_type")
    private ContactType contactType;

    /**
     * A list of the contact subscriptions.
     */
    private List<String> subscriptions;
}
