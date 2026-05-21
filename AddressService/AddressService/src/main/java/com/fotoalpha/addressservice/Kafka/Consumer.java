package com.fotoalpha.addressservice.Kafka;


import com.fotoalpha.addressservice.Events.DeleteAddressEvent;
import com.fotoalpha.addressservice.Events.SaveAddressEvent;
import com.fotoalpha.addressservice.Service.aService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Consumer {
    private final aService service;

    @KafkaListener(topics = "save-address", groupId = "address-service")
    public void saveAddress(SaveAddressEvent event) {
        log.info("Received SaveAddressEvent {}", event);
        service.saveAddress(event);
    }

    @KafkaListener(topics = "delete-address", groupId = "address-service")
    public void deleteAddress(DeleteAddressEvent event) {
        service.deleteAddress(event);
    }
}
