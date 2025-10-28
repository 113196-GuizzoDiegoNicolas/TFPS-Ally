package ar.edu.utn.frc.tup.lc.iv.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Interface for providing Telegram messaging services.
 * This includes functionalities for sending text messages,
 * photos, and videos to Telegram chats.
 */
@Service
public interface TelegramService {

    /**
     * Sends a text message to a specified Telegram chat.
     *
     * @param message the message to be sent
     */
    void sendMessage(String message);

    /**
     * Sends a photo with an optional caption to a specified Telegram chat.
     *
     * @param photo  the photo to be sent
     * @param caption the caption for the photo, can be null or empty
     */
    void sendPhoto(MultipartFile photo, String caption);

    /**
     * Sends a video with an optional caption to a specified Telegram chat.
     *
     * @param video  the video to be sent
     * @param caption the caption for the video, can be null or empty
     */
    void sendVideo(MultipartFile video, String caption);


    /**
     * Sends an audio with an optional caption to a specified Telegram chat.
     *
     * @param audio the audio to be sent
     */
    void sendAudio(MultipartFile audio);
}
