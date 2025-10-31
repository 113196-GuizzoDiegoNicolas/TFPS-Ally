package Ally.Scafolding.configs;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up Envers auditing.
 */
@Configuration
public class EnversConfig {


    /**
     * EntityManagerFactory used to create EntityManager instances for
     * interacting with the persistence context.
     */
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    /**
     * Creates an AuditReader bean for accessing audit information.
     *
     * @return an instance of AuditReader.
     */
    @Bean
    public AuditReader auditReader() {
        return AuditReaderFactory.get(entityManagerFactory.createEntityManager());
    }
}
