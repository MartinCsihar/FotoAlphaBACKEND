package com.fotoalpha.userratingsservice.Kafka;

import com.fotoalpha.userratingsservice.KafkaEvents.AppInfoReqEvent;
import com.fotoalpha.userratingsservice.KafkaEvents.AppInfoResEvent;
import com.fotoalpha.userratingsservice.KafkaEvents.UserInfoReqEvent;
import com.fotoalpha.userratingsservice.KafkaEvents.UserInfoResEvent;
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
    private final UserInfoResManagger uirManager;
    private final AppInfoResEventManager aireManager;
    private final KafkaTemplate<String, AppInfoReqEvent> appInfoReqTemplate;
    private final KafkaTemplate<String, UserInfoReqEvent> userInfoReqTemplate;

    public AppInfoResEvent sendAppInfoReqEvent(AppInfoReqEvent appInfoReqEvent) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<AppInfoResEvent> future = aireManager.createRequest(appInfoReqEvent.correlationId());

        appInfoReqTemplate.send("app-info-req", appInfoReqEvent);

        return future.get(5, TimeUnit.SECONDS);
    }

    public UserInfoResEvent sendUserInfoReqEvent(UserInfoReqEvent userInfoReqEvent) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<UserInfoResEvent> future = uirManager.createRequest(userInfoReqEvent.correlationId());
        userInfoReqTemplate.send("user-profpic-req", userInfoReqEvent);
        return future.get(5, TimeUnit.SECONDS);
    }
}
