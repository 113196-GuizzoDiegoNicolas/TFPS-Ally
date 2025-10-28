package ar.edu.utn.frc.tup.lc.iv.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class that creates a {@link RestTemplate} bean
 * for making HTTP requests in the application.
 * <p>It uses the {@link Configuration} annotation to define beans in Spring.</p>
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Creates and returns a {@link RestTemplate} bean.
     *
     * @return a new instance of {@link RestTemplate}.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
