package Ally.Scafolding.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a subscription with its associated details.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    /**
     * The unique identifier for the subscription.
     */
    private Long id;

    /**
     * The name of the subscription.
     */
    private String name;

    /**
     * Boolean representing weather people can
     * unsubscribe or not.
     */
    private Boolean isUnsubscribable;
}
