package com.fotoalpha.appointmentsservice.Controller;

import com.fotoalpha.appointmentsservice.Entity.Appointments;
import com.fotoalpha.appointmentsservice.Repo.AppRepo;
import com.fotoalpha.appointmentsservice.ResponseRequest.GetAllAppointmentsResponse;
import com.fotoalpha.appointmentsservice.Service.appService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final appService appService;
    private final AppRepo repo;

    @PostMapping("/save")
    public String saveApp(@RequestBody Appointments app){
        try {
            repo.save(app);
            return "OK";
        }
        catch (Exception e){
            return e.getMessage();
        }
    }

    @GetMapping("/getAllAppointments")
    public ResponseEntity<GetAllAppointmentsResponse> getAllAppointments(@RequestParam String userId) {
        return new ResponseEntity<>(appService.getAllAppointmentByUserId(userId ), HttpStatus.OK);
    }
}
