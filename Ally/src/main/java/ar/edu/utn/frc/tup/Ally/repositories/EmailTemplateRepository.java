package ar.edu.utn.frc.tup.lc.iv.repositories;

import ar.edu.utn.frc.tup.lc.iv.entities.EmailTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing entities <p>
 * associated with email templates.
 */
@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplateEntity, Long>,
        JpaSpecificationExecutor<EmailTemplateEntity> {
}
