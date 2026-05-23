package com.fotoalpha.appointmentsservice.KafkaEvents;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record AppointmentCreatedEvent(
        String appointmentId,
        String pairLocations,
        String houseNumber,
        String postalCode,
        String streetName,
        String streetType,
        LocalDate orderDate,
        LocalDate appointmentDate,
        LocalTime appointmentTime,
        Integer price,
        String bundleName,
        String appointmentType,
        String state,
        String userEmail,
        String firstName,
        String lastName,
        String phoneNumber


) {
}
