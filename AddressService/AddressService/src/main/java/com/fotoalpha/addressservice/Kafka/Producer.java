package com.fotoalpha.addressservice.Kafka;

import com.fotoalpha.addressservice.Events.GetAddressesResEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Producer {
    private final KafkaTemplate<String, GetAddressesResEvent> getAddressResTemplate;
    public void sendGetAddressResEvent(GetAddressesResEvent res) {
        getAddressResTemplate.send("address-info-req.comp", res);
    }
}
