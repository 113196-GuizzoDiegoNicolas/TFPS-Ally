// Archivo: src/main/java/Ally/Scafolding/configs/MercadoPagoConfiguration.java
package Ally.Scafolding.configs;

import com.mercadopago.MercadoPagoConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;

@Slf4j
@Configuration
public class MercadoPagoConfiguration {

    @Value("${mercadopago.access-token:TEST-12345678901234567890123456789012}")
    private String accessToken;

    @Value("${mercadopago.public-key:TEST-abcdefgh-ijkl-mnop-qrst-uvwxyz123456}")
    private String publicKey;

    @Value("${mercadopago.base-url:https://api.mercadopago.com}")
    private String baseUrl;

    @Value("${mercadopago.integrator-id:}")
    private String integratorId;

    @Value("${app.name:Ally}")
    private String appName;

    @PostConstruct
    public void init() {
        try {
            // Verificar que tenemos las credenciales
            log.info("⚙️ Inicializando configuración de Mercado Pago...");
            log.info("📱 Aplicación: {}", appName);

            if (accessToken == null || accessToken.trim().isEmpty()) {
                log.warn("⚠️ Access Token de Mercado Pago no configurado");
                return;
            }

            // Configurar SDK de Mercado Pago
            log.info("🔧 Configurando SDK de Mercado Pago...");

            // ESTA ES LA LÍNEA IMPORTANTE: usar el SDK correctamente
            com.mercadopago.MercadoPagoConfig.setAccessToken(accessToken);

            // Configurar otros parámetros
            com.mercadopago.MercadoPagoConfig.setPlatformId(appName + "Health");
            com.mercadopago.MercadoPagoConfig.setCorporationId(appName + "Corp");

            if (integratorId != null && !integratorId.trim().isEmpty()) {
                com.mercadopago.MercadoPagoConfig.setIntegratorId(integratorId);
                log.info("👤 Integrator ID: {}", integratorId);
            }

            log.info("✅ Mercado Pago SDK configurado exitosamente");
            log.info("🔑 Access Token: {}...", maskToken(accessToken));
            log.info("🔑 Public Key: {}...", maskToken(publicKey));
            log.info("🌐 Base URL: {}", baseUrl);

            // Verificar si estamos en modo TEST
            if (accessToken.startsWith("TEST-")) {
                log.info("🔬 MODO TEST/SANDBOX ACTIVADO");
                log.info("💡 Las transacciones serán simuladas");
            } else {
                log.info("🚀 MODO PRODUCCIÓN ACTIVADO");
            }

        } catch (Exception e) {
            log.error("❌ Error configurando Mercado Pago SDK: {}", e.getMessage());
            // No lanzar excepción para que la aplicación pueda iniciar
            log.warn("⚠️ La aplicación continuará sin Mercado Pago SDK");
            log.warn("⚠️ Los pagos con Mercado Pago usarán modo simulación");
        }
    }

    /**
     * Enmascara tokens sensibles para logging
     */
    private String maskToken(String token) {
        if (token == null || token.length() <= 8) {
            return "****";
        }
        return token.substring(0, 4) + "****" + token.substring(token.length() - 4);
    }

    /**
     * Bean para RestTemplate (necesario para llamadas HTTP)
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        log.info("🌐 RestTemplate configurado para llamadas HTTP");
        return restTemplate;
    }

    /**
     * Bean para PaymentClient de Mercado Pago
     */
    @Bean
    public com.mercadopago.client.payment.PaymentClient paymentClient() {
        try {
            log.info("💳 Creando PaymentClient de Mercado Pago...");
            return new com.mercadopago.client.payment.PaymentClient();
        } catch (Exception e) {
            log.error("❌ Error creando PaymentClient: {}", e.getMessage());
            return null; // Retornar null si no se puede crear
        }
    }

    /**
     * Bean para PreferenceClient de Mercado Pago
     */
    @Bean
    public com.mercadopago.client.preference.PreferenceClient preferenceClient() {
        try {
            log.info("🛒 Creando PreferenceClient de Mercado Pago...");
            return new com.mercadopago.client.preference.PreferenceClient();
        } catch (Exception e) {
            log.error("❌ Error creando PreferenceClient: {}", e.getMessage());
            return null; // Retornar null si no se puede crear
        }
    }

    // ============ GETTERS PARA USAR EN OTROS COMPONENTES ============

    public String getAccessToken() {
        return accessToken;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getIntegratorId() {
        return integratorId;
    }

    public String getAppName() {
        return appName;
    }
}