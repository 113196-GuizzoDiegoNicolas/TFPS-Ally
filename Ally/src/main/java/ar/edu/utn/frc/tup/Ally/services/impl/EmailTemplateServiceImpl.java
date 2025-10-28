package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.exceptions.NotFoundException;
import ar.edu.utn.frc.tup.lc.iv.dtos.emailTemplate.CreateEmailTemplateDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.emailTemplate.UpdateEmailTemplateDTO;
import ar.edu.utn.frc.tup.lc.iv.entities.EmailTemplateEntity;
import ar.edu.utn.frc.tup.lc.iv.models.EmailTemplate;
import ar.edu.utn.frc.tup.lc.iv.repositories.EmailTemplateRepository;
import ar.edu.utn.frc.tup.lc.iv.services.EmailTemplateService;
import ar.edu.utn.frc.tup.lc.iv.services.utils.EmailVariableUtils;
import ar.edu.utn.frc.tup.lc.iv.specifications.TemplateSpecifications;
import jakarta.mail.MessagingException;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.w3c.tidy.Tidy;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Implementation of the Email Template <p>
 * service for managing the various templates.
 */
@Service
@NoArgsConstructor
public class EmailTemplateServiceImpl implements EmailTemplateService {

    /**
     * Repository for perform operations with Email Template entities.
     */
    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    /**
     * ModelMapper for perform the necessary mapping operations.
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Creates an email template.
     * @param emailTemplateDTO representing the creation of an email template
     * @return the created template: {@link EmailTemplate}
     * @throws MessagingException if HTML body isn't valid
     */
    @Override
    public EmailTemplate createEmailTemplate(CreateEmailTemplateDTO emailTemplateDTO) throws MessagingException {
        byte[] decodedBytes = Base64.getDecoder().decode(emailTemplateDTO.getBase64body());
        String htmlContent = new String(decodedBytes);

        if (validateHTML(htmlContent)) {
            EmailTemplateEntity entity = new EmailTemplateEntity();
            entity.setBody(htmlContent);
            entity.setName(emailTemplateDTO.getName());
            entity.setActive(true);
            EmailTemplateEntity saved =
                    emailTemplateRepository.save(entity);

            return modelMapper.map(saved, EmailTemplate.class);
        } else {
            throw new MessagingException("Error creating template, the HTML body is INVALID");
        }
    }

    /**
     * Gets a template with the associated identification.
     * @param id which is the template ID
     * @return the associated template or else a null
     */
    @Override
    public EmailTemplate getEmailTemplateById(Long id) {
        Optional<EmailTemplateEntity> optTemplate =
                emailTemplateRepository.findById(id);

        if (optTemplate.isEmpty()) {
            throw new NotFoundException("Template not found");
        }

        return modelMapper.map(optTemplate.get(), EmailTemplate.class);
    }


    /**
     Gets all email templates available in the Database.
     * @return a list of all Email Template in the Database
     */
    @Override
    public List<EmailTemplate> getAllEmailTemplate(Boolean hasPlaceholders, String name) {
        List<EmailTemplateEntity> entities = emailTemplateRepository.findAll();

        List<EmailTemplateEntity> foundTemplateEntities = Boolean.TRUE.equals(hasPlaceholders)
                ? checkTemplatesForVariables(entities)
                : entities;

        if (name != null) {
            foundTemplateEntities = foundTemplateEntities.stream()
                    .filter(x -> x.getName().toLowerCase(Locale.ENGLISH).contains(name.toLowerCase(Locale.ENGLISH)))
                    .collect(Collectors.toList());
        }

        List<EmailTemplate> emailTemplateList = new ArrayList<>();

        for (EmailTemplateEntity entity : foundTemplateEntities) {
            emailTemplateList.add(modelMapper.map(entity, EmailTemplate.class));
        }

        return emailTemplateList;
    }

    /**
     * Retrieves a paginated list of email templates based on specified filters.
     * @param pageable          pagination information.
     * @param active           include only active templates if true.
     * @param hasPlaceholders   include templates with placeholders if true.
     * @param name             filter templates by name.
     * @return      a {@link Page} of {@link EmailTemplate} matching the criteria.
     */
    @Override
    public Page<EmailTemplate> getAllEmailTemplate(Pageable pageable, Boolean active, Boolean hasPlaceholders, String name) {
        Specification<EmailTemplateEntity> spec = TemplateSpecifications.createSpecification(active, hasPlaceholders, name);

        Page<EmailTemplateEntity> emailTemplateEntityPage = emailTemplateRepository.findAll(spec, pageable);

        List<EmailTemplate> emailTemplates = emailTemplateEntityPage.getContent().stream()
                .map(entity -> modelMapper.map(entity, EmailTemplate.class))
                .collect(Collectors.toList());

        return new PageImpl<>(emailTemplates, pageable, emailTemplateEntityPage.getTotalElements());
    }

    /**
     * Updates an email template by its ID.
     * @param updateDto data transfer object that has details of
     * the email template to be updated
     * @return ResponseEntity with the updated Email Template
     * @throws MessagingException if HTML body isn't valid
     */
    @Override
    public EmailTemplate updateEmailTemplate(UpdateEmailTemplateDTO updateDto) throws MessagingException {
        String htmlContent = new String(Base64.getDecoder().decode(updateDto.getBase64body()));
        if (validateHTML(htmlContent)) {
            EmailTemplate emailTemplate = this.getEmailTemplateById(updateDto.getId());
            emailTemplate.setName(updateDto.getName());
            emailTemplate.setBody(updateDto.getBase64body());

            EmailTemplateEntity savedTemplate = emailTemplateRepository.save(modelMapper.map(emailTemplate, EmailTemplateEntity.class));
            return modelMapper.map(savedTemplate, EmailTemplate.class);
        } else {
            throw new MessagingException("Error updating template, the HTML body is INVALID");
        }

    }

    /**
     * Deactivates an email template by setting its status to inactive.
     *
     * @param id the ID of the email template to be deleted.
     * @return true if the operation was successful.
     * @throws NotFoundException if no email template is found with the given ID.
     */
    @Override
    public Boolean deleteEmailTemplate(Long id) {
        Optional<EmailTemplateEntity> templateEntity
                = emailTemplateRepository.findById(id);

        if (templateEntity.isEmpty()) {
            throw new NotFoundException("Template not found");
        }

        templateEntity.get().setActive(false);
        emailTemplateRepository.save(templateEntity.get());
        return true;
    }

    /**
     * This method validates if
     * the html sent is valid or not.
     * @param html which represents the HTML string
     * @return if it's valid or not
     */
    private Boolean validateHTML(String html) {
        Tidy tidy = new Tidy();

        try (StringReader input = new StringReader(html);
             StringWriter output = new StringWriter()) {

            tidy.parse(input, output);

            return tidy.getParseErrors() <= 0;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * Filters a list of email templates to include
     * only those that contain placeholders.
     * @param allTemplates the list of email templates to check
     * @return a list of email templates that contain placeholders
     */
    private List<EmailTemplateEntity> checkTemplatesForVariables(List<EmailTemplateEntity> allTemplates) {
        return allTemplates.stream()
                .filter(templateEntity -> !EmailVariableUtils.extractPlaceholders(templateEntity.getBody()).isEmpty())
                .collect(Collectors.toList());
    }
}
