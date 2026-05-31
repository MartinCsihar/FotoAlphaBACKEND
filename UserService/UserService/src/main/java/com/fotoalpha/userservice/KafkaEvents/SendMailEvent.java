package com.fotoalpha.userservice.KafkaEvents;

import lombok.Builder;

@Builder
public record SendMailEvent(
        String email,
        String resetID
) {
}
