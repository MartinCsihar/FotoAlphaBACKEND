package com.fotoalpha.appointmentsservice.Kafka;

import com.fotoalpha.appointmentsservice.KafkaEvents.GetUserDataEventResponse;
import com.fotoalpha.appointmentsservice.Service.appService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Consumer {
    private final GetUserDataFutureManager manager;
    @KafkaListener(topics = "get-user-data.comp", groupId = "appointments-service")
    public void getUserDataEvent(GetUserDataEventResponse getUserDataEventResponse) {
        manager.complete(getUserDataEventResponse.correlationId(), getUserDataEventResponse);
    }
}
