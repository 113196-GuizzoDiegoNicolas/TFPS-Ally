package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.emailTemplate.CreateEmailTemplateDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.emailTemplate.UpdateEmailTemplateDTO;
import ar.edu.utn.frc.tup.lc.iv.models.EmailTemplate;
import ar.edu.utn.frc.tup.lc.iv.services.EmailTemplateService;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

/**
 * Controller for managing email templates.
 * <p>
 *      From here you can create, delete, modify and get the templates.
 * </p>
 */
@RestController
@RequestMapping("/email-templates")
@CrossOrigin(origins = "*")
public class EmailTemplateController {

    /**
     * Service for managing email templates.
     */
    @Autowired
    private EmailTemplateService emailTemplateService;

    /**
     * A Mapper to map between DTOs and entities.
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Creates a email template.
     * @param createDto data transfer object that has details of
     * the email template to be created
     * @return ResponseEntity with the created Email Template
     * @throws MessagingException if HTML body isn't valid
     */
    @PostMapping()
    public ResponseEntity<EmailTemplate> createEmailTemplate(@RequestBody CreateEmailTemplateDTO createDto) throws MessagingException {
        EmailTemplate result =
                emailTemplateService.createEmailTemplate(createDto);
        return ResponseEntity.ok(result);
    }

    /**
     * Updates an email template by its ID.
     * @param updateDto data transfer object that has details of
     * the email template to be updated.
     * @return ResponseEntity with the updated Email Template.
     * @throws MessagingException if HTML body isn't valid
     */
    @PatchMapping()
    public ResponseEntity<EmailTemplate> updateEmailTemplate(@RequestBody UpdateEmailTemplateDTO updateDto) throws MessagingException {
        return ResponseEntity.ok(emailTemplateService.updateEmailTemplate(updateDto));
    }

    /**
     * Return all the template available.
     * @param hasPlaceholders Optional filter to return templates with placeholders.
     * @param name Optional Seach by name.
     * @return ResponseEntity List of EmailTemplate with all the templates available
     */
    @GetMapping("")
    public ResponseEntity<List<EmailTemplate>> getAllTemplate(@RequestParam(value = "has_placeholders") Optional<Boolean> hasPlaceholders,
                                                              @RequestParam(required = false) String name) {
        return ResponseEntity.ok(emailTemplateService.getAllEmailTemplate(hasPlaceholders.orElse(null), name));
    }

    /**
     * Return an email template by its ID.
     * @param id which is the template ID
     * @return ResponseEntity with the EmailTemplate
     */
    @GetMapping("{id}")
    public ResponseEntity<EmailTemplate> getTemplateById(@PathVariable Long id) {
        return ResponseEntity.ok(emailTemplateService.getEmailTemplateById(id));
    }

    /**
     * @param hasPlaceholders Optional filter to return templates with placeholders.
     * @param active Optional filter to return only active templates.
     * @param name Optional search parameter to filter templates by name.
     * @param pageable Pagination information (page number, size, etc.).
     * @return ResponseEntity containing a Page with all available templates.
     */
    @GetMapping("pageable")
    public ResponseEntity<Page<EmailTemplate>> getAllTemplatePageable(
            @RequestParam(value = "has_placeholders", required = false) Boolean hasPlaceholders,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String name,
            Pageable pageable) {

        Page<EmailTemplate> result = emailTemplateService.getAllEmailTemplate(pageable, active, hasPlaceholders, name);

        return ResponseEntity.ok(result);
    }

    /**
     * Deletes an email template by its ID.
     * @param id the ID of the email template to delete.
     * @return ResponseEntity containing a Boolean
     * indicating the result of the deletion operation.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> deleteEmailTemplate(@PathVariable Long id) {
        Boolean isDeleted = emailTemplateService.deleteEmailTemplate(id);
        return ResponseEntity.ok(isDeleted);
    }

}

