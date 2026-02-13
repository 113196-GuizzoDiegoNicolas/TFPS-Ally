package Ally.Scafolding.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Spring Doc configuration class for OpenAPI (Swagger) documentation.
 */
@Configuration
public class SpringDocConfig {

    @Value("${app.url:http://localhost:8080}")
    private String url;

    @Value("${app.dev-name:Equipo de Desarrollo Ally}")
    private String devName;

    @Value("${app.dev-email:soporte@ally.com}")
    private String devEmail;

    /**
     * Configures OpenAPI documentation for the Ally application.
     */
    @Bean
    public OpenAPI openApi(@Value("${app.name:Ally API}") String appName,
                           @Value("${app.desc:Sistema de Gestión Médica}") String appDescription,
                           @Value("${app.version:1.0.0}") String appVersion) {

        Info info = new Info()
                .title(appName)
                .version(appVersion)
                .description("""
                    ## 🏥 Ally - Sistema de Gestión de Pacientes y Transportistas
                    
                    ### 📋 Descripción
                    API REST para la gestión integral de pacientes, prestadores médicos y servicios de transporte sanitario.
                    
                    ### 🎯 Características Principales
                    - Gestión de pacientes con historial médico
                    - Administración de prestadores médicos por especialidad  
                    - Control de transportistas y zonas de cobertura
                    - Sistema de turnos y servicios
                    - Autenticación y autorización de usuarios
                    
                    ### 👥 Roles del Sistema
                    - **PACIENTE**: Puede gestionar su perfil y solicitar servicios
                    - **PRESTADOR**: Gestiona servicios médicos y disponibilidad
                    - **TRANSPORTISTA**: Administra traslados y zonas de cobertura
                    - **ADMIN**: Acceso completo al sistema
                    """)
                .contact(new Contact()
                        .name(devName)
                        .email(devEmail)
                        .url("https://www.ally.com"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0.html"));

        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Servidor de Desarrollo Local");

        Server productionServer = new Server()
                .url("https://api.ally.com")
                .description("Servidor de Producción");

        return new OpenAPI()
                .components(new Components())
                .info(info)
                .servers(List.of(localServer, productionServer));
    }

    /**
     * Model resolver for proper JSON schema generation.
     */
    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper);
    }
}