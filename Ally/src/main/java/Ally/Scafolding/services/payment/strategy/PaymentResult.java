package Ally.Scafolding.services.payment.strategy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResult {

    private boolean success;
    private String message;
    private String transactionId;
    private String authorizationCode;
    private String errorMessage;
    private Map<String, Object> additionalData;

    public static PaymentResult success(String message, String transactionId) {
        return PaymentResult.builder()
                .success(true)
                .message(message)
                .transactionId(transactionId)
                .build();
    }

    public static PaymentResult success(String message, String transactionId,
                                        Map<String, Object> additionalData) {
        return PaymentResult.builder()
                .success(true)
                .message(message)
                .transactionId(transactionId)
                .additionalData(additionalData)
                .build();
    }

    public static PaymentResult failure(String errorMessage) {
        return PaymentResult.builder()
                .success(false)
                .errorMessage(errorMessage)
                .build();
    }
}