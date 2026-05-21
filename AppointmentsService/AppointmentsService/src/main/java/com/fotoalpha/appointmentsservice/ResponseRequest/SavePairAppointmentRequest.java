package com.fotoalpha.appointmentsservice.ResponseRequest;

import com.fotoalpha.appointmentsservice.Entity.Bundles;
import com.fotoalpha.appointmentsservice.Enums.AppointmentType;
import com.fotoalpha.appointmentsservice.Enums.Status;
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
