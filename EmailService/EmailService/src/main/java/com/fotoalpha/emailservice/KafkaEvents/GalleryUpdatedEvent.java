package com.fotoalpha.emailservice.KafkaEvents;

import lombok.Builder;

@Builder
public record GalleryUpdatedEvent(
        String firstName,
        String email,
        int photoCount,
        int videoCount
) {

}
