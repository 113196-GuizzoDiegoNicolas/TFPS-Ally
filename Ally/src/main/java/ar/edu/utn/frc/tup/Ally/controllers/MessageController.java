package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.services.TelegramService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for managing Telegram messages.
 * Provides endpoints for sending messages, photos, videos, and audio to Telegram.
 */
@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    /**
     * Service to manage Telegram messaging functionalities.
     */
    @Autowired
    private TelegramService telegramService;


    /**
     * Sends a message, photo, or video to a Telegram channel.
     * @param message the message to send (required)
     * @param photo   optional photo to send
     * @param video   optional video to send
     * @return ResponseEntity<Void> indicating the status of the request
     */
    @PostMapping(value = "/telegram", consumes = "multipart/form-data")
    public ResponseEntity<Void> sendMessage(
            @Parameter(description = "Message to send", required = true)
            @RequestParam(value = "message") String message,

            @Parameter(description = "Optional photo to send")
            @RequestParam(value = "photo", required = false) MultipartFile photo,

            @Parameter(description = "Optional video to send")
            @RequestParam(value = "video", required = false) MultipartFile video) {

        if (video != null && !video.isEmpty()) {
            telegramService.sendVideo(video, message);
        } else if (photo != null && !photo.isEmpty()) {
            telegramService.sendPhoto(photo, message);
        } else {
            telegramService.sendMessage(message);
        }
        return ResponseEntity.ok().build();
    }


    /**
     * Sends audio to a Telegram channel.
     * @param audio   the audio file to send (required)
     * @return ResponseEntity<Void> indicating the status of the request
     */
    @PostMapping(value = "/telegram/audio", consumes = "multipart/form-data")
    public ResponseEntity<Void> sendAudio(
            @Parameter(description = "Audio to send", required = true)
            @RequestParam(value = "audio") MultipartFile audio) {

        if (audio != null && !audio.isEmpty()) {
            telegramService.sendAudio(audio);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}

