package Ally.Scafolding.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBeans {
    @Bean(name = "passwordEncoder2")
    public PasswordEncoder passwordEncoder2() {
        return new BCryptPasswordEncoder();
    }
}
