package com.fotoalpha.userratingsservice.Kafka;

import com.fotoalpha.userratingsservice.KafkaEvents.AppInfoReqEvent;
import com.fotoalpha.userratingsservice.KafkaEvents.AppInfoResEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AppInfoResEventManager {
    private final Map<String, CompletableFuture<AppInfoResEvent>> appInfoReqEventMap = new ConcurrentHashMap<>();

    public CompletableFuture<AppInfoResEvent> createRequest(String correlationId) {
        CompletableFuture<AppInfoResEvent> future = new CompletableFuture<>();
        appInfoReqEventMap.put(correlationId, future);
        return future;
    }
    public void complete(String correlationId, AppInfoResEvent appInfoReqEvent) {
        CompletableFuture<AppInfoResEvent> future = appInfoReqEventMap.remove(correlationId);
        if (future != null) {
            future.complete(appInfoReqEvent);
        }
    }
}
