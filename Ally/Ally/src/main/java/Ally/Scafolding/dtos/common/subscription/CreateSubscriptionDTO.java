package Ally.Scafolding.dtos.common.subscription;

import lombok.*;
/**
 * Data Transfer Object (DTO) representing a Subscription.
 * This class is used to transfer data related to contact between
 * layers, and it includes validation constraints for its fields.
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateSubscriptionDTO {

    /**
     * The value of the subscription name.
     */
    private String name;

    /**
     * Boolean representing weather people can
     * unsubscribe or not.
     */
    private Boolean isUnsubscribable;
}
