package com.fotoalpha.appointmentsservice.Kafka;

import com.fotoalpha.appointmentsservice.KafkaEvents.AppInfoReqEvent;
import com.fotoalpha.appointmentsservice.KafkaEvents.FetchUsersEvent;
import com.fotoalpha.appointmentsservice.KafkaEvents.GetUserDataEventResponse;
import com.fotoalpha.appointmentsservice.Service.appService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Consumer {
    private final GetUserDataFutureManager gudeManager;
    private final FetchUsersEventManager fueManager;
    private final appService appService;
    @KafkaListener(topics = "get-user-data.comp", groupId = "appointments-service")
    public void getUserDataEvent(GetUserDataEventResponse getUserDataEventResponse) {
        gudeManager.complete(getUserDataEventResponse.correlationId(), getUserDataEventResponse);
    }

    @KafkaListener(topics = "fetch-users.comp", groupId = "appointments-service")
    public void fetchUsers(FetchUsersEvent fetchUsersEvent) {
        fueManager.complete(fetchUsersEvent, fetchUsersEvent.correlationId());
    }

    @KafkaListener(topics = "app-info-req", groupId = "appointments-service")
    public void appInfoReqEvent(AppInfoReqEvent appInfoReqEvent) {
        appService.getRequestedAppointments(appInfoReqEvent);
    }
}
