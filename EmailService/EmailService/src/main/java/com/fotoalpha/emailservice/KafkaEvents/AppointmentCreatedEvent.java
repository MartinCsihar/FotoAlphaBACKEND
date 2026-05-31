package com.fotoalpha.emailservice.Objects.Events;

import java.time.LocalDate;
import java.time.LocalTime;

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
