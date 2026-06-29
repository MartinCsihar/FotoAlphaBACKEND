package com.fotoalpha.userservice.KafkaEvents;

import lombok.Builder;

import java.util.List;

@Builder
public record UserInfoResEvent(
        String correlationId,
        List<String> profPicUrls,
        List<String> userNames,
        List<String> userIds
) {
}
