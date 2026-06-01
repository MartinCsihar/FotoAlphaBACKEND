package com.fotoalpha.userservice.KafkaEvents;

import lombok.Builder;

@Builder
public record GalleryUpdatedEvent(
        String firstname,
        String email,
        int photoCount,
        int videoCount
) {

}
