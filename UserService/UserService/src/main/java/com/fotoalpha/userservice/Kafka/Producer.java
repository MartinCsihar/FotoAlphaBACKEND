package com.fotoalpha.userservice.Kafka;

import com.fotoalpha.userservice.KafkaEvents.GalleryUpdatedEvent;
import com.fotoalpha.userservice.KafkaEvents.GetUserDataEventResponse;
import com.fotoalpha.userservice.KafkaEvents.SendMailEvent;
import com.fotoalpha.userservice.KafkaEvents.UserInfoResEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Producer {
    private final KafkaTemplate<String, GetUserDataEventResponse> guderTemplate;
    private final KafkaTemplate<String, SendMailEvent> smTemplate;
    private final KafkaTemplate<String, GalleryUpdatedEvent> gueTemplate;
    private final KafkaTemplate<String, UserInfoResEvent> uirTemplate;

    public void sendGUDER(GetUserDataEventResponse response) {
        guderTemplate.send("get-user-data.comp", response);
    }

    public void sendPwResetEmailEvent(SendMailEvent event) {
        smTemplate.send("pw-reset-send-email", event);
    }

    public void sendGalleryUpdatedEvent(GalleryUpdatedEvent gue) {
        gueTemplate.send("gallery-updated", gue);
    }

    public void sendUIR(UserInfoResEvent event) {
        uirTemplate.send("user-profpic-req.comp", event);
    }
}
