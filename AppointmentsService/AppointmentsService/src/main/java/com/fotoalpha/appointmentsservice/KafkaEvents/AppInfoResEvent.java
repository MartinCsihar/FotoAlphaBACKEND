package com.fotoalpha.appointmentsservice.KafkaEvents;

import com.fotoalpha.appointmentsservice.Entity.Appointments;

import java.util.List;

public record AppInfoResEvent(

        String correlationId,
        List<Appointments> querriedAppointments
) {
}

