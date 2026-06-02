package com.fotoalpha.awsservice.Events;

import lombok.Builder;

@Builder
public record SaveProfilePictureEvent(
        String uid,
        String key
) {
}
