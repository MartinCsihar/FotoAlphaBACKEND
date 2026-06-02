package com.fotoalpha.portfolioservice.KafkaEvents;

import java.util.List;

public record SavePhotosEvent(
        List<String> URLs
) {
}
