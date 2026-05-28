package com.fotoalpha.userratingsservice.KafkaEvents;

import com.fotoalpha.userratingsservice.RequestsResponses.AppointmentResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record AppInfoResEvent(
        String correlationId,
        List<AppointmentResponse> querriedAppointments
) {
}
