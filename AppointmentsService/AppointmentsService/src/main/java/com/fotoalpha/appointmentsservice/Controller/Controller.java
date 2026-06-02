package com.fotoalpha.appointmentsservice.Controller;

import com.fotoalpha.appointmentsservice.Entity.Appointments;
import com.fotoalpha.appointmentsservice.ResponseRequest.*;
import com.fotoalpha.appointmentsservice.Service.appService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class Controller {
    private final appService appService;

    @GetMapping("/allAppointments")
    public ResponseEntity<List<Appointments>> getAllAppointments(Authentication authentication) {
        String uid = authentication.getName().split("\\:")[0];
        return new ResponseEntity<>(appService.getAllAppointmentsByUserId(uid), HttpStatus.OK);
    }

    @GetMapping("/appointment")
    public ResponseEntity<GetAppointmentByIdResponse> getAppointmentById(@RequestParam("appId") String appId, Authentication authentication) {
        String uid = authentication.getName().split("\\:")[0];
        return new ResponseEntity<>(appService.getAppointmentByAppointmentIdAndUserId(appId, uid), HttpStatus.OK);
    }

    @PutMapping("/cancelAppointment")
    public ResponseEntity<Boolean> cancelAppointment(@RequestParam("appid") String appointmentId, Authentication authentication) {
        String uid = authentication.getName().split("\\:")[0];
        return new ResponseEntity<>(appService.cancelAppointment(uid, appointmentId), HttpStatus.OK);
    }

    @PostMapping("/savePairAppointment")
    public ResponseEntity<Boolean> savePairAppointment(@RequestBody SavePairAppointmentRequest req, Authentication authentication) {
        try{
            String uid = authentication.getName().split("\\:")[0];
            return new ResponseEntity<>(appService.savePairAppointment(req, uid), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PostMapping("/saveWeddingAppointment")
    public ResponseEntity<Boolean> saveWeddingAppointment(@RequestBody SaveWeddingAppointmentRequest req, Authentication authentication) {
        try {
            String uid = authentication.getName().split("\\:")[0];
            return new ResponseEntity<>(appService.saveWeddingAppointment(req, uid), HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/saveOwnMadeWeddingAppointment")
    public ResponseEntity<Boolean> saveOwnMadeWeddingAppointment(@RequestBody SaveOwnMadeWeddingAppointmentRequest req, Authentication authentication) {
        try {
            String uid = authentication.getName().split("\\:")[0];
            return new ResponseEntity<>(appService.saveOwnMadeApp(req, uid), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
