package com.fotoalpha.appointmentsservice.KafkaEvents;

import java.util.UUID;

public record DeleteAddressEvent(
        String addressID
) {
}
