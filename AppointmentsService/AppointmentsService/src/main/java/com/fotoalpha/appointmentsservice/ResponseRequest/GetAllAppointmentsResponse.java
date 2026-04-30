package com.fotoalpha.appointmentsservice.ResponseRequest;

import com.fotoalpha.appointmentsservice.Entity.Appointments;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetAllAppointmentsResponse {
    List<Appointments> appointments;
}
