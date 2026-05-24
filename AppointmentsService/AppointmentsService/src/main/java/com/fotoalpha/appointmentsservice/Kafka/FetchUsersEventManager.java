package com.fotoalpha.appointmentsservice.Kafka;

import com.fotoalpha.appointmentsservice.KafkaEvents.FetchUsersEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FetchUsersEventManager {
    private final Map<String, CompletableFuture<FetchUsersEvent>> fetchUsersEvents = new ConcurrentHashMap<>();

    public CompletableFuture<FetchUsersEvent> createRequest(String correlationId) {
        CompletableFuture<FetchUsersEvent> future = new CompletableFuture<>();
        fetchUsersEvents.put(correlationId, future);
        return future;
    }
    public void complete(FetchUsersEvent fetchUsersEvent, String correlationId) {
        CompletableFuture<FetchUsersEvent> future = fetchUsersEvents.remove(correlationId);
        if (future != null) {
            future.complete(fetchUsersEvent);
        }
    }
}
