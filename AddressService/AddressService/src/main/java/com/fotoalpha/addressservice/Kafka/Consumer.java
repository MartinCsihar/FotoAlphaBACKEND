package com.fotoalpha.addressservice.Kafka;


import com.fotoalpha.addressservice.Events.DeleteAddressEvent;
import com.fotoalpha.addressservice.Events.SaveAddressEvent;
import com.fotoalpha.addressservice.Service.aService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Consumer {
    private final aService service;

    @KafkaListener(topics = "save-address", groupId = "address-service")
    public void saveAddress(SaveAddressEvent event) {
        service.saveAddress(event);
    }

    @KafkaListener(topics = "delete-address", groupId = "address-service")
    public void deleteAddress(DeleteAddressEvent event) {
        service.deleteAddress(event);
    }
}
