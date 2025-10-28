package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.clients.ContactRestTemplate;
import ar.edu.utn.frc.tup.lc.iv.clients.dtos.ContactResponseDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.email.CreateEmailContactDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.email.CreateEmailDTO;
import ar.edu.utn.frc.tup.lc.iv.models.ContactType;
import ar.edu.utn.frc.tup.lc.iv.models.EmailTemplate;
import ar.edu.utn.frc.tup.lc.iv.models.KeyValueCustomPair;
import ar.edu.utn.frc.tup.lc.iv.models.EmailNotification;
import ar.edu.utn.frc.tup.lc.iv.services.EmailService;
import ar.edu.utn.frc.tup.lc.iv.services.EmailTemplateService;
import ar.edu.utn.frc.tup.lc.iv.services.NotificationService;
import ar.edu.utn.frc.tup.lc.iv.services.utils.EmailVariableUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.w3c.tidy.Tidy;


/**
 * Implementation of the Email service for operate email sending.
 */
@Service
@NoArgsConstructor
public class EmailServiceImpl implements EmailService {

    /**
     * Email Template service that allows search which template will be used.
     */
    @Autowired
    private EmailTemplateService emailTemplateService;

    /**
     * Dependency injection for the NotificationService.
     */
    @Autowired
    private NotificationService notificationService;
    /**
     * Service that provides the email sending.
     */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Rest template to use contacts microservice.
     */
    @Autowired
    private ContactRestTemplate contactRestTemplate;

    /**
     * Email Sender.
     */
    private static final String EMAIL_FROM =
            "noreplynotificationsdev@gmail.com";

    /**
     * PlaceHolder variable pattern.
     */
    private static final Pattern PLACEHOLDER_PATTERN =
            Pattern.compile("\\{\\{(.*?)\\}\\}");

    /**
     * Send an email with a template.
     * @param createEmailDTO data transfer object.
     * @param contactId (optional) the contact identifier.
     */
    @Override
    public void sendEmailWithTemplate(CreateEmailDTO createEmailDTO, Long contactId) {
        try {
            EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateById(createEmailDTO.getTemplateId());
            String processedBody = processTemplate(emailTemplate.getBody(), createEmailDTO.getVariables());
            sendEmail(createEmailDTO.getRecipient(), createEmailDTO.getSubject(), processedBody);

            EmailNotification emailNotification = new EmailNotification();
            emailNotification.setBody(processedBody);
            emailNotification.setRecipient(createEmailDTO.getRecipient());
            emailNotification.setTemplateId(createEmailDTO.getTemplateId());
            emailNotification.setTemplateName(emailTemplate.getName());
            emailNotification.setSubject(createEmailDTO.getSubject());
            if (contactId != null) {
                emailNotification.setContactId(contactId);
            }

            notificationService.createNotification(emailNotification);
        } catch (MessagingException e) {
            throw new IllegalArgumentException("Error sending email", e);
        }
    }

    /**
     * Processes the template by replacing placeholders with the
     * provided key-value pairs.
     * @param template  The template string containing placeholders like {{key}}.
     * @param variables The list of KeyValueCustomPair containing keys and values.
     * @return The processed template with placeholders replaced
     * by their corresponding values.
     */
    public String processTemplate(String template, List<KeyValueCustomPair> variables) {
        Set<String> requiredKeys = EmailVariableUtils.extractPlaceholders(template);

        // Extract keys from the provided variables
        Set<String> providedKeys = variables.stream()
                .map(KeyValueCustomPair::getKey)
                .collect(Collectors.toSet());

        // If keys do not match, throw a BadRequestException
        if (!requiredKeys.equals(providedKeys)) {
            throw new IllegalArgumentException("Required keys not match");
        }

        String processedTemplate = template;

        // Replace placeholders with values
        for (KeyValueCustomPair entry : variables) {
            processedTemplate = processedTemplate.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        return processedTemplate;
    }

    /**
     * Sends an email using the JavaMailSender.
     * @param to recipient's email address
     * @param subject subject of the email
     * @param body body of the email
     */
    @SneakyThrows
    private void sendEmail(String to, String subject, String body) {
        if (validateHTML(body)) {
            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
                helper.setText(body, true);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setFrom(EMAIL_FROM);

                mailSender.send(mimeMessage);

            } catch (MessagingException e) {
                throw new MessagingException("Error sending email", e);
            }
        } else {
            throw new MessagingException("Error sending email, the HTML body is INVALID");
        }

    }

    /**
     * Sends an email to a list of contacts using the
     * provided {@link CreateEmailContactDTO}.
     * @param createEmailContactDTO the DTO containing contact IDs and email details.
     * @throws IllegalArgumentException if a contact's type is not "EMAIL".
     */
    @Override
    public void sendEmailToContacts(CreateEmailContactDTO createEmailContactDTO) {
        ContactResponseDTO[] response = contactRestTemplate
                .getContactsByIds(createEmailContactDTO.getContactIds()).getBody();

        CreateEmailDTO createEmailDTO = new CreateEmailDTO();
        createEmailDTO.setSubject(createEmailContactDTO.getSubject());
        createEmailDTO.setTemplateId(createEmailContactDTO.getTemplateId());
        createEmailDTO.setVariables(createEmailContactDTO.getVariables());

        EmailNotification emailNotification = new EmailNotification();
        emailNotification.setTemplateName(emailTemplateService
                .getEmailTemplateById(createEmailContactDTO.getTemplateId()).getName());
        emailNotification.setSubject(createEmailContactDTO.getSubject());
        emailNotification.setTemplateId(createEmailContactDTO.getTemplateId());

        for (ContactResponseDTO contact : response) {
            if (!ContactType.EMAIL.equals(contact.getContactType())) {
                throw new IllegalArgumentException("Invalid contact type of contact id: "
                        + contact.getId());
            }

            if (createEmailContactDTO.getNotificationType() != null
                    && !contact.getSubscriptions().contains(createEmailContactDTO.getNotificationType().toUpperCase(Locale.ROOT))) {
                continue;
            }
            createEmailDTO.setRecipient(contact.getContactValue());
            sendEmailWithTemplate(createEmailDTO, contact.getId());
        }

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
}
