package Ally.Scafolding.dtos.common.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Schema(description = "DTO para recibir webhooks de Mercado Pago")
public class MercadoPagoWebhookDTO {

    @JsonProperty("id")
    @Schema(description = "ID del webhook", example = "123456789")
    private Long id;

    @JsonProperty("live_mode")
    @Schema(description = "Si es modo producción", example = "true")
    private Boolean liveMode;

    @JsonProperty("type")
    @Schema(description = "Tipo de evento", example = "payment")
    private String type;

    @JsonProperty("date_created")
    @Schema(description = "Fecha de creación del evento")
    private OffsetDateTime dateCreated;

    @JsonProperty("user_id")
    @Schema(description = "ID de usuario Mercado Pago", example = "123456789")
    private Long userId;

    @JsonProperty("api_version")
    @Schema(description = "Versión de la API", example = "v1")
    private String apiVersion;

    @JsonProperty("action")
    @Schema(description = "Acción del evento", example = "payment.created")
    private String action;

    @JsonProperty("data")
    @Schema(description = "Datos del evento")
    private WebhookData data;

    @Data
    public static class WebhookData {
        @JsonProperty("id")
        @Schema(description = "ID del recurso", example = "123456789")
        private String id;
    }
}