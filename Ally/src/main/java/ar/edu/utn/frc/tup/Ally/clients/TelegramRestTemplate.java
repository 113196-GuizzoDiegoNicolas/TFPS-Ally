package ar.edu.utn.frc.tup.lc.iv.clients;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Service for sending messages via the Telegram API.
 * Utilizes a {@link RestTemplate} to perform HTTP
 * requests to the Telegram service
 */
@NoArgsConstructor
@Service
public class TelegramRestTemplate {

    /**
     * RestTemplate used to perform HTTP requests to external services.
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Base URL of the Telegram API for sending messages.
     * The bot token is injected at runtime.
     */
    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot{token}";

    /**
     * Token of the target chat to send messages.
     */
    private static final String TOKEN_PLACEHOLDER = "{token}";

    /**
     * Telegram bot token used for authenticating with the Telegram API.
     * Injected from the properties file.
     */
    @Value("${telegram.token}")
    private String botToken;

    /**
     * Chat or channel ID to which messages will be sent.
     * Injected from the properties file.
     */
    @Value("${telegram.channel}")
    private String chatId;

    /**
     * Sends a message to the specified Telegram chat or channel.
     *
     * @param message the text message to be sent
     * @return a {@link ResponseEntity} containing the
     * response body from the Telegram API
     */
    public ResponseEntity<String> sendMessage(String message) {
        String url = TELEGRAM_API_URL.replace(TOKEN_PLACEHOLDER, botToken) + "/sendMessage";
        String fullMessage = String.format("{\"chat_id\":\"%s\",\"text\":\"%s\"}", chatId, message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(fullMessage, headers);
        String response = restTemplate.postForEntity(url, requestEntity, String.class).getBody();
        return ResponseEntity.ok(response);
    }
    /**
     * Sends a photo to the specified Telegram chat or channel.
     *
     * @param photo   the photo to be sent
     * @param caption the caption for the photo
     * @return a {@link ResponseEntity} containing the
     * response body from the Telegram API
     */
    public ResponseEntity<String> sendPhoto(MultipartFile photo, String caption) {
        String url = TELEGRAM_API_URL.replace(TOKEN_PLACEHOLDER, botToken) + "/sendPhoto";

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("chat_id", chatId);
        map.add("caption", caption);

        try {
            byte[] bytes = photo.getBytes();

            map.add("photo", new ByteArrayResource(bytes) {
                @Override
                public String getFilename() {
                    return photo.getOriginalFilename();
                }
            });

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading the file.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        ResponseEntity<String> response;
        response = restTemplate.postForEntity(url, requestEntity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        }  else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending photo.");
        }

    }

    /**
     * Sends a video to the specified Telegram chat or channel.
     *
     * @param video   the video file to be sent
     * @param caption the caption for the video
     * @return a {@link ResponseEntity} containing
     * the response body from the Telegram API
     */
    public ResponseEntity<String> sendVideo(MultipartFile video, String caption) {
        String url = TELEGRAM_API_URL.replace(TOKEN_PLACEHOLDER, botToken) + "/sendVideo";

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("chat_id", chatId);
        map.add("caption", caption);

        try {
            byte[] bytes = video.getBytes();
            map.add("video", new ByteArrayResource(bytes) {
                @Override
                public String getFilename() {
                    return video.getOriginalFilename();
                }
            });

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading the video file.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending video.");
        }
    }

    /**
     * Sends an audio file to the specified Telegram chat or channel.
     *
     * @param audio   the audio file to be sent
     * @return a {@link ResponseEntity} containing the
     * response body from the Telegram API
     */
    public ResponseEntity<String> sendAudio(MultipartFile audio) {
        String url = TELEGRAM_API_URL.replace(TOKEN_PLACEHOLDER, botToken) + "/sendAudio";

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("chat_id", chatId);

        try {
            byte[] bytes = audio.getBytes();
            map.add("audio", new ByteArrayResource(bytes) {
                @Override
                public String getFilename() {
                    return audio.getOriginalFilename();
                }
            });

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading the audio file.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending audio.");
        }
    }

}

