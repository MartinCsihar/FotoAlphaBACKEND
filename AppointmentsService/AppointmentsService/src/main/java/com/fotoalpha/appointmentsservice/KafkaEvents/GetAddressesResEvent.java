package com.fotoalpha.appointmentsservice.KafkaEvents;

import lombok.Builder;

import java.util.List;

@Builder
public record GetAddressesResEvent(
        String correlationId,
        List<String> addresses
) {
}
