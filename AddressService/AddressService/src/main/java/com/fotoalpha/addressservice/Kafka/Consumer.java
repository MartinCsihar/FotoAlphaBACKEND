package com.fotoalpha.addressservice.Kafka;


import com.fotoalpha.addressservice.Events.DeleteAddressEvent;
import com.fotoalpha.addressservice.Events.GetAddressesReqEvent;
import com.fotoalpha.addressservice.Events.SaveAddressEvent;
import com.fotoalpha.addressservice.Service.aService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class Consumer {
    private final aService service;

    @KafkaListener(topics = "save-address", groupId = "address-service-2")
    public void saveAddress(SaveAddressEvent event) {
        log.info("Received SaveAddressEvent {}", event);
        service.saveAddress(event);
    }

    @KafkaListener(topics = "delete-address", groupId = "address-service-2")
    public void deleteAddress(DeleteAddressEvent event) {
        service.deleteAddress(event);
    }

    @KafkaListener(topics = "address-info-req", groupId = "address-service-2")
    public void getAddress(GetAddressesReqEvent req){
        log.info("Received GetAddressesReqEvent {}", req);
        service.getLocations(req);
    }
}
