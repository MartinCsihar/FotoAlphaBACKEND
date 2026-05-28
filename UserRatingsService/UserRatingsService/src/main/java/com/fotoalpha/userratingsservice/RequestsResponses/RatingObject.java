package com.fotoalpha.userratingsservice.RequestsResponses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RatingObject {
    int rating;
    LocalDate date;
    AppointmentResponse appointmentResponse;

}
