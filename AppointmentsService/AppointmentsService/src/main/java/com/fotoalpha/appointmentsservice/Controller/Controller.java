package com.fotoalpha.appointmentsservice.Controller;

import com.fotoalpha.appointmentsservice.ResponseRequest.GetAllAppointmentsResponse;
import com.fotoalpha.appointmentsservice.Service.appService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class Controller {
    private final appService appService;

    @GetMapping("/allAppointments")
    public ResponseEntity<GetAllAppointmentsResponse> getAllAppointments(Authentication authentication) {
        String uid = authentication.getName();
        return new ResponseEntity<>(appService.getAllAppointmentByUserId(uid), HttpStatus.OK);
    }
}
