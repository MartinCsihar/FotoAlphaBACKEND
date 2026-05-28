package com.fotoalpha.appointmentsservice.KafkaEvents;


import java.util.List;

public record AppInfoResEvent(

        String correlationId,
        List<AppointmentResponse> querriedAppointments
) {
}

