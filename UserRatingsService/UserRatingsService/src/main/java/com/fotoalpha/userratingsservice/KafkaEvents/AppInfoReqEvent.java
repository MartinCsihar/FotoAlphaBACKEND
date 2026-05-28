package com.fotoalpha.userratingsservice.KafkaEvents;

import lombok.Builder;

import java.util.List;

@Builder
public record AppInfoReqEvent(
        String correlationId,
        List<String> appIds

) {

}
