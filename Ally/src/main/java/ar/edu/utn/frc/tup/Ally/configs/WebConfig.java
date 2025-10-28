package ar.edu.utn.frc.tup.lc.iv.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up CORS.
 * <p>
 * This implements the {@link WebMvcConfigurer}
 * interface to provide
 * custom configs for CORS.
 * </p>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures the CORS mappings for the application.
     * <p>
     * This allows requests from any origin, all HTTP methods,
     * all headers but no credentials to be included in requests.
     * </p>
     * @param registry the CORS registry used to add CORS mappings
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false);
    }
}
