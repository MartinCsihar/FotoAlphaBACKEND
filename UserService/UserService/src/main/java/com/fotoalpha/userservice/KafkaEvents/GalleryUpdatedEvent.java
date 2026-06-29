package com.fotoalpha.userservice.KafkaEvents;

import lombok.Builder;

@Builder
public record GalleryUpdatedEvent(
        String firstName,
        String email,
        Integer photoCount,
        Integer videoCount,
        String type
) {

}
