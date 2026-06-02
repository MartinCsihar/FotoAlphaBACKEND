package com.fotoalpha.emailservice.Kafka;

import com.fotoalpha.emailservice.KafkaEvents.AppointmentCreatedEvent;
import com.fotoalpha.emailservice.KafkaEvents.GalleryUpdatedEvent;
import com.fotoalpha.emailservice.KafkaEvents.SendMailEvent;
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
    public void consumePwResetSendEmail(SendMailEvent event) throws MessagingException {
            emailService.sendPwResetEmail(event);
    }

    @KafkaListener(topics = "gallery-updated", groupId = "email-service")
    public void consumeGalleryUpdated(GalleryUpdatedEvent event) throws MessagingException {
        emailService.sendGalleryUpdatedEvent(event);
    }
}
