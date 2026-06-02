package com.fotoalpha.awsservice.Kafka;

import com.fotoalpha.awsservice.Events.SavePhotosEvent;
import com.fotoalpha.awsservice.Events.SaveProfilePictureEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Producer {
    private final KafkaTemplate<String, SavePhotosEvent> speTemplate;
    private final KafkaTemplate<String, SaveProfilePictureEvent> sppTemplate;
    public void sendSavePhotosEvent(SavePhotosEvent savePhotosEvent) {
        speTemplate.send("save-photos", savePhotosEvent);
    }

    public void sendSaveProfilePictureEvent(SaveProfilePictureEvent event) {
        sppTemplate.send("save-profile-picture", event);
    }
}
