package com.fotoalpha.userratingsservice.Kafka;

import com.fotoalpha.userratingsservice.KafkaEvents.AppInfoResEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Consumer {
    private final AppInfoResEventManager appInfoResEventManager;
    @KafkaListener(topics = "app-info-req.comp", groupId = "userratings-service")
    public void consume(AppInfoResEvent appInfoResEvent) {
        appInfoResEventManager.complete(appInfoResEvent.correlationId(), appInfoResEvent);
    }
}
