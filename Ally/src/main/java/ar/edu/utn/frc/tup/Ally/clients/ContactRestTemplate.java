package ar.edu.utn.frc.tup.lc.iv.clients;

import ar.edu.utn.frc.tup.lc.iv.clients.dtos.ContactResponseDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.exceptions.ClientNotAvailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Service class for interacting with the Contact API using RestTemplate.
 * Provides methods to retrieve contact details by their IDs.
 */
@NoArgsConstructor
@Service
public class ContactRestTemplate {

    /**
     * RestTemplate for making HTTP requests to external services.
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Base URL of the Contact API, injected from application properties.
     */
    @Value("${contact.api.url}")
    private String contactApiUrl;

    /**
     *  Name of the fallback method called when the circuit breaker is triggered.
     */
    private static final String FALLBACK_METHOD = "fallback";

    /**
     * Name of the circuit breaker service for contact retrieval.
     */
    private static final String CIRCUIT_SERVICE = "contacts_service";


    /**
     * Retrieves contact details for the given list of IDs by making a GET
     * request to the Contact API.
     * @param ids the list of contact IDs to retrieve
     * @return ResponseEntity containing an array of ContactResponseDTO
     * with contact details
     */
    @CircuitBreaker(name = CIRCUIT_SERVICE, fallbackMethod = FALLBACK_METHOD)
    public ResponseEntity<ContactResponseDTO[]> getContactsByIds(List<Long> ids) {
        // Build the queryParams
        String idsParam = String.join("&ids=", ids.stream()
                .map(String::valueOf)
                .toArray(String[]::new));

        String url = contactApiUrl + "/by-ids?ids=" + idsParam;

        return restTemplate.getForEntity(url, ContactResponseDTO[].class);
    }

    /**
     * Fallback method for getContactsByIds.
     *
     * @param ids the list of contact IDs
     * @param ex the exception that caused the fallback
     * @return ResponseEntity with an empty array of ContactResponseDTO
     */
    public ResponseEntity<ContactResponseDTO[]> fallback(List<Long> ids, Throwable ex) {
        throw new ClientNotAvailableException("Service Contacts didn't respond, please try"
                + "again later");
    }
}
