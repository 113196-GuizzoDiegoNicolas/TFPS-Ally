package ar.edu.utn.frc.tup.lc.iv.clients;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service class for interacting with the OpenAI API
 * using RestTemplate.
 */
@NoArgsConstructor
@Service
public class ChatGptRestTemplate {

    /**
     * RestTemplate used to perform HTTP requests
     * to external services.
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * The URL of the OpenAI API endpoint.
     */
    @Value("${openai.api.url}")
    private String apiUrl;

    /**
     * The API key used for authentication with the OpenAI API.
     */
    @Value("${openai.api.key}")
    private String apiKey;

    /**
     * Sends a chat request to the OpenAI API
     * and returns the response.
     * @param jsonBody The JSON body of the request
     * @return ResponseEntity containing the response
     * from the API as a string.
     */
    public ResponseEntity<String> getChatResponse(String jsonBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        return restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                request,
                String.class
        );
    }
}
