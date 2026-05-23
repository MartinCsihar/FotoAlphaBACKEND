package com.fotoalpha.appointmentsservice.ResponseRequest;

import com.fotoalpha.appointmentsservice.Entity.Address;
import com.fotoalpha.appointmentsservice.Entity.PersonalBundles;
import com.fotoalpha.appointmentsservice.Enums.AppointmentType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
@Builder
public class SaveWeddingAppointmentRequest {
    LocalDate appointmentDate;
    LocalTime appointmentTime;
    AppointmentType type;
    Integer bundle;
    Address address;
    PersonalBundles personalBundle;
}
