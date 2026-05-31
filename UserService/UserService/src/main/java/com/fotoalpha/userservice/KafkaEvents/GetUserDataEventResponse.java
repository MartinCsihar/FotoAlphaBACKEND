package com.fotoalpha.userservice.KafkaEvents;

import lombok.Builder;

@Builder
public record GetUserDataEventResponse(
        String correlationId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber

) {
}
