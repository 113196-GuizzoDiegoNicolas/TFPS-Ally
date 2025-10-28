package ar.edu.utn.frc.tup.lc.iv.services;

import org.springframework.stereotype.Service;

/**
 * Service interface for interacting with the ChatGPT API.
 * This interface defines the contract for services that provide
 * functionality to generate responses using ChatGPT.
 */
@Service
public interface ChatGptService {

    /**
     * Obtains a response from the ChatGPT API
     * based on the provided request.
     *
     * @param request The input prompt or message
     * from the user for which a response is desired.
     * @return A string containing the generated response from ChatGPT.
     */
    String getChatGPTResponse(String request);

    /**
     * Obtains a response from the ChatGPT API
     * based on dashboard analysis.
     *
     * @param request The input prompt or message
     * from the user for which a response is desired.
     * @return A string containing the generated response from ChatGPT.
     */
    String getChatGPTResponseDashboard(String request);
}
