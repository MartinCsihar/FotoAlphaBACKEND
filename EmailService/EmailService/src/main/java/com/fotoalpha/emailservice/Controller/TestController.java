package com.fotoalpha.emailservice.Controller;

import com.fotoalpha.emailservice.KafkaEvents.AppointmentCreatedEvent;
import com.fotoalpha.emailservice.Service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final EmailService service;
    @PostMapping("/api/test/appCreated")
    public ResponseEntity<String> testAppCreated(@RequestBody AppointmentCreatedEvent event) throws MessagingException {
        return new ResponseEntity<>(service.sendAppointmentCreatedEmail(event), HttpStatus.OK);
    }
}
