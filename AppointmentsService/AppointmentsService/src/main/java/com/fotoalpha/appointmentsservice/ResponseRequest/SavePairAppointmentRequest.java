package com.fotoalpha.appointmentsservice.ResponseRequest;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class SavePairAppointmentRequest {
    LocalDate appointmentDate;
    LocalTime appointmentTime;
    Integer bundleId;
}
