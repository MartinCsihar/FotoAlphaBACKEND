package com.fotoalpha.userservice.Kafka;

import com.fotoalpha.userservice.KafkaEvents.GetUserDataEventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Producer {
    private final KafkaTemplate<String, GetUserDataEventResponse> guderTemplate;

    public void sendGUDER(GetUserDataEventResponse response) {
        guderTemplate.send("get-user-data.comp", response);
    }
}
