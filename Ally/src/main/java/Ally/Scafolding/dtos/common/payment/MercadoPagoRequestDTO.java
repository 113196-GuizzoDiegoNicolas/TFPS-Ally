package Ally.Scafolding.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear preferencia en Mercado Pago")
public class MercadoPagoRequestDTO {

    @Schema(description = "Lista de items a pagar")
    private List<ItemDTO> items;

    @Schema(description = "Información del pagador")
    private PayerDTO payer;

    @Schema(description = "URLs de redirección")
    private BackUrlsDTO backUrls;

    @Schema(description = "URL de notificación (webhook)")
    private String notificationUrl;

    @Schema(description = "Referencia externa (ID del servicio)")
    private String externalReference;

    @Schema(description = "Fecha de expiración en formato ISO 8601")
    private String expires;

    @Schema(description = "Fecha desde cuando estará disponible en formato ISO 8601")
    private String dateOfExpiration;

    @Schema(description = "Métodos de pago excluidos")
    private List<String> excludedPaymentMethods;

    @Schema(description = "Tipos de pago excluidos")
    private List<String> excludedPaymentTypes;

    @Schema(description = "Configuración de envío")
    private Boolean shipments;

    @Schema(description = "Datos adicionales")
    private Map<String, Object> additionalInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemDTO {

        @Schema(description = "ID del item", example = "item001")
        private String id;

        @Schema(description = "Título del item", example = "Consulta Cardiología")
        private String title;

        @Schema(description = "Descripción del item", example = "Consulta médica especializada")
        private String description;

        @Schema(description = "URL de la imagen del item")
        private String pictureUrl;

        @Schema(description = "Categoría del item", example = "health")
        private String categoryId;

        @Schema(description = "Cantidad", example = "1")
        private Integer quantity;

        @Schema(description = "Precio unitario", example = "25000.50")
        private BigDecimal unitPrice;

        @Schema(description = "Moneda", example = "ARS")
        @Builder.Default
        private String currencyId = "ARS";
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PayerDTO {

        @Schema(description = "Nombre del pagador", example = "Juan Pérez")
        private String name;

        @Schema(description = "Apellido del pagador", example = "González")
        private String surname;

        @Schema(description = "Email del pagador", example = "cliente@example.com")
        private String email;

        @Schema(description = "Teléfono del pagador")
        private PhoneDTO phone;

        @Schema(description = "Dirección del pagador")
        private AddressDTO address;

        @JsonProperty("date_created")
        @Schema(description = "Fecha de creación del pagador")
        private String dateCreated;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhoneDTO {

        @Schema(description = "Código de área", example = "11")
        private String areaCode;

        @Schema(description = "Número de teléfono", example = "999999999")
        private String number;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressDTO {

        @Schema(description = "Calle", example = "Av. Corrientes")
        private String streetName;

        @Schema(description = "Número", example = "1234")
        private String streetNumber;

        @Schema(description = "Código postal", example = "C1043")
        private String zipCode;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BackUrlsDTO {

        @Schema(description = "URL de éxito", example = "https://tuapp.com/payment/success")
        private String success;

        @Schema(description = "URL de fallo", example = "https://tuapp.com/payment/failure")
        private String failure;

        @Schema(description = "URL de pendiente", example = "https://tuapp.com/payment/pending")
        private String pending;
    }
}