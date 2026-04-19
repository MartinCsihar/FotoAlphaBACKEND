package com.fotoalpha.portfolioservice.KafkaEvents;

import lombok.AllArgsConstructor;

import java.util.List;

public record SavePhotosEvent(
        List<String> presignedURLs
) {
}
