package com.fotoalpha.portfolioservice.Kafka;

import com.fotoalpha.portfolioservice.KafkaEvents.SavePhotosEvent;
import com.fotoalpha.portfolioservice.Service.KafkaService.KafkaService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Consumer {
    private final KafkaService kafkaService;

    @KafkaListener(topics = "save-photos", groupId = "portfolio-service")
    public void listen(SavePhotosEvent event){
        kafkaService.savePhotos(event);
    }
}
