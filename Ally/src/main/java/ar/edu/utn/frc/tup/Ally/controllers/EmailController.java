package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.email.CreateEmailContactDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.email.CreateEmailDTO;
import ar.edu.utn.frc.tup.lc.iv.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
/**
 * Controller for sending emails.
 */
@RestController
@RequestMapping("emails")
@CrossOrigin(origins = "*")
public class EmailController {

    /**
     * Service to manage the email petitions.
     */
    @Autowired
    private EmailService emailService;


    /**
     * Send an email.
     * @param createEmailDTO which is the data transfer object that contains <p>
     * the precise information of what do you want to send and to who
     * @return void ResponseEntity
     */
    @PostMapping("/send")
    public ResponseEntity<Void> sendEmail(@RequestBody CreateEmailDTO createEmailDTO) {
        emailService.sendEmailWithTemplate(createEmailDTO, null);
        return ResponseEntity.ok().build();
    }

    /**
     * Sends an email to specified contacts using a given template.
     * @param createEmailContactDTO DTO;
     * @return void ResponseEntity
     */
    @PostMapping("/send-to-contacts")
    public ResponseEntity<Void> sendEmailToContacts(@RequestBody CreateEmailContactDTO
                                                                createEmailContactDTO) {
        emailService.sendEmailToContacts(createEmailContactDTO);
        return ResponseEntity.ok().build();
    }
}
