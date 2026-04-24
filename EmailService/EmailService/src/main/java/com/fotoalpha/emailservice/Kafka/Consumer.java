package com.fotoalpha.emailservice.Kafka;

import com.fotoalpha.emailservice.Objects.Events.AppointmentCreatedEvent;
import com.fotoalpha.emailservice.Objects.Events.SendPwResetEvent;
import com.fotoalpha.emailservice.Service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Consumer {
    private final EmailService emailService;
    @KafkaListener(topics = "appointment-created", groupId = "email-service")
    public void consume(AppointmentCreatedEvent event) throws MessagingException {
       emailService.sendAppointmentCreatedEmail(event);
    }

    @KafkaListener(topics = "pw-reset-send-email", groupId = "email-service")
    public void consumePwResetSendEmail(SendPwResetEvent event) throws MessagingException {
            emailService.sendPwResetEmail(event);
    }
}
