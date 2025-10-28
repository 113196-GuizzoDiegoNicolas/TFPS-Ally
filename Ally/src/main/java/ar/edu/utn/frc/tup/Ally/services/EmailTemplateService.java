package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.dtos.emailTemplate.CreateEmailTemplateDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.emailTemplate.UpdateEmailTemplateDTO;
import ar.edu.utn.frc.tup.lc.iv.models.EmailTemplate;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Interface to manage email templates.
 */
@Service
public interface EmailTemplateService {
    /**
     * Create an Email Template.
     * @param emailTemplateDTO representing the creation of an email template
     * @return an EmailTemplate
     */
    EmailTemplate createEmailTemplate(CreateEmailTemplateDTO emailTemplateDTO) throws MessagingException;

    /**
     * Gets an email template associated with an ID.
     * @param id its ID
     * @return the associated EmailTemplate
     */
    EmailTemplate getEmailTemplateById(Long id);

    /**
     * Retrieves email templates, opt. filtering by presence
     * of placeholders.
     * @param hasPlaceholders if true, filters templates with placeholders;
     * @param name if not empty search by name.
     * returns all if false or null.
     * @return a list of {@link EmailTemplate} objects.
     */

    List<EmailTemplate> getAllEmailTemplate(Boolean hasPlaceholders, String name);

    /**
     * Retrieves a paginated list of email templates with optional filtering.
     * @param pageable Pagination information (page number, size, etc.).
     * @param active Optional filter to return only active templates.
     * @param hasPlaceholders Optional filter to return templates with placeholders.
     * @param name Optional search parameter to filter templates by name.
     * @return Page of EmailTemplate containing the filtered templates.
     */
    Page<EmailTemplate> getAllEmailTemplate(Pageable pageable, Boolean active, Boolean hasPlaceholders, String name);

    /**
     * Updates an email template by its ID.
     * @param updateDto data transfer object that has details of
     * the email template to be updated
     * @return ResponseEntity with the updated Email Template
     */
    EmailTemplate updateEmailTemplate(UpdateEmailTemplateDTO updateDto) throws MessagingException;

    /**
     * Deletes logically an email template by its ID.
     * @param id identifier of
     * the email template to be updated
     * @return Boolean retrieving the operation status
     */
    Boolean deleteEmailTemplate(Long id);
}
