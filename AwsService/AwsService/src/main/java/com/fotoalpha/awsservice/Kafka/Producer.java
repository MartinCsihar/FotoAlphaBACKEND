package com.fotoalpha.awsservice.Kafka;

import com.fotoalpha.awsservice.Events.SavePhotosEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Producer {
    private final KafkaTemplate<String, SavePhotosEvent> kafkaTemplate;
    public void sendSavePhotosEvent(SavePhotosEvent savePhotosEvent) {
        kafkaTemplate.send("save-photos", savePhotosEvent);
    }
}
