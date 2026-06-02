package com.fotoalpha.userservice.KafkaEvents;

import lombok.Builder;

@Builder
public record SaveProfilePictureEvent(
        String uid,
        String key
) {
}
