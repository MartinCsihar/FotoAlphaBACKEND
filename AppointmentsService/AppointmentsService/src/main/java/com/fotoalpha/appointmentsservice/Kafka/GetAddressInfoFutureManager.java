package com.fotoalpha.appointmentsservice.Kafka;

import com.fotoalpha.appointmentsservice.KafkaEvents.GetAddressesReqEvent;
import com.fotoalpha.appointmentsservice.KafkaEvents.GetAddressesResEvent;
import com.fotoalpha.appointmentsservice.KafkaEvents.GetUserDataEventResponse;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GetAddressInfoFutureManager {
    private final Map<String, CompletableFuture<GetAddressesResEvent>> pending = new ConcurrentHashMap<>();

    public CompletableFuture<GetAddressesResEvent> createRequest(String correlationId){
        CompletableFuture<GetAddressesResEvent> future = new CompletableFuture<>();
        pending.put(correlationId, future);
        return future;
    }
    public void complete(String correlationId, GetAddressesResEvent response){
        // pending.remove(correlationID) returns the CompletableFuture object belonging to the given correlationId
        CompletableFuture<GetAddressesResEvent> future = pending.remove(correlationId);
        if(future != null){
            future.complete(response);
        }
    }
}
