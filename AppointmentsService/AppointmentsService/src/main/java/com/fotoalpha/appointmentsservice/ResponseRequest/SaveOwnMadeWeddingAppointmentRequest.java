package com.fotoalpha.appointmentsservice.ResponseRequest;

import com.fotoalpha.appointmentsservice.Entity.Address;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class SaveOwnMadeWeddingAppointmentRequest {
    Integer rawPhotos;
    Integer editedPhotos;
    Integer instantPhotos;
    Boolean lightPaintPhotos;
    Address address;
    LocalDate orderDate;
    LocalDate appointmentDate;
    LocalTime appointmentTime;
    List<Integer> eventIds;
}
