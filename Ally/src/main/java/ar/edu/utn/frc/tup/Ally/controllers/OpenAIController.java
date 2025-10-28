package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.services.ChatGptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * REST controller for handling requests
 * related to OpenAI functionalities.
 * This controller provides an endpoint to create
 * email templates using AI.
 */
@RestController
@RequestMapping("openAI")
@CrossOrigin(origins = "*")
public class OpenAIController {

    /**
     * Service for interacting with the ChatGPT API.
     */
    @Autowired
    private ChatGptService chatGptService;

    /**
     * Creates an email template using AI by
     * invoking the ChatGPT service.
     *
     * @param request The request body containing the
     * prompt for the AI to generate a template.
     * @return ResponseEntity containing the generated email template.
     */
    @PostMapping("/template")
    public ResponseEntity<String> createTemplateWithAI(@RequestBody String request) {
        String sanitizedRequest = removeAccentsAndSpecialCharacters(request);

        String result = chatGptService.getChatGPTResponse(sanitizedRequest);
        return ResponseEntity.ok(result);
    }

    /**
     * Creates an analysis based on a dashboard.
     * invoking the ChatGPT service.
     *
     * @param request The request body containing the
     * prompt for the AI to generate a template.
     * @return ResponseEntity containing the generated email template.
     */
    @PostMapping("/dashboard")
    public ResponseEntity<String> dashboardSummary(@RequestBody String request) {
        String sanitizedRequest = removeAccentsAndSpecialCharacters(request);

        String result = chatGptService.getChatGPTResponseDashboard(sanitizedRequest);
        return ResponseEntity.ok(result);
    }


    /**
     * Removes accents and special characters from the given input string.
     * @param input The input string from which accents and
     * special characters will be removed.
     * @return A string without diacritical marks and special characters
     * containing only letters, numbers, and spaces.
     */
    private String removeAccentsAndSpecialCharacters(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String withoutDiacritics = pattern.matcher(normalized).replaceAll("");
        return withoutDiacritics.replaceAll("[^\\p{L}\\p{N} ]", "").trim();
    }
}
