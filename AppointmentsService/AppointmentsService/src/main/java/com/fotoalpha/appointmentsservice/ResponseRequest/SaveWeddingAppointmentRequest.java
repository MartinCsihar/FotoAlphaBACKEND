package com.fotoalpha.appointmentsservice.ResponseRequest;

import com.fotoalpha.appointmentsservice.Enums.AppointmentType;
import com.fotoalpha.appointmentsservice.Enums.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class SaveWeddingAppointmentRequest {
    LocalDate orderDate;
    LocalDate appointmentDate;
    LocalTime appointmentTime;
    AppointmentType appointmentType;
    Status status;
    Integer bundleId;
}
