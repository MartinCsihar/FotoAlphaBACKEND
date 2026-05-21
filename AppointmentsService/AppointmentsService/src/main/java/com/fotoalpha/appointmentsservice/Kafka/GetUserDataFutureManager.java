package com.fotoalpha.appointmentsservice.Kafka;

import com.fotoalpha.appointmentsservice.KafkaEvents.GetUserDataEventResponse;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GetUserDataFutureManager {
    private final Map<String, CompletableFuture<GetUserDataEventResponse>> pending = new ConcurrentHashMap<>();

    public CompletableFuture<GetUserDataEventResponse> createRequest(String correlationId){
        CompletableFuture<GetUserDataEventResponse> future = new CompletableFuture<>();
        pending.put(correlationId, future);
        return future;
    }
    public void complete(String correlationId, GetUserDataEventResponse response){
        // pending.remove(correlationID) returns the CompletableFuture object belonging to the given correlationId
        CompletableFuture<GetUserDataEventResponse> future = pending.remove(correlationId);
        if(future != null){
            future.complete(response);
        }
    }
}
