package com.fotoalpha.userservice.KafkaEvents;

import lombok.Builder;

import java.util.List;

@Builder
public record UserInfoReqEvent(
        String correlationId,
        List<String> userIDs
) {
}
