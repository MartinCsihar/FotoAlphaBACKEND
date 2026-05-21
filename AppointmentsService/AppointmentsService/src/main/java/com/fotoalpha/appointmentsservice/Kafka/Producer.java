package com.fotoalpha.appointmentsservice.Kafka;

import com.fotoalpha.appointmentsservice.KafkaEvents.AppointmentCreatedEvent;
import com.fotoalpha.appointmentsservice.KafkaEvents.GetUserDataEvent;
import com.fotoalpha.appointmentsservice.KafkaEvents.GetUserDataEventResponse;
import com.fotoalpha.appointmentsservice.KafkaEvents.SaveAddressEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;
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
    private final GetUserDataFutureManager manager;

    public void sendSaveAddressEvent(SaveAddressEvent saveAddress) {
        kafkaAddressTemplate.send("save-address", saveAddress);
    }

    public void sendAppCreatedEvent(AppointmentCreatedEvent appCreatedEvent) {
        kafkaAppointmentTemplate.send("appointment-created", appCreatedEvent);
    }

    public GetUserDataEventResponse requestUserData(GetUserDataEvent getUserDataEvent) throws ExecutionException, InterruptedException, TimeoutException {

        CompletableFuture<GetUserDataEventResponse> future = manager.createRequest(getUserDataEvent.correlationId());

        kafkaGetUserDataTemplate.send("get-user-data", getUserDataEvent);
        return future.get(5, TimeUnit.SECONDS);
    }
}
