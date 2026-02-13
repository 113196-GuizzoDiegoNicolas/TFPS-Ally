package Ally.Scafolding.payment.client;

import Ally.Scafolding.configs.MercadoPagoConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MercadoPagoClient {

    private final RestTemplate restTemplate;
    private final MercadoPagoConfiguration mercadoPagoConfig;
    private final ObjectMapper objectMapper;

    private static final String PREFERENCES_URL = "/checkout/preferences";
    private static final String PAYMENTS_URL = "/v1/payments";
    private static final String REFUNDS_URL = "/v1/payments/{payment_id}/refunds";

    /**
     * Crea una preferencia de pago
     */
    public Map<String, Object> createPreference(Map<String, Object> preferenceData) {
        String url = mercadoPagoConfig.getBaseUrl() + PREFERENCES_URL;

        HttpHeaders headers = createHeaders();

        log.debug("Creando preferencia en Mercado Pago: {}", preferenceData);

        try {
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(preferenceData, headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.POST, request, Map.class);

            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK) {
                log.info("Preferencia creada exitosamente: {}", response.getBody().get("id"));
                return response.getBody();
            } else {
                throw new RuntimeException("Error creando preferencia: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException e) {
            log.error("Error HTTP creando preferencia: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Error Mercado Pago: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Error creando preferencia: {}", e.getMessage(), e);
            throw new RuntimeException("Error creando preferencia en Mercado Pago", e);
        }
    }

    /**
     * Obtiene un pago por ID
     */
    public Map<String, Object> getPayment(String paymentId) {
        String url = mercadoPagoConfig.getBaseUrl() + PAYMENTS_URL + "/" + paymentId;

        HttpHeaders headers = createHeaders();

        log.debug("Obteniendo pago de Mercado Pago: {}", paymentId);

        try {
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.GET, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException("Error obteniendo pago: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException e) {
            log.error("Error HTTP obteniendo pago: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Error obteniendo pago de Mercado Pago", e);
        } catch (Exception e) {
            log.error("Error obteniendo pago: {}", e.getMessage(), e);
            throw new RuntimeException("Error obteniendo pago de Mercado Pago", e);
        }
    }

    /**
     * Reembolsa un pago
     */
    public Map<String, Object> refundPayment(String paymentId, BigDecimal amount) {
        String url = mercadoPagoConfig.getBaseUrl() + REFUNDS_URL.replace("{payment_id}", paymentId);

        HttpHeaders headers = createHeaders();
        Map<String, Object> requestBody = amount != null ?
                Collections.singletonMap("amount", amount) : Collections.emptyMap();

        log.info("Reembolsando pago {} por monto {}", paymentId, amount);

        try {
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.POST, request, Map.class);

            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK) {
                log.info("Pago {} reembolsado exitosamente", paymentId);
                return response.getBody();
            } else {
                throw new RuntimeException("Error reembolsando pago: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException e) {
            log.error("Error HTTP reembolsando pago: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Error reembolsando pago en Mercado Pago", e);
        } catch (Exception e) {
            log.error("Error reembolsando pago: {}", e.getMessage(), e);
            throw new RuntimeException("Error reembolsando pago en Mercado Pago", e);
        }
    }

    /**
     * Reembolsa el pago completo
     */
    public Map<String, Object> refundPayment(String paymentId) {
        return refundPayment(paymentId, null);
    }

    /**
     * Valida la firma de un webhook
     */
    public boolean validateWebhookSignature(String signature, Map<String, Object> payload) {
        // Implementación de validación de firma (si es necesario)
        // Mercado Pago envía x-signature en los headers
        // Se debe validar que el payload no haya sido alterado
        log.debug("Validando firma de webhook: {}", signature);

        // Por ahora retornamos true, en producción implementar validación real
        return true;
    }

    /**
     * Busca pagos por referencia externa
     */
    public Map<String, Object> searchPayments(String externalReference) {
        String url = mercadoPagoConfig.getBaseUrl() + PAYMENTS_URL + "/search";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("external_reference", externalReference)
                .queryParam("sort", "date_created")
                .queryParam("criteria", "desc");

        HttpHeaders headers = createHeaders();

        try {
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    builder.toUriString(), HttpMethod.GET, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException("Error buscando pagos: " + response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Error buscando pagos: {}", e.getMessage(), e);
            throw new RuntimeException("Error buscando pagos en Mercado Pago", e);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(mercadoPagoConfig.getAccessToken());
        headers.set("User-Agent", "AllyHealth/1.0");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}