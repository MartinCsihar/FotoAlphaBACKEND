package com.fotoalpha.userratingsservice.Kafka;

import com.fotoalpha.userratingsservice.KafkaEvents.AppInfoReqEvent;
import com.fotoalpha.userratingsservice.KafkaEvents.AppInfoResEvent;
import com.fotoalpha.userratingsservice.RequestsResponses.AppointmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
public class Producer {
    private final AppInfoResEventManager aireManager;
    private final KafkaTemplate<String, AppInfoReqEvent> appInfoReqTemplate;

    public AppInfoResEvent sendAppInfoReqEvent(AppInfoReqEvent appInfoReqEvent) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<AppInfoResEvent> future = aireManager.createRequest(appInfoReqEvent.correlationId());

        appInfoReqTemplate.send("app-info-req", appInfoReqEvent);

        return future.get(5, TimeUnit.SECONDS);
    }
}
