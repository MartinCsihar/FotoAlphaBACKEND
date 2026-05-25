package com.fotoalpha.appointmentsservice.KafkaEvents;

import java.util.List;

public record AppInfoReqEvent(
        String correlationId,
        List<String> appIds
) {
}
