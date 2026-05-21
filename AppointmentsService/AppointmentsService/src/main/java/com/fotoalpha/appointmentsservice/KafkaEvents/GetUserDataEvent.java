package com.fotoalpha.appointmentsservice.KafkaEvents;

import lombok.Builder;

@Builder
public record GetUserDataEvent(String UserID, String correlationId) {
}
