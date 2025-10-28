package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.clients.TelegramRestTemplate;
import ar.edu.utn.frc.tup.lc.iv.services.TelegramService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementation of the {@link TelegramService} interface for sending messages,
 * photos, and videos via Telegram.
 */
@Service
@NoArgsConstructor
public class TelegramServiceImpl implements TelegramService {
    /**
     * Rest template to use telegram api.
     */
    @Autowired
    private TelegramRestTemplate telegramRestTemplate;

    /**
     * Sends a text message to a specified Telegram chat.
     *
     * @param message the message to be sent
     * @throws IllegalArgumentException if the message could not be sent
     */
    @Override
    public void sendMessage(String message) {
        try {
            telegramRestTemplate.sendMessage(message);
        } catch (Exception e) {
            throw new IllegalArgumentException("Couldn't send message: " + e.getMessage(), e);
        }
    }

    /**
     * Sends a photo with an optional caption to a specified Telegram chat.
     *
     * @param photo  the photo to be sent
     * @param caption the caption for the photo
     * @throws IllegalArgumentException if the photo could not be sent
     */
    @Override
    public void sendPhoto(MultipartFile photo, String caption) {
        try {
            telegramRestTemplate.sendPhoto(photo, caption);
        } catch (Exception e) {
            throw new IllegalArgumentException("Couldn't send photo: " + e.getMessage(), e);
        }
    }

    /**
     * Sends a video with an optional caption to a specified Telegram chat.
     *
     * @param video  the video to be sent
     * @param caption the caption for the video
     * @throws IllegalArgumentException if the video could not be sent
     */
    @Override
    public void sendVideo(MultipartFile video, String caption) {
        try {
            telegramRestTemplate.sendVideo(video, caption);
        } catch (Exception e) {
            throw new IllegalArgumentException("Couldn't send video: " + e.getMessage(), e);
        }
    }
    /**
     * Sends an audi with an optional caption to a specified Telegram chat.
     *
     * @param audio  the audio to be sent
     * @throws IllegalArgumentException if the audio could not be sent
     */
    @Override
    public void sendAudio(MultipartFile audio) {
        try {
            telegramRestTemplate.sendAudio(audio);
        } catch (Exception e) {
            throw new IllegalArgumentException("Couldn't send audio: " + e.getMessage(), e);
        }
    }
}
