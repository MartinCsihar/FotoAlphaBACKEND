package com.fotoalpha.userservice.KafkaEvents;

import lombok.Builder;

@Builder
public record GetUserDataEvent(String UserID, String correlationId) {
}
