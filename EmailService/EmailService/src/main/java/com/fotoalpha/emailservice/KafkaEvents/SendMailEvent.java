package com.fotoalpha.emailservice.KafkaEvents;


public record SendMailEvent(
        String email,
        String resetID
) {
}
