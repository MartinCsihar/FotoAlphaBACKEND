package com.fotoalpha.userratingsservice.Kafka;

import com.fotoalpha.userratingsservice.KafkaEvents.AppInfoResEvent;
import com.fotoalpha.userratingsservice.KafkaEvents.UserInfoReqEvent;
import com.fotoalpha.userratingsservice.KafkaEvents.UserInfoResEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component

public class UserInfoResManagger {
    private final Map<String, CompletableFuture<UserInfoResEvent>> userInfoResEvent = new ConcurrentHashMap<>();

    public CompletableFuture<UserInfoResEvent> createRequest(String correlationId) {
        CompletableFuture<UserInfoResEvent> future = new CompletableFuture<>();
        userInfoResEvent.put(correlationId, future);
        return future;
    }
    public void complete(String correlationId, UserInfoResEvent appInfoReqEvent) {
        CompletableFuture<UserInfoResEvent> future = userInfoResEvent.remove(correlationId);
        if (future != null) {
            future.complete(appInfoReqEvent);
        }
    }
}
