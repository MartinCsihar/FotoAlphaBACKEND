package com.fotoalpha.userservice.Kafka;

import com.fotoalpha.userservice.KafkaEvents.GetUserDataEvent;
import com.fotoalpha.userservice.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Consumer {
    private final UserService userService;
    @KafkaListener(topics = "get-user-data", groupId = "user-service")
    public void consume(GetUserDataEvent event){
        userService.createGetUserDataResponse(event);
    }
}
