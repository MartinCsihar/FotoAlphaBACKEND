package com.fotoalpha.appointmentsservice.Kafka;

import com.fotoalpha.appointmentsservice.KafkaEvents.*;
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
    private final KafkaTemplate<String, SaveAddressEvent> kafkaAddressTemplate;
    private final KafkaTemplate<String, AppointmentCreatedEvent> kafkaAppointmentTemplate;
    private final KafkaTemplate<String, GetUserDataEvent> kafkaGetUserDataTemplate;
    private final KafkaTemplate<String, FetchUsersEvent> kafkaFetchUsersTemplate;
    private final GetUserDataFutureManager gudeManager;
    private final FetchUsersEventManager fueManager;

    public void sendSaveAddressEvent(SaveAddressEvent saveAddress) {
        kafkaAddressTemplate.send("save-address", saveAddress);
    }

    public void sendAppCreatedEvent(AppointmentCreatedEvent appCreatedEvent) {
        kafkaAppointmentTemplate.send("appointment-created", appCreatedEvent);
    }

    public GetUserDataEventResponse requestUserData(GetUserDataEvent getUserDataEvent) throws ExecutionException, InterruptedException, TimeoutException {

        CompletableFuture<GetUserDataEventResponse> future = gudeManager.createRequest(getUserDataEvent.correlationId());

        kafkaGetUserDataTemplate.send("get-user-data", getUserDataEvent);
        return future.get(5, TimeUnit.SECONDS);
    }
    public FetchUsersEvent sendFetchUsersEvent(FetchUsersEvent fetchUsersEvent) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<FetchUsersEvent> future = fueManager.createRequest(fetchUsersEvent.correlationId());

        kafkaFetchUsersTemplate.send("fetch-users", fetchUsersEvent);
        return future.get(5, TimeUnit.SECONDS);
    }
}
