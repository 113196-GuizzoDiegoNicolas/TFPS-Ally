package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.dtos.email.CreateEmailContactDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.email.CreateEmailDTO;
import org.springframework.stereotype.Service;

/**
 * Service Interface to manage sending emails.
 */
@Service
public interface EmailService {
    /**
     * Sends an email.
     * @param createEmailDTO data transfer object that contains info needed to sending
     * @param contactId (optional) identifier of the contact.
     */
    void sendEmailWithTemplate(CreateEmailDTO createEmailDTO, Long contactId);

    /**
     * Sends an email to known contacts.
     * @param createEmailContactDTO data transfer object that contains
     * info needed to sending
     */
     void sendEmailToContacts(CreateEmailContactDTO createEmailContactDTO);

    }
