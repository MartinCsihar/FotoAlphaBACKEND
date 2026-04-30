package com.fotoalpha.appointmentsservice.ResponseRequest;

import com.fotoalpha.appointmentsservice.Entity.Appointments;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetAppointmentByIdResponse {
    Appointments querriedAppointment;
}
