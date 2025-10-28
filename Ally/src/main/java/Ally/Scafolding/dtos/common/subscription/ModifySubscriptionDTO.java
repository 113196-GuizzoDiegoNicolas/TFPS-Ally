package Ally.Scafolding.dtos.common.subscription;

import lombok.*;
/**
 * Data Transfer Object (DTO) representing a change in subscription.
 * This class is used to transfer data related to contact between
 * layers, and it includes validation constraints for its fields.
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModifySubscriptionDTO {

    /**
     * The identifier representing contact.
     */
    private Long contactId;

    /**
     * The identifier representing subscription.
     */
    private Long subscriptionId;

    /**
     * Boolean representing weather contact is
     * unsubscribe or not.
     */
    private Boolean subscriptionValue;
}
