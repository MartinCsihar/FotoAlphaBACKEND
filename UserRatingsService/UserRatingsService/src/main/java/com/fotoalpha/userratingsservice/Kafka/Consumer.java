package com.fotoalpha.userratingsservice.Kafka;

import com.fotoalpha.userratingsservice.KafkaEvents.AppInfoResEvent;
import com.fotoalpha.userratingsservice.KafkaEvents.UserInfoResEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Consumer {
    private final AppInfoResEventManager appInfoResEventManager;
    private final UserInfoResManagger userInfoResManagger;
    @KafkaListener(topics = "app-info-req.comp", groupId = "userratings-service")
    public void consume(AppInfoResEvent appInfoResEvent) {
        appInfoResEventManager.complete(appInfoResEvent.correlationId(), appInfoResEvent);
    }

    @KafkaListener(topics = "user-profpic-req.comp", groupId = "userratings-service")
    public void consume(UserInfoResEvent userInfoResEvent) {
        userInfoResManagger.complete(userInfoResEvent.correlationId(), userInfoResEvent);

    }
}
