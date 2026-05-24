package com.fotoalpha.appointmentsservice.ResponseRequest;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class AdminModifyDetailsRequest {
    LocalDate appointmentDate;
    LocalTime appointmentTime;
}
