package Ally.Scafolding.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //  Desactiva CSRF (necesario para APIs y Swagger)
                .csrf(csrf -> csrf.disable())

                //  Configura rutas públicas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/api/auth/**",
                                "/api/users/**",
                                "/h2-console/**",     //  necesario para abrir H2 Console
                                "/patients/**",
                                "/ping"
                        ).permitAll()
                        .anyRequest().permitAll() // Todo lo demás abierto por ahora
                )

                //  Permite usar la H2 console en frames
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                // Permite autenticación básica (útil para probar por Postman)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

