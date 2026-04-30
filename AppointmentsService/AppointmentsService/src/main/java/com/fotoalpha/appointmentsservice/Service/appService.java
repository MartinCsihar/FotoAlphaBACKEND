package com.fotoalpha.appointmentsservice.Service;

import com.fotoalpha.appointmentsservice.Entity.Appointments;
import com.fotoalpha.appointmentsservice.Repo.AppRepo;
import com.fotoalpha.appointmentsservice.ResponseRequest.GetAllAppointmentsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class appService {
    private final AppRepo appRepo;

    public GetAllAppointmentsResponse getAllAppointmentByUserId(String uid) {
        List<Appointments> querriedApps = appRepo.findAllByUserId(uid);
        return GetAllAppointmentsResponse.builder().appointments(querriedApps).build();
    }
}
